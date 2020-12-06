package it.duck.sanner;

import it.duck.utility.IPHelper;
import org.soulwing.snmp.*;

import java.util.List;

public class Scanner {


    public void scanNetwork(String ip, int mask, List<String> mibs, List<String> oids, List<String> communities) {
        if(ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP cannot be empty!");
        }

        if(mask < 0 || mask > 32) {
            throw new IllegalArgumentException("Mask has to be smaller than 32 and greater than 0");
        }


        for(String address : IPHelper.calculateNetwork(ip, mask)) {
            scanIP(ip, mibs, oids, communities);
        }
    }

    public void scanIP(String ip, List<String> mibs, List<String> oids, List<String> communities) {
        if(ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP cannot be empty!");
        }

        if(communities == null || communities.isEmpty()) {
            communities = StandardSettings.getCommunities();
        }

        if(oids == null || oids.isEmpty()) {
            oids = StandardSettings.getOIDS();
        }

        Mib mib = StandardSettings.getMIBs(mibs);

        SimpleSnmpV2cTarget target = new SimpleSnmpV2cTarget();
        target.setAddress(ip);

        for(String community : communities) {
            target.setCommunity(community);

            try (SnmpContext context = SnmpFactory.getInstance().newContext(target, mib)) {
                context.asyncGetNext(this::callBack, oids);
            }
        }
    }


    private void callBack(SnmpEvent<VarbindCollection> event) {
        VarbindCollection result = event.getResponse().get();

        for(Varbind varbind : result.asList()) {
            System.out.println(varbind);
        }
    }
}
