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
    Random rand = new Random(3);


    // Not needed anymore (delete later)
    private static int temp = 0;
    private static Monster[] pokemonObjectArray = new Monster[3];


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Pokemon Game");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
