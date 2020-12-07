package it.duck.sanner.listener;

import it.duck.sanner.StandardSettings;
import org.soulwing.snmp.SnmpFactory;
import org.soulwing.snmp.SnmpListener;
import org.soulwing.snmp.SnmpNotificationEvent;
import org.soulwing.snmp.SnmpNotificationHandler;

public class SNMPListener implements SnmpNotificationHandler {

    private SnmpListener listener;

    public SNMPListener() {
        listener = SnmpFactory.getInstance().newListener(StandardSettings.getMIB());
    }

    public SNMPListener(int port) {
        changePort(port);
    }

    public void changePort(int port) {
        if(port < 1024 || port > 65536) {
            throw new IllegalArgumentException("Port has to be greater than 1024 and less than 65536");
        }

        listener = SnmpFactory.getInstance().newListener(port, StandardSettings.getMIB());
    }

    public void start() {
        if(listener != null) {
            System.out.println("Listener started");
            listener.addHandler(this);
        }
    }

    public void stop() {
        if(listener != null) {
            System.out.println("Listener stopped!");
            listener.close();
        }
    }

    @Override
    public Boolean handleNotification(SnmpNotificationEvent e) {
        System.out.println("New Trap/Inform: " + e);
        return true;
    }
}
