package Benchmark;

import com.sun.management.OperatingSystemMXBean;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Scanner;

public class CpuInfo {
    private OperatingSystemMXBean osBean;

    public CpuInfo() {
        for (com.sun.management.OperatingSystemMXBean bean : ManagementFactory.getPlatformMXBeans(com.sun.management.OperatingSystemMXBean.class)) {
            if (bean != null) {
                osBean = bean;
                break;
            }
        }
        if (osBean == null) {
            throw new RuntimeException("Could not find an instance of OperatingSystemMXBean");
        }
    }

    public String getArchitecture() {
        return osBean.getArch();
    }

    public int getAvailableProcessors() {
        return osBean.getAvailableProcessors();
    }

    public double getCpuUsage() {
        return osBean.getProcessCpuLoad();
    }

    public String getCpuModel() {
            String osName = System.getProperty("os.name").toLowerCase();
            //String arch = System.getProperty("os.arch").toLowerCase();
            String cpuModelWindows = "Unknown";

        if (osName.contains("win")) { // Windows
            try {
                Process process = Runtime.getRuntime().exec("wmic cpu get name");
                Scanner scanner = new Scanner(process.getInputStream());
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty() && !line.equalsIgnoreCase("name")) {
                        cpuModelWindows = line;
                        break;
                    }
                }
                scanner.close();
                return  cpuModelWindows.trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            } else if (osName.contains("mac")) { // macOS
                try {
                    Process process = Runtime.getRuntime().exec("sysctl -n machdep.cpu.brand_string");
                    Scanner scanner = new Scanner(process.getInputStream());
                    String cpuModel = scanner.nextLine();
                    scanner.close();
                    return cpuModel.trim();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Unknown";
                }
            } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) { // Linux/Unix
                try {
                    Process process = Runtime.getRuntime().exec("cat /proc/cpuinfo | grep 'model name' | uniq");
                    Scanner scanner = new Scanner(process.getInputStream());
                    String cpuModel = scanner.nextLine();
                    scanner.close();
                    return cpuModel.substring(cpuModel.indexOf(':') + 1).trim();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Unknown";
                }
            } else { // Unsupported OS
                return "Unknown";
            }

        return "Unknown";
    }



}
