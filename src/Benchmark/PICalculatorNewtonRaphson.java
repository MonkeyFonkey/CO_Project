package Benchmark;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class PICalculatorNewtonRaphson implements iBenchmark{
    int[] arr;
    BigDecimal result;

    private int numDigits;

    // Newton-Raphson formula

    public BigDecimal calculatePi() {
        // initialize variables
        BigDecimal pi = BigDecimal.ZERO;
        BigDecimal a = BigDecimal.ONE;
        BigDecimal b = BigDecimal.ONE.divide(sqrt(new BigDecimal("2"), numDigits), numDigits, RoundingMode.HALF_UP);
        BigDecimal t = new BigDecimal("0.25");
        BigDecimal p = BigDecimal.ONE;

        // perform iterations using Newton-Raphson formula
        for (int i = 0; i < numDigits; i++) {
            BigDecimal aNext = a.add(b).divide(new BigDecimal("2"), numDigits, RoundingMode.HALF_UP);
            BigDecimal bNext = sqrt(a.multiply(b), numDigits);
            BigDecimal tNext = t.subtract(p.multiply(a.subtract(aNext).pow(2)));
            BigDecimal pNext = p.multiply(new BigDecimal("2"));

            // update variables
            a = aNext;
            b = bNext;
            t = tNext;
            p = pNext;
        }

        // calculate final value of pi
        pi = a.add(b).pow(2).divide(t.multiply(new BigDecimal("4")), numDigits, RoundingMode.HALF_UP);

        return pi;
    }

    public int getNumDigits() {
        return numDigits;
    }

    private BigDecimal sqrt(BigDecimal num, int numDigits) {
        // calculate square root using BigDecimal class
        return BigDecimal.valueOf(Math.sqrt(num.doubleValue())).setScale(numDigits, RoundingMode.HALF_UP);
    }



    public void run(){
        result = calculatePi();
    }

    public void run(Object ... params){
            //Null
    }

    public void initialize(int digits){
        this.numDigits = digits;
    }

    public void clean(){

    }

    public void cancel(){


    }
    public void warmUp(){
        PICalculatorNewtonRaphson cp = new PICalculatorNewtonRaphson();
        cp.initialize(30);
        for(int i = 0; i < 2; i++){
            cp.run();
        }
    }

    public String getResult() {
        return "PI number is: " + result;
    }

}
