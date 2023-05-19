package Benchmark.Ram;

import java.util.Arrays;

public class MemBandwidthImplementation {
    private static final int ARRAY_SIZE = 1000000;
    private static final int ITERATIONS = 100000;

    public static void main(String[] args) {
        long transferRate = measureMemoryBandwidth();
        System.out.println("Memory Bandwidth: " + transferRate + " bytes/sec");
    }

    static long measureMemoryBandwidth() {
        // Create a large array to perform memory operations
        byte[] data = new byte[ARRAY_SIZE];

        // Perform sequential write operations
        long startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            Arrays.fill(data, (byte) i);
        }
        long endTime = System.nanoTime();

        // Calculate the transfer rate in bytes per second
        long elapsedTimeNs = endTime - startTime;
        return (long) ((long) (ITERATIONS * ARRAY_SIZE) / (elapsedTimeNs / 1e9));
    }
}
