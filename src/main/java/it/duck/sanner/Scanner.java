package it.duck.sanner;

import it.duck.utility.IPHelper;
import org.soulwing.snmp.Mib;
import org.soulwing.snmp.SimpleSnmpV2cTarget;
import org.soulwing.snmp.SnmpContext;
import org.soulwing.snmp.SnmpFactory;

import java.util.List;

public class Scanner {


    public static void scanNetwork(String ip, int mask, List<String> mibs, List<String> oids, List<String> communities) {
        if(ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP cannot be empty!");
        }

        if(mask < 0 || mask > 32) {
            throw new IllegalArgumentException("Mask has to be smaller than 32 and greater than 0");
        }


        for(String address : IPHelper.calculateNetwork(ip, mask)) {
            scanIP(address, mibs, oids, communities);
        }
    }

    public static void scanNetwork(String startIP, String endIP, List<String> mibs, List<String> oids, List<String> communities) {
        if(startIP == null || startIP.trim().isEmpty()) {
            throw new IllegalArgumentException("Start-IP cannot be empty!");
        }

        if(endIP == null || endIP.trim().isEmpty()) {
            throw new IllegalArgumentException("End-IP cannot be empty!");
        }


        for(String address : IPHelper.calculateNetwork(startIP, endIP)) {
            scanIP(address, mibs, oids, communities);
        }
    }

    public static void scanIP(String ip, List<String> mibs, List<String> oids, List<String> communities) {
        if(ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP cannot be empty!");
        }

        if(communities == null || communities.isEmpty()) {
            communities = StandardSettings.getCommunities();
        }

        if(oids == null || oids.isEmpty()) {
            oids = StandardSettings.getOIDs();
        }

        Mib mib = StandardSettings.getMIB(mibs);

        SimpleSnmpV2cTarget target = new SimpleSnmpV2cTarget();
        target.setAddress(ip);

        for(String community : communities) {
            target.setCommunity(community);

            SnmpContext context = SnmpFactory.getInstance().newContext(target, mib);
            context.asyncGetNext(new ResultCallback(community), oids);
        }
    }
}
