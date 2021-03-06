package it.duck.scanner;

import it.duck.utility.IPHelper;
import it.duck.utility.ThreadHandler;
import org.soulwing.snmp.*;

import java.util.List;

public class Scanner {

    /**
     * Scannt ein ganzes Netzwerk von der Start-IP {<code>ip</code>}
     * bis hin zur Broadcast-IP welche mittels der Maske berechnet wird.<br>
     * <br>
     * <strong><i>Example</i></strong>
     * <br><br>
     * Falls man das 192.168.1.0 Netzwerk mit den Standardeinstellungen scannen möchte, würde der Befehl
     * wie folgt aussehen: <br>
     * <code>Scanner.scanNetwork("192.168.1.0", 24, null, null, null, false)</code>
     *
     * @param ip Start IP, darf nicht null sein
     * @param mask Netmask, muss zwischen 0 und 32 liegen
     * @param mibs Custom MIBS, kann null sein
     * @param oids Custom OIDS, kann null sein
     * @param communities Custom Communities, kann null sein
     * @param useGet Welche Methode genutzt werden soll
     */
    public static void scanNetwork(String ip, int mask, List<String> mibs, List<String> oids, List<String> communities, boolean useGet) {
        if(ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP cannot be empty!");
        }

        if(mask < 0 || mask > 32) {
            throw new IllegalArgumentException("Mask has to be smaller than 32 and greater than 0");
        }

        communities = StandardSettings.getCommunities(communities);
        oids = StandardSettings.getOIDs(oids, useGet);
        Mib mib = StandardSettings.getMIB(mibs);

        for(String address : IPHelper.calculateNetwork(ip, mask)) {
            scan(address, mib, oids, communities, useGet);
        }
    }

    /**
     * Scannt ein ganzes Netzwerk von der Start-IP <code>startIP</code>
     * bis hin zur End-IP <code>endIP</code>.<br>
     * <br>
     * <strong><i>Example</i></strong>
     * <br><br>
     * Falls man ein Netzwerk startend von 192.168.1.15 bis hin zu 192.168.1.30 mit den
     * Standardeinstellungen scannen möchte, würde der Befehl wie folgt aussehen: <br>
     * <code>Scanner.scanNetwork("192.168.1.15", "192.168.1.30", null, null, null, false)</code>
     *
     * @param startIP Start IP, darf nicht null sein
     * @param endIP End IP, darf nicht null sein
     * @param mibs Custom MIBS, kann null sein
     * @param oids Custom OIDS, kann null sein
     * @param communities Custom Communities, kann null sein
     * @param useGet Welche Methode genutzt werden soll
     */
    public static void scanNetwork(String startIP, String endIP, List<String> mibs, List<String> oids, List<String> communities, boolean useGet) {
        if(startIP == null || startIP.trim().isEmpty()) {
            throw new IllegalArgumentException("Start-IP cannot be empty!");
        }

        if(endIP == null || endIP.trim().isEmpty()) {
            throw new IllegalArgumentException("End-IP cannot be empty!");
        }

        communities = StandardSettings.getCommunities(communities);
        oids = StandardSettings.getOIDs(oids, useGet);
        Mib mib = StandardSettings.getMIB(mibs);

        for(String address : IPHelper.calculateNetwork(startIP, endIP)) {
            scan(address, mib, oids, communities, useGet);
        }
    }

    /**
     * Scannt eine einzelne IP.<br>
     * <br>
     * <strong><i>Example</i></strong>
     * <br><br>
     * Falls man die IP 192.168.1.1 mit den Standardeinstellungen scannen möchte, würde der Befehl wie folgt aussehen:
     * <br>
     * <code>Scanner.scanIP("192.168.1.1", null, null, null, false)</code>
     * @param ip IP-Adresse, darf nicht null sein
     * @param mibs Custom MIBS, kann null sein
     * @param oids Custom OIDS, kann null sein
     * @param communities Custom Communities, kann null sein
     * @param useGet Welche Methode genutzt werden soll
     */
    public static void scanIP(String ip, List<String> mibs, List<String> oids, List<String> communities, boolean useGet) {
        if(ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP cannot be empty!");
        }

        communities = StandardSettings.getCommunities(communities);
        oids = StandardSettings.getOIDs(oids, useGet);
        Mib mib = StandardSettings.getMIB(mibs);

        scan(ip, mib, oids, communities, useGet);
    }

    private static void scan(String ip, Mib mib, List<String> oids, List<String> communities, boolean useGet) {
        ThreadHandler.startNewThread(() -> {
            SimpleSnmpV2cTarget target = new SimpleSnmpV2cTarget();
            target.setAddress(ip);

            SimpleSnmpTargetConfig config = new SimpleSnmpTargetConfig();
            config.setTimeout(2000);
            config.setRetries(1);

            for(String community : communities) {
                target.setCommunity(community);

                SnmpContext context = SnmpFactory.getInstance().newContext(target, mib, config, null);
                if(useGet) {
                    context.asyncGet(new ResultCallback(community), oids);
                } else {
                    context.asyncGetNext(new ResultCallback(community), oids);
                }
            }
        });
    }
}
