package Benchmark;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Scanner;

import com.sun.management.OperatingSystemMXBean;

public class CpuInfo {
    private OperatingSystemMXBean osBean;

    public CpuInfo() {
        for (com.sun.management.OperatingSystemMXBean bean : ManagementFactory.getPlatformMXBeans(com.sun.management.OperatingSystemMXBean.class)) {
            if (bean instanceof com.sun.management.OperatingSystemMXBean) {
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

    public double getSystemLoadAverage() {
        return osBean.getSystemLoadAverage();
    }

    public double getCpuUsage() {
        return osBean.getProcessCpuLoad();
    }

    public String getCpuModel() {
            String osName = System.getProperty("os.name").toLowerCase();
            String arch = System.getProperty("os.arch").toLowerCase();

            if (osName.contains("win")) { // Windows
                try {
                    Process process = Runtime.getRuntime().exec("wmic cpu get name");
                    Scanner scanner = new Scanner(process.getInputStream());
                    if (scanner.hasNextLine()) {
                        scanner.nextLine(); // skip the first line (header)
                    }
 //                 scanner.skip(".*\n"); // skip the first line (header)
                    String cpuModel = scanner.nextLine();
                    scanner.close();
                    return cpuModel.trim();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Unknown";
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
    }



}
