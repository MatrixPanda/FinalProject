package sample;

import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.Random;
import java.text.DecimalFormat;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Player player;
    private Monster monster;
    private Move move;
    private ArrayList<Monster> pokemonObjectArrayList = new ArrayList<>();
    private ArrayList<Move> moveObjectArrayList = new ArrayList<>();
    private ArrayList<Monster> randRolledPokemonList = new ArrayList<>();

    // Not needed anymore (delete later)
    private static int temp = 0;
    private static Monster[] pokemonObjectArray = new Monster[3];


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Pokemon Game");


        summonPokemons();
        randomPokemonList();

        // For debugging list, remove later
        System.out.print("Testing to see if random list is working: ");
        System.out.println("1-" + randRolledPokemonList.get(0).getName() + " 2-" + randRolledPokemonList.get(1).getName() +
                " 3-" + randRolledPokemonList.get(2).getName());

        startBattle();


        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    // returns the correct Move object based on the name, returns null if doesn't exist.
    private Move readMove(String moveName) {
        for (Move move : moveObjectArrayList) {
            if (move.getMoveName().equalsIgnoreCase(moveName)) {
                return move;
            }
        }
        // Default move and in case if move spelling is wrong, insert first move by default so program doesn't CRASH!!!
        return moveObjectArrayList.get(0);
    }


    // Pick random pokemons to chose from
    private void randomPokemonList() {
        Random rand = new Random();
        ArrayList<Integer> alreadyRolled = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            int n = rand.nextInt(pokemonObjectArrayList.size());

            while (alreadyRolled.contains(n)) {
                n = rand.nextInt(pokemonObjectArrayList.size());
            }
            alreadyRolled.add(n);
            randRolledPokemonList.add(pokemonObjectArrayList.get(n));
        }
    }


    // Reads move file to create move objects and store it in moveObjectArrayList
    // then Reads move file to create pokemon objects and store it in pokemonObjectArrayList
    private void summonPokemons() {
        // Read move file to create move objects and store it in moveObjectArrayList
        try {
            // Read stats from file
            String inFile = "Move List.txt";
            String delimiter = ",";

            BufferedReader br = new BufferedReader(new FileReader(inFile));
            List<String> lineList = br.lines().map(Object::toString).collect(Collectors.toList());

            br.close();

            for (String line :  lineList) {
                String[] data = line.split(delimiter);

                for (int i=0; i<data.length; i++) {
                    String name = data[0];
                    String type = data[1];
                    int power = Integer.valueOf(data[2]);
                    float accuracy = Integer.valueOf(data[3]);

                    move = new Move(name, type, power, accuracy);
                }
                moveObjectArrayList.add(move);
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while reading the file");
            e.printStackTrace();
        }

        // Read move file to create pokemon objects and store it in pokemonObjectArrayList
        try {
            // Read stats from file
            String inFile = "Pokemon List.txt";
            String delimiter = ",";

            BufferedReader br = new BufferedReader(new FileReader(inFile));
            List<String> lineList = br.lines().map(Object::toString).collect(Collectors.toList());

            br.close();

            for (String line :  lineList) {
                String[] data = line.split(delimiter);

                for (int i=0; i<data.length; i++) {
                    String name = data[0];
                    String type = data[1];
                    int hp = Integer.valueOf(data[2]);
                    int attack = Integer.valueOf(data[3]);
                    int defense = Integer.valueOf(data[4]);
                    int speed = Integer.valueOf(data[5]);
                    String move1 = data[6];
                    String move2 = data[7];
                    String move3 = data[8];
                    String move4 = data[9];

                    monster = new Monster(name, type, hp, attack, defense, speed,
                            readMove(move1), readMove(move2), readMove(move3),
                            readMove(move4));
                }
                pokemonObjectArrayList.add(monster);
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while reading the file");
            e.printStackTrace();
        }
    }


    // Function to let the battle begin!!!
    private void startBattle() {
        player = new HumanPlayer(pokemonObjectArrayList.get(3));  // Pass in object selected from list UI later.

        monster = new Monster("Pikachu", "Electric", 142, 102, 61, 121,
                readMove("Thunder"), readMove("Hidden Power Ice"), readMove("Fire Punch"),
                readMove("Air Slash"));

        Player enemy = new CPUPlayer(monster);

        while ((!player.hasLost()) && (!enemy.hasLost())) {
            // print both Pokemon's HP
            System.out.println("");
            System.out.printf("%s has %d HP\n",
                    player.getMonster().getName(),
                    player.getMonster().getHP());
            System.out.printf("%s has %d HP\n",
                    enemy.getMonster().getName(),
                    enemy.getMonster().getHP());

            // decide the next move
            int playerMove = player.chooseMove();
            int enemyMove = enemy.chooseMove();

            // execute the next move
            if (player.isFasterThan(enemy)) {
                player.attack(enemy, playerMove);
                if (!enemy.hasLost()) {
                    enemy.attack(player, enemyMove);
                }
            } else {
                enemy.attack(player, enemyMove);
                if (!player.hasLost()) {
                    player.attack(enemy, playerMove);
                }
            }
        }

        if (player.hasLost()) {
            System.out.printf("You and %s have lost the battle.\n",
                    player.getMonster().getName());
        } else {
            System.out.printf("You and %s are victorious!\n",
                    player.getMonster().getName());
        }
    }



    public static void main(String[] args) {
        // Self Notes for later
        // Store monsters all into a list, the select the index for monster, OR type in name and search for the name in index and return?
        // If pokemon's move type == self type, damage = 1.5x, if received dmg type == resisted.exist...else if doubled.exist...
        // Use arraylist, can pass into array and convert to array later if needed.
        // use .ignoreCase
        // Add Status Moves later?? Moves usage count limited???

        launch(args);
    }
}
