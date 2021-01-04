package it.duck.scanner;


import org.soulwing.snmp.Mib;
import org.soulwing.snmp.MibFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class StandardSettings {

    private static final Mib defaultMib;

    static {
        defaultMib = MibFactory.getInstance().newMib();
        List<String> mibs = new ArrayList<>();

        try {
            Reader r = new InputStreamReader(StandardSettings.class.getResourceAsStream("/mibs.txt"));
            BufferedReader reader = new BufferedReader(r);

            String line;
            while((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty()) {
                    mibs.add(line);
                }
            }
        } catch (IOException ignore) {}

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
            communities.add("public");
        }
        return communities;
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
     * Gibt die angegebene OIDs, sowie die standard OIDs zurück. Mit dem Parameter {@code useGet}
     * wird, falls {@code true}, ein Index hinzugefügt
     * @param oids Benutzerdefinierte OIDs
     * @param useGet Welche Methode genutzt wird
     * @return Die OIDs
     */
    public static List<String> getOIDs(List<String> oids, boolean useGet) {
        if(oids == null) {
            oids = new ArrayList<>();
        }

        oids.add("sysName" + (useGet ? ".0" : ""));
        oids.add("sysContact" + (useGet ? ".0" : ""));
        oids.add("sysUpTime" + (useGet ? ".0" : ""));
        oids.add("sysDescr" + (useGet ? ".0" : ""));
        oids.add("sysLocation" + (useGet ? ".0" : ""));
        oids.add("sysServices" + (useGet ? ".0" : ""));

        return oids;
    }
}
