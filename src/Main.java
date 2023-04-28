import Benchmark.PICalculatorNewtonRaphson;
import Logging.TimeUnit;
import Timing.Timing;
import GUI.GUI;
import Benchmark.CpuInfo;

public class Main {
    public static void main(String[] args) {
//        Timing clock = new Timing();
//        PICalculatorNewtonRaphson pi1 = new PICalculatorNewtonRaphson();
//        pi1.initialize(100);
//        pi1.run();
//        pi1.getResult();

//          new GUI();

        CpuInfo cpuInfo = new CpuInfo();

        System.out.println("CPU Architecture: " + cpuInfo.getArchitecture());
        System.out.println("Available Processors: " + cpuInfo.getAvailableProcessors());
        System.out.println("System Load Average: " + cpuInfo.getSystemLoadAverage());
        System.out.println("CPU Usage: " + cpuInfo.getCpuUsage() * 100 + "%");
    }
}