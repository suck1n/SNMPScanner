package it.duck.scanner.listener;

import it.duck.gui.GUI;
import it.duck.scanner.StandardSettings;
import org.soulwing.snmp.SnmpFactory;
import org.soulwing.snmp.SnmpListener;
import org.soulwing.snmp.SnmpNotificationEvent;
import org.soulwing.snmp.VarbindCollection;

public class SNMPListener {

    private SnmpListener listener;
    private int port;

    /**
     * Default Constructor
     * <br>
     * Erstellt einen Listener mit dem Standardport (162)
     *
     */
    public SNMPListener() {
        this(162);
    }

    /**
     * Erstellt einen Listener der auf den definierten <code>port</code> hört.
     * @param port Der Port
     */
    public SNMPListener(int port) {
        this.port = port;
    }

    /**
     * Ändert den Port
     * @param port Der Port
     */
    public void changePort(int port) {
        if(port < 1 || port > 65536) {
            throw new IllegalArgumentException("Port has to be greater than 1 and less than 65536");
        }

        this.port = port;
    }

    /**
     * Startet den Listener
     */
    public void start() {
        if(listener == null) {
            listener = SnmpFactory.getInstance().newListener(port, StandardSettings.getMIB(GUI.getInstance().getSettingMIBs()));
            listener.addHandler(this::handleNotification);
            GUI.getInstance().info("Listener hört jetzt auf dem Port " + port + "!");
        }
    }

    /**
     * Stoppt den Listener
     */
    public void stop() {
        if(listener != null) {
            listener.removeHandler(this::handleNotification);
            listener.close();
            GUI.getInstance().info("Listener wurde gestoppt!");
            listener = null;
        }
    }

    private Boolean handleNotification(SnmpNotificationEvent e) {
        String ip = e.getSubject().getPeer().getAddress();
        VarbindCollection result = e.getSubject().getVarbinds();

        GUI.getInstance().info("Neuer Trap/Inform von " + ip + "!");

        GUI.getInstance().addListenerResult("Results", ip, result);

        return true;
    }
}
