package it.duck.sanner;

import org.soulwing.snmp.*;

public class ResultCallback implements SnmpCallback<VarbindCollection> {

    private final String community;

    public ResultCallback(String community) {
        this.community = community;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<VarbindCollection> e) {
        String ip = e.getContext().getTarget().getAddress();
        try {
            VarbindCollection result = e.getResponse().get();
            System.out.println("Got Response from " + ip + " on community " + community);

            for(Varbind varbind : result.asList()) {
                System.out.println("\t- " + varbind.getName() + " = " + varbind);
            }
        } catch (TimeoutException ignore) {
            System.out.println("No Response from " + ip);
        } finally {
            e.getContext().close();
        }
    }
}
