package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javafx.event.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class Controller {

    @FXML
    private TextField textField;

    private Player player;
    private Monster monster;
    private Move move;
    private ArrayList<Monster> pokemonObjectArrayList = new ArrayList<>();
    private ArrayList<Move> moveObjectArrayList = new ArrayList<>();
    private ArrayList<Monster> randRolledPokemonList = new ArrayList<>();
    private String selectedPokemon;

    @FXML private ListView<String> randomPokemonList;
    @FXML private ObservableList<String> items = FXCollections.observableArrayList();

    private Socket connection;
    private String host = "localhost";
    private int port = 8888;
    private static PrintWriter pw = null;
    private static int newHp = 1;


    public void initialize()  {
        summonPokemons();
        randomPokemonList();

        randomPokemonList.setItems(populateList());
    }


    @FXML
    public void displaySelected(MouseEvent event) {

        selectedPokemon = randomPokemonList.getSelectionModel().getSelectedItem();
        if(selectedPokemon ==  null || selectedPokemon.isEmpty()){
            textField.setText("Nothing Selected");
        }
        else{
            textField.setText((selectedPokemon + " is selected"));
        }

    }


    @FXML
    public void testButton(ActionEvent e) {
       // textField.setText("Hey");
       // connectToServer();
        startBattle();
       // connectToServer();
        /*
        try {
            connection = new Socket(host, port);
            System.out.println("Searching for opponent...\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        */
    }


    // Give function later
    public void someButtom() {
        System.out.println("testing someButton");
    }


    private void connectToServer() {
        try {
            connection = new Socket(host, port);
            System.out.println("Connected to server");
            pw = new PrintWriter(connection.getOutputStream(), true);
            pw.println("ATTACK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

       // startBattle();
    private void disconnectFromServer() {
        try {
            connection.close();
            System.out.println("disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    // Send opponent their new hp value
    private void sendHp(int hp) {
        try {
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

            dos.writeUTF(String.valueOf(hp));

            dos.close();
        } catch (IOException e) {
            System.err.println("Error reading from server.");
        }
    }


    private void updateHp() {
        try {
            connection = new Socket(host, port);
            System.out.println("CONNECTED");
            pw = new PrintWriter(connection.getOutputStream(), true);
            pw.println("UPDATEhp");  // selectedf is second token in server code, passed in as file name
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DataInputStream dis = new DataInputStream(connection.getInputStream());
            String temp = dis.readUTF();
            newHp = Integer.valueOf(temp);
            System.out.println("THIS IS THE NEW UPDATED HP: " + newHp);
            dis.close();
        } catch (IOException e) {
            System.err.println("Error reading from socket.");
        }
/*
        try {
            connection.close();
            System.out.println("disconnected" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }


    // Function to let the battle begin!!!
    private void startBattle() {

        player = new HumanPlayer(readPokemon(selectedPokemon));

        monster = new Monster("Pikachu", "Electric", 242, 102, 61, 121,
                readMove("Thunder"), readMove("Hidden Power Ice"), readMove("Fire Punch"),
                readMove("Air Slash"));

        Player enemy = new CPUPlayer(monster);

        System.out.println("");
        System.out.printf("%s has %d HP\n",
                player.getMonster().getName(),
                player.getMonster().getHP());
        System.out.printf("%s has %d HP\n",
                enemy.getMonster().getName(),
                enemy.getMonster().getHP());

        int playerMove = player.chooseMove();

        int isFaster = 0;  // Checks if this pokemon is faster than opponent. 1 is true, 0 is false.
        switch(isFaster) {
            case 1:     // Attack first
                connectToServer();

                int newHp = player.attack(enemy, playerMove);
                System.out.println("NEW HP VALUE TO SEND: " + newHp);
                sendHp(newHp);
                disconnectFromServer();
                break;

            case 0:     // Get attacked first
                updateHp();
                break;
        }

    }
}
