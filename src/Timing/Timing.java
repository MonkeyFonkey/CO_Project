package Timing;

public class Timing implements iTiming{
    private int totalTime;
    private long end, start;


    public void start(){
        start = System.nanoTime();
        totalTime = 0;
    }

    public long stop(){
        end = System.nanoTime();
        totalTime += end - start;
        return totalTime;
    }

    public void resume(){
        start = System.nanoTime();
    }

    public long pause(){
        end = System.nanoTime();
        totalTime += end - start;
        return end-start;
    }

    public void reset(){
        start = 0;
        end = 0;
        totalTime = 0;
    }
}
