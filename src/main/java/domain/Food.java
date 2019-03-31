package domain;

import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public class Food {
    
    private int id;
    private String name;   
    private HashMap<String, Double> nutrients;

    public Food(int id, String name) {
        this.id = id;
        this.name = name;
        this.nutrients = new HashMap<>();
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public double getNutrient(String name) {
        return this.nutrients.getOrDefault(name, Double.NaN);
    }
    
    public void setNutrient(String name, double amount) {
        this.nutrients.put(name, amount);
    }
    
    public ObservableList<PieChart.Data> getBasicNutrients() {
        // Energy percent
        double energy = getNutrient("energy");
        double fat = getNutrient("fat") * 37 / energy * 100;
        double protein = getNutrient("protein") * 17 / energy * 100;
        double carbohydrate = getNutrient("carbohydrate") * 17 / energy * 100;
        double alcohol = getNutrient("alcohol") * 29 / energy * 100;
        double other = 100.0 - fat - protein - carbohydrate - alcohol;
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Rasva", fat),
                new PieChart.Data("Proteiini", protein),
                new PieChart.Data("Hiilihydraatti imeytyvä", carbohydrate),
                new PieChart.Data("Alkoholi", alcohol),
                new PieChart.Data("Muu", other)
        );
        
        return pieChartData;
    }
    
}