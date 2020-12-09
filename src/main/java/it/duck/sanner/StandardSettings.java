package it.duck.sanner;


import org.soulwing.snmp.Mib;
import org.soulwing.snmp.MibFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class StandardSettings {

    private static final Mib defaultMib;

    static {
        defaultMib = MibFactory.getInstance().newMib();
        List<String> mibs = new ArrayList<>();

        try {
            File f = new File(StandardSettings.class.getResource("/mibs.txt").toURI());
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line;
            while((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty()) {
                    mibs.add(line);
                }
            }
        } catch (URISyntaxException | IOException ignore) {}

        for(String m : mibs) {
            try {
                defaultMib.load(m);
            } catch (IOException e) {
                System.err.println("Failed to load Mib: " + m);
            }
        }
    }

    /**
     * Gibt die angegebenen Communities und die Standard Communities zurück
     * @param communities Benutzerdefinierte Communities
     * @return Alle Communities
     */
    public static List<String> getCommunities(List<String> communities) {
        if(communities == null) {
            communities = new ArrayList<>();
        }
        communities.add("public");
        communities.add("private");
        return communities;
    }

    /**
     * Gibt die Standard Communities [public, private] zurück
     * @return Die Communities
     */
    public static List<String> getCommunities() {
        return getCommunities(null);
    }

    /**
     * Gibt ein Standard MIB-Object zurück
     * @return Das MIB-Objekt
     */
    public static Mib getMIB() {
        return defaultMib;
    }

    /**
     * Gibt das Standard MIB-Objekt zurück bei dem auch die <code>customMibs</code> geladen wurden
     * @param customMibs Liste der benutzerdefinierten MIBs
     * @return Das MIB-Objekt
     */
    public static Mib getMIB(List<String> customMibs) {
        if(customMibs != null && !customMibs.isEmpty()) {
            for(String m : customMibs) {
                try {
                    defaultMib.load(m);
                } catch (IOException e) {
                    System.err.println("Failed to load Mib: " + m);
                }
            }
        }

        return defaultMib;
    }

    /**
     * Gibt die angegebene OIDs, sowie die standard OIDs zurück
     * @param oids Benutzerdefinierte OIDs
     * @return Die OIDs
     */
    public static List<String> getOIDs(List<String> oids) {
        if(oids == null) {
            oids = new ArrayList<>();
        }

        oids.add("sysName");
        oids.add("sysUpTime");
        oids.add("sysDescr");
        oids.add("ipAdEntAddr");

        return oids;
    }

    /**
     * Gibt die Standard OIDs [sysUpTime, sysName, sysDecr, ipAdEntAddr] zurück
     * @return Die OIDs
     */
    public static List<String> getOIDs() {
        return getOIDs(null);
    }
}
