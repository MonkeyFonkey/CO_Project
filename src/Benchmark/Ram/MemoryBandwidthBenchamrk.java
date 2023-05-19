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

        if (transferRate < 100000000) {
            score = 1.0;
        } else if (transferRate < 200000000) {
            score = 2.0;
        } else if (transferRate < 300000000) {
            score = 3.0;
        } else if (transferRate < 400000000) {
            score = 4.0;
        } else if (transferRate < 500000000) {
            score = 5.0;
        } else if (transferRate < 600000000) {
            score = 6.0;
        } else if (transferRate < 700000000) {
            score = 7.0;
        } else if (transferRate < 800000000) {
            score = 8.0;
        } else if (transferRate < 900000000) {
            score = 9.0;
        } else {
            score = 10.0;
        }


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
