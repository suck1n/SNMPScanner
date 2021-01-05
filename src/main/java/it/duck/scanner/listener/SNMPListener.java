package it.duck.scanner.listener;

import it.duck.scanner.StandardSettings;
import org.soulwing.snmp.SnmpFactory;
import org.soulwing.snmp.SnmpListener;
import org.soulwing.snmp.SnmpNotificationEvent;

public class SNMPListener {

    private SnmpListener listener;

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
        changePort(port);
    }

    /**
     * Ändert den Port
     * @param port Der Port
     */
    public void changePort(int port) {
        if(port < 1024 || port > 65536) {
            throw new IllegalArgumentException("Port has to be greater than 1024 and less than 65536");
        }

        // TODO Get Settings Mibs
        listener = SnmpFactory.getInstance().newListener(port, StandardSettings.getMIB(null));
    }

    /**
     * Startet den Listener
     */
    public void start() {
        if(listener != null) {
            System.out.println("Listener started");
            listener.addHandler(this::handleNotification);
        }
    }

    /**
     * Stoppt den Listener
     */
    public void stop() {
        if(listener != null) {
            System.out.println("Listener stopped!");
            listener.close();
        }
    }

    // TODO Add to Table
    private Boolean handleNotification(SnmpNotificationEvent e) {
        System.out.println("New Trap/Inform: " + e);
        return true;
    }
}
