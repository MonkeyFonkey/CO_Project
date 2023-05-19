package Logging;

public enum TimeUnit {
    Nano, Micro, Milli, Sec;


    public static double toTimeUnit(long time,  TimeUnit unit){
        return switch (unit) {
            case Nano -> time;
            case Micro -> time / 1000.0;
            case Milli -> time / 1000000.0;
            case Sec -> time / 1000000000.0;
        };

    }

}
