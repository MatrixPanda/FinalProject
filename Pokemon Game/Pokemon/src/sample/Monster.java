package sample;

import java.util.Scanner;
import java.util.Random;
import java.text.DecimalFormat;


// Represents a Pokemon	with four available	moves
public class Monster {

    private String name;
    private String type;

    private int hp;
    private int speed;
    private int attack;
    private int defence;

    Move move1;
    Move move2;
    Move move3;
    Move move4;

    public Monster(String name, String type, int hp, int attack, int defence, int speed,
                   Move m1, Move m2, Move m3, Move m4) {
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.attack = attack;
        this.defence = defence;
        this.speed = speed;

        this.move1 = m1;
        this.move2 = m2;
        this.move3 = m3;
        this.move4 = m4;
    }

    // Get and set methods to access the private variables
    public String getName() {
        return name;
    }

    public int getHP() {
        return hp;
    }

    // Used to update hp after each attack
    public int setHP(int hitPointsUpdated) {
        this.hp = hitPointsUpdated;
        return hp;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAtk() {
        return attack;
    }

    public int getDef() {
        return defence;
    }
}
