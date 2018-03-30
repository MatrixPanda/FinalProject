package sample;

import java.util.Scanner;
import java.util.Random;
import java.text.DecimalFormat;


// Selects	moves	by	generating	a	random	number
public class CPUPlayer extends Player {

    public CPUPlayer(Monster cpuMonster) {
        this.monster = cpuMonster;
    }

    // CPU's move is randomly generated
    public int chooseMove() {
        int random = (int )(Math.random() * 4 + 1);  // generates 0-3, adding 1 gives 1-4
        return random;
    }

}
