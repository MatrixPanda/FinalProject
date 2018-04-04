package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Time;
import java.util.*;
import java.util.TimerTask;

public class TimeThread extends Thread {
    public int time;

    static int interval;
    static Timer timer;



    public static final int setInterval() {
        if (interval == 1000)
            timer.cancel();
        return ++interval;
    }

    public TimeThread (int secs) {
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        interval = secs;
        System.out.println(secs);
       // textFieldTimer.appendText(String.valueOf(TimeThread.setInterval()));

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println(setInterval());
            }
        }, delay, period);
    }


    public void run() {
        try {
            System.out.print("TIMER");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTime() {
        return this.time;
    }

}
