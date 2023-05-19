package Benchmark;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class NetworkInfo {
    public static String getPCID() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            String userName = System.getProperty("user.name");
            StringBuilder sb = new StringBuilder();
            if (mac != null) {
                for (byte b : mac) {
                    sb.append(String.format("%02X", b));
                }
            }
            return sb + "-" + userName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
