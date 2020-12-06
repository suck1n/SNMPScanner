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

    private static Mib defaultMib;

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

    public static List<String> getCommunities() {
        List<String> communities = new ArrayList<>();
        communities.add("public");
        communities.add("private");
        return communities;
    }

    public static Mib getMIBs(List<String> customMibs) {
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

    public static List<String> getOIDS() {
        List<String> oids = new ArrayList<>();
        oids.add("sysUpTime");
        oids.add("sysName");
        oids.add("ipAdEntAddr");

        return oids;
    }
}
