package Benchmark.Ram;
import Benchmark.iBenchmark;

import static Benchmark.Ram.MemBandwidthImplementation.measureMemoryBandwidth;

public class MemoryBandwidthBenchamrk implements iBenchmark {

    private double score;

    @Override
    public void run() {
        long transferRate = measureMemoryBandwidth();
        score = calculateScore(transferRate);
    }


    public double getScore(){
        return score;
    }

    private static double calculateScore(long transferRate) {
        double score;
        score = transferRate/100000000;
        return score;
    }
    @Override
    public void run(Object... params) {

    }

    @Override
    public void initialize(int param) {

    }

    @Override
    public void clean() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void warmUp() {

    }

    @Override
    public String getResult() {
        return null;
    }
}
