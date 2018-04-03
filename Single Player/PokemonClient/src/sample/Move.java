package sample;

import java.util.Scanner;
import java.util.Random;
import java.text.DecimalFormat;


// Represents a	single move
public class Move {

    private String name;
    private String type;
    private int power;
    private float accuracy;

    public Move(String name, String type, int power, float accuracy) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy/100;
    }

    // Get methods to access the private variables
    public String getMoveName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public float getAccuracy() {
        return accuracy;
    }


}
