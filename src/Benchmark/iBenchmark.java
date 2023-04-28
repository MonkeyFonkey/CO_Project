package Benchmark;

public interface iBenchmark {
    int[] arr = new int[0];

    void run();

    void run(Object ... params);

    void initialize(int param);

    void clean();

    void cancel();

    void warmUp();

    String getResult();
}
