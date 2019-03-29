package ui;

import api.Fineli;
import domain.Food;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FoodDiaryUI extends Application {
    
    private Food food;
    private Label foodName;
    
    private Scene searchScene;
    private Scene nutrientScene;
    
    @Override
    public void start(Stage primaryStage) {
        // Search Scene
        
        BorderPane searchAlignmentPane = new BorderPane();

        VBox searchPane = new VBox(10);
        searchPane.setAlignment(Pos.CENTER);
        
        Label searchHeader = new Label("Hae ruokia:");
        
        HBox inputPane = new HBox(10);
        inputPane.setAlignment(Pos.CENTER);
        
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
        results.setOnMouseClicked(e -> {
            food = results.getSelectionModel().getSelectedItem();
            foodName.setText(food.getName());
            primaryStage.setScene(nutrientScene);
            System.out.println(food);
        });
        
        TextField searchInput = new TextField();
        searchInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                results.setItems(Fineli.searchObservable(searchInput.getText()));
            }
        });
        
        Button searchButton = new Button("Hae");
        searchButton.setOnAction(e -> {
            results.setItems(Fineli.searchObservable(searchInput.getText()));
        });
        
        
        inputPane.getChildren().addAll(searchInput, searchButton);
        searchPane.getChildren().addAll(searchHeader, inputPane, results);
        
        searchAlignmentPane.setTop(createMenu(primaryStage, false, true));
        searchAlignmentPane.setCenter(searchPane);
        searchScene = new Scene(searchAlignmentPane, 600, 350);
        
        
        // Nutrient Scene
        
        BorderPane nutrientAlignmentPane = new BorderPane();
        
        VBox nutrientPane = new VBox(10);
        nutrientPane.setAlignment(Pos.CENTER);
        foodName = new Label();
        
        nutrientPane.getChildren().addAll(foodName); 
        nutrientAlignmentPane.setTop(createMenu(primaryStage, true, true));
        nutrientAlignmentPane.setCenter(nutrientPane);
        nutrientScene = new Scene(nutrientAlignmentPane, 600, 350);
        
        
        // Setup Primary Stage
        
        primaryStage.setTitle("Ruokapäiväkirja");
        primaryStage.setScene(searchScene);
        primaryStage.show();
        
    }
    
    public HBox createMenu(Stage primaryStage, boolean includeSearch, boolean includeFoodDiary) {
        HBox menu = new HBox(10);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(10));
        
        if (includeFoodDiary) {
            Button foodDiaryMenuButton = new Button("Ruokapäiväkirja");
            foodDiaryMenuButton.setOnAction(e -> {
                //primaryStage.setScene();
                System.out.println("TODO");
            });
            menu.getChildren().add(foodDiaryMenuButton);
        }
        
        if (includeSearch) {
            Button searchMenuButton = new Button("Haku");
            searchMenuButton.setOnAction(e -> {
                primaryStage.setScene(searchScene);
            });
            menu.getChildren().add(searchMenuButton);
        }
        
        return menu;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
