package domain;

import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

/**
 * A class to save all the data from the API and database.
 */
public class Food {
    
    private int id;
    private String name;   
    private HashMap<String, Double> nutrients;
    private int amount;

    public Food(int id, String name) {
        this.id = id;
        this.name = name;
        this.nutrients = new HashMap<>();
        this.amount = 100;
    }
    
    public Food(int id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.nutrients = new HashMap<>();
        this.amount = amount;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
    
    /**
     * Calculates and returns the amount of nutrient in the food in grams.
     * @param name name of the nutrient (ex. "carbohydrate")
     * @return amount of nutrient in grams
     */
    public double getNutrient(String name) {
        return this.nutrients.getOrDefault(name, 0.0) * ((double) this.amount / 100);
    }
    
    public void setNutrient(String name, double amount) {
        this.nutrients.put(name, amount);
    }
    
    /**
     * Calculates percentual values of nutrients
     * @return list of nutrients that can be set to PieChart
     */
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

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        
        if (object.getClass() != this.getClass()) {
            return false;
        }
        
        Food food = (Food) object;
        
        return food.getId() == this.id;
    }
    
    
    
}