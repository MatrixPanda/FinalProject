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

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Pokemon Game");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
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
