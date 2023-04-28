package Benchmark;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class CpuInfo {
    private OperatingSystemMXBean osBean;

    public CpuInfo() {
        osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
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
}
