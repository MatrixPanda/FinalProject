package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javafx.event.*;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Controller {
    @FXML
    private ListView<String> PokemonList;
    @FXML
    private TextField textField;
    ObservableList list = FXCollections.observableArrayList();

    public void initialize()  {
        loadData();
    }

    @FXML
    public void displaySelected(MouseEvent event) {

        String list = PokemonList.getSelectionModel().getSelectedItem();
        if(list ==  null || list.isEmpty()){
            textField.setText("Nothing Selected");

        }
        else{
            textField.setText((list + "is selected"));

        }
    }
    @FXML
    public void testButton(ActionEvent e) {
        textField.setText("Hey");
    }

    public void loadData(){
        //Read File to populate Listview
        String fileName = "Pokemon List.txt";

        String line = null;

        try{
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
            PokemonList.getItems().addAll(list);

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }

    }






}

