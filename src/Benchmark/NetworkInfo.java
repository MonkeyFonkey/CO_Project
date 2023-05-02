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
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X", mac[i]));
                }
            }
            return sb.toString() + "-" + userName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
