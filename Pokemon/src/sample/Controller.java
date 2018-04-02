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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class Controller {

//    @FXML
//    private TextField textField;
    @FXML
    private TextArea textArea;
    @FXML
    public TextArea gameText;

    private Player player;
    private Monster monster;
    private Move move;
    private ArrayList<Monster> pokemonObjectArrayList = new ArrayList<>();
    private ArrayList<Move> moveObjectArrayList = new ArrayList<>();
    private ArrayList<Monster> randRolledPokemonList = new ArrayList<>();

    @FXML private ListView<String> randomPokemonList;
    @FXML private ObservableList<String> items = FXCollections.observableArrayList();


    public void initialize()  {
        summonPokemons();
        randomPokemonList();
        randomPokemonList.setItems(populateList());


    }



    @FXML
    public void displaySelected(MouseEvent event) {

        String list = randomPokemonList.getSelectionModel().getSelectedItem();
        if(list ==  null || list.isEmpty()){
            //textField.setText("Nothing Selected");
            textArea.setText("Nothing Selected");
        }
        else{
            for (Monster m: randRolledPokemonList) {
                if (m.getName().equalsIgnoreCase(list)) {
                    //textField.setText((list + " selected ->  HP: " + m.getHP() + "   Attack: " + m.getAtk() + "   Defense: " + m.getDef() + "   Speed: " + m.getSpeed()));
                    textArea.setText(list + " selected ->  HP: " + m.getHP() + "   Attack: " + m.getAtk() +
                                    "   Defense: " + m.getDef() + "   Speed: " + m.getSpeed() + "\nMoves: \n"
                            + m.move1.getMoveName() + ":\t\t\tPower: " + m.move1.getPower() + " \tAccuracy: " +
                            m.move1.getAccuracy() + "\n" + m.move2.getMoveName() + ":\t\t\t\tPower: " + m.move2.getPower() +
                            " \tAccuracy: " + m.move2.getAccuracy() + "\n" + m.move3.getMoveName() + ":\t\t\tPower: " +
                            m.move3.getPower() + " \tAccuracy: " + m.move3.getAccuracy() + "\n" + m.move4.getMoveName() +
                            ":\t\t\tPower: " + m.move4.getPower() + " Accuracy: " + m.move4.getAccuracy());
                }
            }
        }
    }
    @FXML
    public void testButton(ActionEvent e) {
        startBattle();


    }



    // Give function later
    public void someButtom() {
        System.out.println("testing someButton");
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



    // Function to let the battle begin!!!
    private void startBattle() {
        player = new HumanPlayer(pokemonObjectArrayList.get(3));
        monster = new Monster("Pikachu", "Electric", 142, 102, 61, 121,
                readMove("Thunder"), readMove("Hidden Power Ice"), readMove("Fire Punch"),
                readMove("Air Slash"));

        Player enemy = new CPUPlayer(monster);

        while ((!player.hasLost()) && (!enemy.hasLost())) {
            // print both Pokemon's HP
            gameText.setText("");


            gameText.setText(player.getMonster().getName() + " has "+  player.getMonster().getHP()+"HP\n");
            gameText.setText(enemy.getMonster().getName() + " has "+  enemy.getMonster().getHP()+"HP\n");


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
            gameText.setText("You and " + player.getMonster().getName() + "have lost the battle.\n");

        } else {
            gameText.setText("You and " + player.getMonster().getName() + "are victorious!\n");
        }
    }
}

