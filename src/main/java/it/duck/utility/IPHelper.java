package it.duck.utility;

import java.util.ArrayList;

public class IPHelper {


    /**
     * Berechnet alle IPs des Netzwerkes <code>ip</code> mithilfe der Maske
     *
     * @param ip Die IP
     * @param mask Die Maske
     * @return Alle IPs des Netzwerkes
     */
    public static ArrayList<String> calculateNetwork(String ip, int mask) {
        String lastIP = getLastIP(ip, mask);

        return calculateNetwork(ip, lastIP);
    }

    /**
     * Berechnet alle IPs zwischen der <code>startIP</code> und der <code>lastIP</code>
     *
     * @param startIP Die Start-IP
     * @param lastIP Die letzte IP
     * @return Alle IPs zwischen den IPs
     */
    public static ArrayList<String> calculateNetwork(String startIP, String lastIP) {

        int[] ipParts = getIPParts(startIP);
        int[] lastIPParts = getIPParts(lastIP);

        ArrayList<String> ips = new ArrayList<>();
        for(int first = ipParts[0]; first <= lastIPParts[0]; first++) {
            for(int second = ipParts[1]; second <= lastIPParts[1]; second++) {
                for(int third = ipParts[2]; third <= lastIPParts[2]; third++) {
                    for(int fourth = ipParts[3]; fourth <= lastIPParts[3]; fourth++) {
                        ips.add(first + "." + second + "." + third + "." + fourth);
                    }
                }
            }
        }
        return ips;
    }


    private static String getLastIP(String ip, int mask) {
        String maskBinary = getBinaryMask(mask);
        String ipBinary = getBinaryIP(ip);

        StringBuilder lastIPBuilder= new StringBuilder();

        for(int i = 0; i < 4; i++) {
            StringBuilder binary = new StringBuilder();
            for(int j = 0; j < ipBinary.length() / 4; j++) {
                char maskChar = maskBinary.charAt(i * 8 + j);
                char ipChar = ipBinary.charAt(i * 8 +j);

                if(maskChar == '1' || ipChar == '1') {
                    binary.append(1);
                } else {
                    binary.append(0);
                }
            }

            lastIPBuilder.append(Integer.parseInt(binary.toString(), 2)).append(".");
        }

        lastIPBuilder.deleteCharAt(lastIPBuilder.length() - 1);

        return lastIPBuilder.toString();
    }

    private static String getBinaryIP(String ip) {
        int[] parts = getIPParts(ip);

        StringBuilder ipBuilder = new StringBuilder();
        for(int i = 0; i < 4; i++) {
            String binary = String.format("%8s", Integer.toBinaryString(parts[i])).replace(" ", "0");
            ipBuilder.append(binary);
        }

        return ipBuilder.toString();
    }

    private static String getBinaryMask(int mask) {
        StringBuilder maskBuilder = new StringBuilder();
        for(int i = 0; i < 32; i++) {
            if(i < mask) {
                maskBuilder.append(0);
            } else {
                maskBuilder.append(1);
            }
        }

        return maskBuilder.toString();
    }

    private static int[] getIPParts(String ip) {
        String[] stringParts = ip.split("\\.");
        int[] parts = new int[4];

        for(int i = 0; i < stringParts.length; i++) {
            parts[i] = Integer.parseInt(stringParts[i]);
        }

        return parts;
    }
}
