package Benchmark.Ram;
import Benchmark.iBenchmark;

import java.text.DecimalFormat;

import static Benchmark.Ram.MemBandwidthImplementation.measureMemoryBandwidth;

public class MemoryBandwidthBenchamrk implements iBenchmark {

    private double score;

    @Override
    public void run() {
        long transferRate = measureMemoryBandwidth();
        score = calculateScore(transferRate);
    }

    @Override
    public void initialize(int param) {
        //nothing
    }


    public double getScore(){
        return score;
    }

    private static double calculateScore(long transferRate) {
        double score;
        score = (double) transferRate / 100000000;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedScore = decimalFormat.format(score);

        return Double.parseDouble(formattedScore);
    }

    @Override
    public String getResult() {
        return null;
    }
}
