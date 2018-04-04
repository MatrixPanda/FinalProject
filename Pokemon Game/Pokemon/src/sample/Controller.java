package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.event.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Controller  {

    @FXML
    private TextField textFieldTimer;
    @FXML
    private TextField textFieldPlayerPoke;
    @FXML
    private TextField textFieldEnemyPoke;
    @FXML
    private TextField textFieldSelfHp;
    @FXML
    private TextField textFieldEnemyHp;
    @FXML
    private TextArea textArea;
    @FXML
    public TextArea gameText;
    @FXML
    public TextArea gameText2;


    private Player player;
    private Monster monster;
    private Move move;
    private ArrayList<Monster> pokemonObjectArrayList = new ArrayList<>();
    private ArrayList<Move> moveObjectArrayList = new ArrayList<>();
    private ArrayList<Monster> randRolledPokemonList = new ArrayList<>();
    private String selectedPokemon = "Tyranitar";

    @FXML private ListView<String> randomPokemonList;
    @FXML private ObservableList<String> items = FXCollections.observableArrayList();

    private Socket connection;
    private String host = "localhost";
    private int port = 8888;
    private static PrintWriter pw = null;

    private static int turn = 1;
    private static int pokemonsKilled = 0;
    private TimeThread timer;
    private StartGame battle;
    private DisplayTimer displayT;
    private ResetLists resetLists;



    public void initialize()  {
        summonPokemons();
        pickRandomPokemons();
        randomPokemonList.setItems(populateList());
    }


    // Need this to be multithreaded, otherwise if u chose an ability while the timer counts +1 at the SAME time, your..
    //...your command won't do anything, you will have to click again (or again...)
    public class StartGame extends Thread {
        Monster pokemon;
        public StartGame(Monster m) {
            this.pokemon = m;
        }
        public void run() {
            try {
                startBattle(pokemon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class DisplayTimer extends Thread {
        public DisplayTimer() {
        }
        public void run() {
            try {

                textFieldTimer.appendText(String.valueOf(TimeThread.setInterval()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // Unused for now
    public class ResetLists extends Thread {
        public ResetLists() {
        }
        public void run() {
            try {
                randRolledPokemonList.clear();
                summonPokemons();
                randomPokemonList.getItems().clear();
                pickRandomPokemons();
                randomPokemonList.setItems(populateList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void displaySelected(MouseEvent event) {

        selectedPokemon = randomPokemonList.getSelectionModel().getSelectedItem();
        if(selectedPokemon ==  null || selectedPokemon.isEmpty()){
            //textField.setText("Nothing Selected");
            textArea.setText("Nothing Selected");
        }
        else{
            for (Monster m: randRolledPokemonList) {
                if (m.getName().equalsIgnoreCase(selectedPokemon)) {
                    textArea.setText(selectedPokemon + " selected ->  HP: " + m.getHP() + "   Attack: " + m.getAtk() +
                            "   Defense: " + m.getDef() + "   Speed: " + m.getSpeed() + "\nMoves: \n"
                            + m.move1.getMoveName() + ",   " + m.move2.getMoveName() + ",   " + m.move3.getMoveName() +
                            ",   " +  m.move4.getMoveName());
                }
            }
        }
    }


    @FXML
    public void testButton(ActionEvent e) {
        turn = 1;
        pokemonsKilled = 0;
        timer = new TimeThread(0);
        timer.start();

        displayT = new DisplayTimer();
        displayT.start();

        battle = new StartGame(readPokemon(selectedPokemon));
        battle.start();
       // timer.interrupt();
       // startBattle(readPokemon(selectedPokemon));
    }


    // Give function later
    public void someButtom() {
        System.out.println("testing someButton");
    }


    @FXML
    public void moveButton1(ActionEvent e) {
        System.out.println("1");
        Scanner in = new Scanner(System.in);
        HumanPlayer.inputMove = 1;
    }

    public void moveButton2(ActionEvent e) {
        System.out.println("2");
        HumanPlayer.inputMove = 2;
    }

    public void moveButton3(ActionEvent e) {
        System.out.println("3");
        HumanPlayer.inputMove = 3;
    }

    public void moveButton4(ActionEvent e) {
        System.out.println("4");
        HumanPlayer.inputMove = 4;
    }

    public void surrenderButton(ActionEvent e) {
        System.exit(0);
    }



    private void connectAndSend(int killCount, int turnCount, int timeCount) {
        try {
            connection = new Socket(host, port);
            System.out.println("Connected to server");
            pw = new PrintWriter(connection.getOutputStream(), true);
            pw.println("SEND");
            String sendInfo = String.valueOf("Kill Streak: " + killCount + "   Turns Used: " + turnCount +
                                             "   Time Spent: " + timeCount + "sec");
            // Send away!
            try {
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

                dos.writeUTF(sendInfo);

                dos.close();
            } catch (IOException e) {
                System.err.println("Error reading from server.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Disconnect when done sending
        try {
            connection.close();
            System.out.println("disconnected" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void disconnectFromServer() {
        try {
            connection.close();
            System.out.println("disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Pick 3 random pokemons and store them in an ArrayList
    private void pickRandomPokemons() {
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



    // Populate ListView
    private ObservableList<String> populateList() {
        for (Monster pokemon : randRolledPokemonList) {
            items.add(pokemon.getName());
        }
        return items;
    }



    // returns the correct Move object based on the name, returns first move if doesn't exist or typo entry.
    private Monster readPokemon(String pokemonName) {
        for (Monster monster : pokemonObjectArrayList) {
            if (monster.getName().equalsIgnoreCase(pokemonName)) {
                return monster;
            }
        }
        // Default pokemon
        return pokemonObjectArrayList.get(0);
    }



    // returns the correct Move object based on the name, returns first move if doesn't exist or typo entry.
    private Move readMove(String moveName) {
        for (Move move : moveObjectArrayList) {
            if (move.getMoveName().equalsIgnoreCase(moveName)) {
                return move;
            }
        }
        // Default move and in case if move spelling is wrong, insert first move by default so program doesn't CRASH!!!
        return moveObjectArrayList.get(0);
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
    private void startBattle(Monster pokemon) {
        //  player = new HumanPlayer(pokemonObjectArrayList.get(3));  // Pass in object selected from list UI later.
        player = new HumanPlayer(pokemon);
        int rememberStartHp = player.getMonster().getHP();  // remember the original hp

         // Re-roll random opponent each time. Clear old rand list first
         randRolledPokemonList.clear();
         pickRandomPokemons();
         Player enemy = new CPUPlayer(randRolledPokemonList.get(0));



         // Begin Fighting
        while ((!player.hasLost()) && (!enemy.hasLost())) {
           // System.out.println("Turn: " + turn);
            gameText2.clear();
            textFieldPlayerPoke.clear();  // clear before add
            textFieldEnemyPoke.clear();
            textFieldSelfHp.clear();
            textFieldEnemyHp.clear();

            gameText2.appendText(selectedPokemon + "  ->  HP: " + pokemon.getHP() + "   Attack: " + pokemon.getAtk() +
                    "   Defense: " + pokemon.getDef() + "   Speed: " + pokemon.getSpeed() + "\nMoves: \n"
                    + pokemon.move1.getMoveName() + ":\t\t\tPower: " + pokemon.move1.getPower() + " \tAccuracy: " +
                    pokemon.move1.getAccuracy() + "\n" + pokemon.move2.getMoveName() + ":\t\t\t\tPower: " + pokemon.move2.getPower() +
                    " \tAccuracy: " + pokemon.move2.getAccuracy() + "\n" + pokemon.move3.getMoveName() + ":\t\t\tPower: " +
                    pokemon.move3.getPower() + " \tAccuracy: " + pokemon.move3.getAccuracy() + "\n" + pokemon.move4.getMoveName() +
                    ":\t\t\tPower: " + pokemon.move4.getPower() + " Accuracy: " + pokemon.move4.getAccuracy());

            textFieldPlayerPoke.appendText(player.getMonster().getName());
            textFieldEnemyPoke.appendText(enemy.getMonster().getName());
            textFieldSelfHp.appendText(String.valueOf(player.getMonster().getHP()));
            textFieldEnemyHp.appendText(String.valueOf(enemy.getMonster().getHP()));

            // Print in game info
            gameText.appendText("Turn: " + turn + "\n");
            gameText.appendText(player.getMonster().getName()  + " has "+  player.getMonster().getHP()+"HP\n");
            gameText.appendText(enemy.getMonster().getName() + " has "+  enemy.getMonster().getHP()+"HP\n");
            gameText.appendText("1: " + player.getMonster().move1.getMoveName() + "   2: "  +  player.getMonster().move2.getMoveName() + "\n");
            gameText.appendText("3: " + player.getMonster().move3.getMoveName() + "   4: "  +  player.getMonster().move4.getMoveName() + "\n");
            gameText.appendText("\n");

          //  System.out.println("1: " + player.getMonster().move1.getMoveName() + "   2: " + player.getMonster().move2.getMoveName());
          //  System.out.println("2: " + player.getMonster().move3.getMoveName() + "   3: " + player.getMonster().move3.getMoveName());

            // decide the next move
            while (HumanPlayer.inputMove == 0) {
                Scanner in = new Scanner(System.in);
            }
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
            turn++;
            textFieldSelfHp.clear();  // clear before add
            textFieldEnemyHp.clear();
            textFieldSelfHp.appendText(String.valueOf(player.getMonster().getHP()));
            textFieldEnemyHp.appendText(String.valueOf(enemy.getMonster().getHP()));

            // If enemy dies first, player moves onto next round and its hp is increased by 20, but cannot be more than...
            // ...its max hp
            if (enemy.hasLost()) {
                System.out.println("PLAYERS OLD HP: " + player.getMonster().getHP());
                int temp = player.getMonster().getHP();
                temp += 100;
                if (temp > rememberStartHp) {
                    temp = rememberStartHp;
                    player.getMonster().setHP(temp);
                    pokemonsKilled++;
                    startBattle(player.getMonster());
                   // battle = new StartGame(readPokemon(selectedPokemon));
                   // battle.start();
                } else {
                    player.getMonster().setHP(temp);
                    System.out.println("PLAYERS NEW HP: " + player.getMonster().getHP());
                    pokemonsKilled++;
                   // battle = new StartGame(readPokemon(selectedPokemon));
                   // battle.start();
                    startBattle(player.getMonster());
                }
            }
        }
        System.out.println("SENDING SCORE TO SERVER (commented out for now)...");
        connectAndSend(pokemonsKilled, turn, TimeThread.setInterval());   // use TimeThread.setInterval()
        /*
        if (player.hasLost()) {
            System.out.println("GAME OVER!");
        } else {
            System.out.println("Victorious!");
        }
        */

        // When game is over, first clear all lists then re-summon pokemons to reset their stats.
        System.out.println("GAME OVER! Connect to server to see your score and other players score. ");

        /*
        randRolledPokemonList.clear();
        summonPokemons();
        randomPokemonList.getItems().clear();
        pickRandomPokemons();
        randomPokemonList.setItems(populateList());
        */
       // resetLists = new ResetLists();
       // resetLists.start();
       // resetLists.stop();
    }
}

