package sample;

import java.util.Scanner;
import java.util.Random;
import java.text.DecimalFormat;


// Represents	players	in	general
abstract public class Player {

    Monster monster;

    // all moster variables should be private
    public Monster getMonster() {
        return monster;
    }

    // returns true if player has lost
    public boolean hasLost() {
        if (monster.getHP() > 0) {
            return false;
        } else {
            return true;
        }
    }


    // Human player selects move through user input, CPU player randomly generates one
    abstract public int chooseMove();

    // Player attack another player and hp is updated
    public int attack(Player p, int moveNum) {
        int movePower = 0;
        int damageDealt = 0;
        float hitPercent = 0.0f;
        String moveName = "NULL";
        if (moveNum == 1) {
            movePower = monster.move1.getPower();
            hitPercent = monster.move1.getAccuracy();
            moveName = monster.move1.getMoveName();
        } else if (moveNum == 2) {
            movePower = monster.move2.getPower();
            hitPercent = monster.move2.getAccuracy();
            moveName = monster.move2.getMoveName();
        } else if (moveNum == 3) {
            movePower = monster.move3.getPower();
            hitPercent = monster.move3.getAccuracy();
            moveName = monster.move3.getMoveName();
        } else {
            movePower = monster.move4.getPower();
            hitPercent = monster.move4.getAccuracy();
            moveName = monster.move4.getMoveName();
        }

        System.out.println(monster.getName() + " used " + moveName + " on " + p.monster.getName());

        // Checks accuracy first before attacking
        float hitOrMiss = (float)(Math.random() * 1.0f);
        if (hitOrMiss <= hitPercent) {
           // damageDealt = monster.getAtk() + movePower - p.monster.getDef();
            damageDealt = ((22*movePower*(monster.getAtk()/p.monster.getDef()))/50)+2; // Based on real pokemon game math
            if (damageDealt <= 0) {
                damageDealt = 1;  // Everyone can do at least 1 damage minimum
            }
            p.monster.setHP((p.monster.getHP() - damageDealt));
        } else if (hitOrMiss > hitPercent) {
            System.out.println(monster.getName() + "'s attack against " + p.monster.getName() + " missed!");
        }
        return p.monster.getHP();
    }

    // Checks speed to see who attacks first
    public boolean isFasterThan(Player p) {
        if (monster.getSpeed() > p.monster.getSpeed()) {
            return true;
        } else if (monster.getSpeed() == p.monster.getSpeed()) {  // Checks for speed tie
            int speedTie = (int)(Math.random() * 2);
            if (speedTie == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
