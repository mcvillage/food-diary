package ui;

import api.Fineli;
import domain.Food;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FoodDiaryUI extends Application {
    
    private Scene searchScene;
    
    @Override
    public void start(Stage primaryStage) {
        // search
        
        VBox searchPane = new VBox(10);
        Label header = new Label("Hae ruokia:");
        HBox inputPane = new HBox(10);
        searchPane.setAlignment(Pos.CENTER);
        inputPane.setAlignment(Pos.CENTER);
        
        TextField searchInput = new TextField();
        Button searchButton = new Button("Hae");
        inputPane.getChildren().addAll(searchInput, searchButton);
        
        ListView<Food> results = new ListView<>();
        results.setCellFactory(param -> new ListCell<Food>() {

            @Override
            protected void updateItem(Food item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        
        
        searchPane.getChildren().addAll(header, inputPane, results);
        searchScene = new Scene(searchPane, 500, 300);
        
        searchButton.setOnAction(e -> {
            results.setItems(search(searchInput.getText()));       
        });
        
        searchInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                results.setItems(search(searchInput.getText()));
            }
        });
        
        results.setOnMouseClicked(e -> {
            Food food = results.getSelectionModel().getSelectedItem();
            System.out.println(food);
        });
        
        
        // setup primary stage
        
        primaryStage.setTitle("Ruokapäiväkirja");
        primaryStage.setScene(searchScene);
        primaryStage.show();
        
    }
    
    private static ObservableList<Food> search(String keyword) {
        List<Food> foodList = new ArrayList<>();
        try {
            foodList = Fineli.search(keyword);
        } catch (IOException ex) {
            System.out.println("Error in Fineli search");
            ex.printStackTrace();
        }
        
        ObservableList<Food> observableFoodList = FXCollections.observableArrayList(foodList);
        return observableFoodList;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
