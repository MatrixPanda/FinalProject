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
		System.out.print("Enter 1-4 to select a move: ");
		int moveSlot = in.nextInt();
		if (moveSlot > 4) {   // Checks for illegal values for move selected
			moveSlot = 4;
		} else if (moveSlot < 1) {
			moveSlot = 1;
		}
    return moveSlot;
  }

}
