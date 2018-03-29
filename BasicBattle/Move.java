import java.util.Scanner;
import java.util.Random;
import java.text.DecimalFormat;


// Represents	a	single	move
public class Move {

  private String name;
  private String type;
  private int power;
  private float accuracy;

	public Move(String n, String t, int p, float acc) {
    this.name = n;
    this.type = t;
    this.power = p;
    this.accuracy = acc;
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
