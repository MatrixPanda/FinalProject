package sample;

import java.util.Scanner;
import java.util.Random;
import java.text.DecimalFormat;


// Selects	moves	by	asking	the	user	via	console	input
public class HumanPlayer extends Player {

    public HumanPlayer(Monster humanMonster) {
        this.monster = humanMonster;
    }

    // User inputs a move number 1-4.
    public int chooseMove() {
        Scanner in = new Scanner(System.in);
        gameText.setText("Enter 1-4 to select a move: \n");
        gameText.setText("1: " + monster.move1.getMoveName() + "  2: " + monster.move2.getMoveName());
        gameText.setText("\n3: " + monster.move3.getMoveName() + "  4: " + monster.move4.getMoveName());
        int moveSlot = in.nextInt();
        if (moveSlot > 4) {   // Checks for illegal values for move selected
            moveSlot = 4;
        } else if (moveSlot < 1) {
            moveSlot = 1;
        }
        return moveSlot;
    }

}

