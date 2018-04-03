package sample;

import java.util.*;
import java.util.TimerTask;

public class TimeThread implements Runnable {
    public int time;

    static int interval;
    static Timer timer;



    public static final int setInterval() {
        if (interval == 1)
            timer.cancel();
        return --interval;
    }

    public TimeThread () {
        int delay = 1000;
        int period = 1000;
        int secs = 10;
        timer = new Timer();
        interval = secs;
        System.out.println(secs);

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println(setInterval());
            }
        }, delay, period);
    }


    public void run() {
        try {
            System.out.print("Time Left: ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTime() {
        return this.time;
    }

}
