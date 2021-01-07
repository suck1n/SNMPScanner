package it.duck.scanner;


import it.duck.gui.GUI;
import org.soulwing.snmp.Mib;
import org.soulwing.snmp.MibFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class StandardSettings {

    private static final List<String> mibs;

    static {
        mibs = new ArrayList<>();

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
    }

    /**
     * Gibt die angegebenen Communities oder die Standard Communities zurück falls das Argument <code>null</code> ist
     * @param communities Benutzerdefinierte Communities
     * @return Liste aus Strings mit den Communities
     */
    public static List<String> getCommunities(List<String> communities) {
        if(communities == null || communities.isEmpty()) {
            communities = new ArrayList<>();
            communities.add("public");
            communities.add("private");
        }

        return communities;
    }

    /**
     * Gibt das Standard MIB-Objekt zurück, falls das Argument <code>null</code> ist.
     * Ansonsten gibt es ein MIB-Objekt zurück bei dem die <code>customMibs</code> geladen wurden
     * @param customMibs Liste der benutzerdefinierten MIBs
     * @return Das MIB-Objekt
     */
    public static Mib getMIB(List<String> customMibs) {
        if(customMibs == null || customMibs.isEmpty()) {
            customMibs = mibs;
        }

        Mib mib = MibFactory.getInstance().newMib();

        for(String m : customMibs) {
            try {
                mib.load(m);
            } catch (IOException e) {
                GUI.getInstance().error("Die MIB " + m + " konnte nicht geladen werden!");
            }
        }

        return mib;
    }

    /**
     * Gibt die Standard MIBs zurück
     * @return Liste aus Strings welche die MIBs enthaltet
     */
    public static List<String> getMIBs() {
        return mibs;
    }

    /**
     * Gibt die angegebene OIDs zurück, falls das Argument <code>null</code> ist werden die Standard OIDs zurückgegeben.
     * Mit dem Parameter {@code useGet} wird bestimmt ob bei den Standard OIDs ein Index hinzugefügt werden soll
     * @param oids Benutzerdefinierte OIDs
     * @param useGet Welche Methode genutzt wird
     * @return Liste aus Strings mit den OIDs
     */
    public static List<String> getOIDs(List<String> oids, boolean useGet) {
        if(oids == null || oids.isEmpty()) {
            oids = new ArrayList<>();

            oids.add("sysName" + (useGet ? ".0" : ""));
            oids.add("sysContact" + (useGet ? ".0" : ""));
            oids.add("sysUpTime" + (useGet ? ".0" : ""));
            oids.add("sysDescr" + (useGet ? ".0" : ""));
            oids.add("sysLocation" + (useGet ? ".0" : ""));
            oids.add("sysServices" + (useGet ? ".0" : ""));
        }

        return oids;
    }
}
