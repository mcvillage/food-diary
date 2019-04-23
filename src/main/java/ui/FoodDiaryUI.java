package ui;

import api.Fineli;
import domain.Food;
import domain.FoodService;
import dao.FoodDao;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class FoodDiaryUI extends Application {
    
    private FoodService foodService;

    private PieChart nutrientData;
    private Label weekLabel;
    private HBox weekDayBox;
    private BarChart<String, Number> barChart;
    private ListView<Food> foodsOfTheDay;
    
    private Stage primaryStage;
    private Scene searchScene;
    private Scene nutrientScene;
    private Scene foodDiaryScene;
    private Scene foodDiaryDayScene;
    
    private int[] dimensions;
    
    @Override
    public void init() throws SQLException {
        FoodDao foodDao = new FoodDao("food.db");
        this.foodService = new FoodService(foodDao);
        this.dimensions = new int[] {750, 450};
        this.weekDayBox = new HBox(1);
        this.weekDayBox.setAlignment(Pos.CENTER);
        createWeekDayBox(foodService.getFirstDayOfWeek());
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
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
            foodService.setFood(results.getSelectionModel().getSelectedItem());
            nutrientData.setData(foodService.getFood().getBasicNutrients());
            nutrientData.setTitle("Energiansaanti:\n" + foodService.getFood().getName());
            primaryStage.setScene(nutrientScene);
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
        searchScene = new Scene(searchAlignmentPane, dimensions[0], dimensions[1]);
        
        
        // Nutrient Scene
        
        BorderPane nutrientAlignmentPane = new BorderPane();
        
        VBox nutrientPane = new VBox(10);
        nutrientPane.setAlignment(Pos.CENTER);
        nutrientData = new PieChart();
        nutrientData.setLegendVisible(false);
        
        Label foodAdditionLabel = new Label("Ruoan paino (g)");
        HBox foodAddition = new HBox(10);
        foodAddition.setAlignment(Pos.CENTER);
        
        TextField foodAmountField = new TextField();
        foodAmountField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                foodAmountField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        Button addFoodButton = new Button("Lisää");
        addFoodButton.setOnAction(e -> {
            if (foodAmountField.getText().isEmpty()) return;
            foodService.saveWithCurrentDate(foodService.getFood(), Integer.parseInt(foodAmountField.getText()));
            foodAmountField.clear();
        });
        foodAddition.getChildren().addAll(foodAmountField, addFoodButton);
        
        nutrientPane.getChildren().addAll(nutrientData, foodAdditionLabel, foodAddition); 
        nutrientAlignmentPane.setTop(createMenu(primaryStage, true, true));
        nutrientAlignmentPane.setCenter(nutrientPane);
        nutrientScene = new Scene(nutrientAlignmentPane, dimensions[0], dimensions[1]);
        
        
        // Food Diary Scene
        
        BorderPane foodDiaryAlignmentPane = new BorderPane();
        VBox foodDiaryPane = new VBox(10);
        foodDiaryPane.setAlignment(Pos.CENTER);
        HBox weekNavigationBox = new HBox(10);
        weekNavigationBox.setAlignment(Pos.CENTER);
        
        foodDiaryPane.getChildren().addAll(weekNavigationBox, weekDayBox);
        
        LocalDate firstDayOfWeek = foodService.getFirstDayOfWeek();
        weekLabel = new Label(firstDayOfWeek + " - " + firstDayOfWeek.plusDays(6));
        weekLabel.setStyle("-fx-font-weight: bold");
        
        Button leftArrow = new Button("<");
        leftArrow.setStyle("-fx-font-weight: bold");
        leftArrow.setOnAction(e -> {
            foodService.setDate(foodService.getDate().minusWeeks(1));
            LocalDate tempDate = foodService.getFirstDayOfWeek();
            weekLabel.setText(tempDate + " - " + tempDate.plusDays(6));
            
            createWeekDayBox(tempDate);
        });
        
        Button rightArrow = new Button(">");
        rightArrow.setStyle("-fx-font-weight: bold");
        rightArrow.setOnAction(e -> {
            foodService.setDate(foodService.getDate().plusWeeks(1));
            LocalDate tempDate = foodService.getFirstDayOfWeek();
            weekLabel.setText(tempDate + " - " + tempDate.plusDays(6));
            
            createWeekDayBox(tempDate);
        });
        
        weekNavigationBox.getChildren().addAll(leftArrow, weekLabel, rightArrow);
        weekNavigationBox.setPadding(new Insets(0, 0, 40, 0));
        
        foodDiaryAlignmentPane.setTop(createMenu(primaryStage, true, false));
        foodDiaryAlignmentPane.setCenter(foodDiaryPane);
        foodDiaryScene = new Scene(foodDiaryAlignmentPane, dimensions[0], dimensions[1]);
        
        // Food Diary Day Scene
        
        BorderPane foodDiaryDayAlignmentPane = new BorderPane();
        VBox foodDiaryDayPane = new VBox(10);
        foodDiaryDayPane.setAlignment(Pos.CENTER);
        
        HBox foodDiaryDayInfo = new HBox(10);
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<String, Number>(xAxis, yAxis);
        barChart.setTitle("Ravintoaineiden yhteenveto");
        xAxis.setLabel("Ravintoaine");
        yAxis.setLabel("Määrä (g)");
        
        foodsOfTheDay = new ListView<>();
        foodsOfTheDay.setCellFactory(param -> new XCell(foodService, barChart, foodsOfTheDay));
        
        foodDiaryDayInfo.getChildren().addAll(barChart, foodsOfTheDay);
        foodDiaryDayPane.getChildren().addAll(foodDiaryDayInfo);
        
        foodDiaryDayAlignmentPane.setTop(createMenu(primaryStage, true, true));
        foodDiaryDayAlignmentPane.setCenter(foodDiaryDayPane);
        foodDiaryDayScene = new Scene(foodDiaryDayAlignmentPane, dimensions[0], dimensions[1]);
        
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
                createWeekDayBox(foodService.getFirstDayOfWeek());
                primaryStage.setScene(foodDiaryScene);
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
    
    public void createWeekDayBox(LocalDate firstDayOfWeek) {
        weekDayBox.getChildren().clear();
        for (int i = 0; i < 7; i++) {
            weekDayBox.getChildren().add(createWeekDay(firstDayOfWeek.plusDays(i)));
        }
    }
    
    public StackPane createWeekDay(LocalDate date) {
        StackPane weekDayPane = new StackPane();
        weekDayPane.setPickOnBounds(false);
        
        VBox dayBlockText = new VBox();
        dayBlockText.setAlignment(Pos.CENTER);
        
        Label weekDay = new Label(foodService.getDayOfWeekName(date));
        weekDay.setStyle("-fx-font-weight: bold");
        weekDay.setTextFill(Color.WHITE);
        
        Label dayAndMonth = new Label(date.getDayOfMonth() + "." + date.getMonthValue() + ".");
        dayAndMonth.setTextFill(Color.WHITE);
        dayAndMonth.setPadding(new Insets(0, 0, 20, 0));
        
        Label calories = new Label(foodService.getTotalCaloriesByDate(date) + " kcal");
        calories.setTextFill(Color.WHITE);
        
        dayBlockText.getChildren().addAll(weekDay, dayAndMonth, calories);
        
        Rectangle weekDayBackgroundBlock = new Rectangle(70, 120);
        weekDayBackgroundBlock.setFill(Color.web("#2665b7"));
        if (date.equals(LocalDate.now())) {
            weekDayBackgroundBlock.setStroke(Color.BLACK);
            weekDayBackgroundBlock.setStrokeWidth(3);
        }
        
        Rectangle mask = new Rectangle(80, 120);
        mask.setFill(Color.TRANSPARENT);
        
        mask.setOnMouseClicked(e -> {
            foodService.setDate(date);
            updateBarChart();
            primaryStage.setScene(foodDiaryDayScene);
        });
        
        weekDayPane.getChildren().addAll(weekDayBackgroundBlock, dayBlockText, mask);
        return weekDayPane;
    }
    
    public void updateBarChart() {
        Map<String, Double> foodDataDay = foodService.getNutrientsByDate(foodService.getDate());
        XYChart.Series series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data("hiilihydraatti", foodDataDay.getOrDefault("carbohydrate", 0.0)));
        series1.getData().add(new XYChart.Data("sokeri", foodDataDay.getOrDefault("sugar", 0.0)));
        series1.getData().add(new XYChart.Data("proteiini", foodDataDay.getOrDefault("protein", 0.0)));
        series1.getData().add(new XYChart.Data("rasva", foodDataDay.getOrDefault("fat", 0.0)));
        series1.getData().add(new XYChart.Data("tyydyttyneet rasvahapot", foodDataDay.getOrDefault("saturatedFat", 0.0)));
        series1.getData().add(new XYChart.Data("suola", foodDataDay.getOrDefault("salt", 0.0)));
        series1.getData().add(new XYChart.Data("kuitu", foodDataDay.getOrDefault("fiber", 0.0)));
        series1.getData().add(new XYChart.Data("orgaaniset hapot", foodDataDay.getOrDefault("organicAcids", 0.0)));
        series1.getData().add(new XYChart.Data("sokerialkoholi", foodDataDay.getOrDefault("sugarAlcohol", 0.0)));
        series1.getData().add(new XYChart.Data("alkoholi", foodDataDay.getOrDefault("alcohol", 0.0)));

        barChart.getData().clear();
        barChart.getData().add(series1);
        barChart.setTitle("Ravintoaineiden yhteenveto " + foodService.getDate().toString());

        foodsOfTheDay.setItems(FXCollections.observableArrayList(foodService.getFoodListByDate(foodService.getDate())));
    }
    
    static class XCell extends ListCell<Food> {

        HBox hbox = new HBox(5);
        Label label = new Label("");
        Pane pane = new Pane();
        Button button = new Button("Poista");
        
        private FoodService foodService;
        private BarChart barChart;
        private ListView<Food> foodsOfTheDay;

        public XCell(FoodService foodService, BarChart barChart, ListView<Food> foodsOfTheDay) {
            super();
            this.foodService = foodService;
            this.barChart = barChart;
            this.foodsOfTheDay = foodsOfTheDay;
            
            hbox.getChildren().addAll(button, label, pane);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(e -> {
                foodService.removeEntry(getItem(), foodService.getDate());
                getListView().getItems().remove(getItem());
                updateBarChart();
                
            });
        }

        @Override
        protected void updateItem(Food food, boolean empty) {
            super.updateItem(food, empty);
            setText(null);
            setGraphic(null);

            if (food != null && !empty) {
                label.setText(food.getName());
                setGraphic(hbox);
            }
        }
        
        public void updateBarChart() {
            Map<String, Double> foodDataDay = foodService.getNutrientsByDate(foodService.getDate());
            XYChart.Series series1 = new XYChart.Series();
            series1.getData().add(new XYChart.Data("hiilihydraatti", foodDataDay.getOrDefault("carbohydrate", 0.0)));
            series1.getData().add(new XYChart.Data("sokeri", foodDataDay.getOrDefault("sugar", 0.0)));
            series1.getData().add(new XYChart.Data("proteiini", foodDataDay.getOrDefault("protein", 0.0)));
            series1.getData().add(new XYChart.Data("rasva", foodDataDay.getOrDefault("fat", 0.0)));
            series1.getData().add(new XYChart.Data("tyydyttyneet rasvahapot", foodDataDay.getOrDefault("saturatedFat", 0.0)));
            series1.getData().add(new XYChart.Data("suola", foodDataDay.getOrDefault("salt", 0.0)));
            series1.getData().add(new XYChart.Data("kuitu", foodDataDay.getOrDefault("fiber", 0.0)));
            series1.getData().add(new XYChart.Data("orgaaniset hapot", foodDataDay.getOrDefault("organicAcids", 0.0)));
            series1.getData().add(new XYChart.Data("sokerialkoholi", foodDataDay.getOrDefault("sugarAlcohol", 0.0)));
            series1.getData().add(new XYChart.Data("alkoholi", foodDataDay.getOrDefault("alcohol", 0.0)));

            barChart.getData().clear();
            barChart.getData().add(series1);
            barChart.setTitle("Ravintoaineiden yhteenveto " + foodService.getDate().toString());

            foodsOfTheDay.setItems(FXCollections.observableArrayList(foodService.getFoodListByDate(foodService.getDate())));
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
