package domain;

import dao.FoodDao;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FoodService {
    
    private FoodDao foodDao;
    private Food food;
    private LocalDate date;

    public FoodService(FoodDao foodDao) throws SQLException {
        this.foodDao = foodDao;
        this.foodDao.initDatabase();
        this.food = new Food(-1, "default");
        this.date = LocalDate.now();
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public String getDayOfWeekName(LocalDate date) {
        
        switch (date.getDayOfWeek().getValue()) {
            case 1:
                return "Ma";
            case 2:
                return "Ti";
            case 3:
                return "Ke";
            case 4:
                return "To";
            case 5:
                return "Pe";
            case 6:
                return "La";
            default:
                return "Su";
        }
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public LocalDate getFirstDayOfWeek() {
        if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
            return date;
        }
        
        LocalDate tempDate = date.plusDays(0);
        while (tempDate.minusDays(1).getDayOfWeek() != DayOfWeek.SUNDAY) {
            tempDate = tempDate.minusDays(1);
        }
        
        return tempDate;
    }
    
    public void saveWithCurrentDate(Food food, int amount) {
        save(food, LocalDate.now(), amount);  
    }
    
    public void save(Food food, LocalDate date, int amount) {
        try {
            int foodId = this.foodDao.saveFood(food);
            int dateId = this.foodDao.saveDate(date);
            this.foodDao.saveFoodDate(foodId, dateId, amount);
            System.out.println("Food [" + foodId + "], Date [" + dateId + "] successfully saved to database");
        } catch (SQLException ex) {
            System.out.println("Error when tried to save food");
        }
    }
    
    public void removeEntry(Food food, LocalDate date) {
        try {
            this.foodDao.removeEntry(food, date);
        } catch (SQLException ex) {
            System.out.println("Error when tried to remove entry");
        }
        
        System.out.println("REMOVED " + food.getName());
    }
    
    public List<Food> getFoodListByDate(LocalDate date) {
        List<Food> foodList = new ArrayList<>();
        
        try {
            foodList = this.foodDao.getFoodListByDate(date);
            
        } catch (SQLException ex) {
            System.out.println("Error when tried to get list of foods by date");
        }
        
        return foodList;
    }
    
    public long getTotalCaloriesByDate(LocalDate date) {
        List<Food> foodList = getFoodListByDate(date);
        double sum = 0;
        for (Food food : foodList) {
            sum += food.getNutrient("energyKcal");
        }
        return Math.round(sum);
    }
    
    public Map<String, Double> getNutrientsByDate(LocalDate date) {
        List<Food> foodList = getFoodListByDate(date);
        Map<String, Double> foodMap = new HashMap<>();
        
        for (Food food : foodList) {
            addNutrientToFoodMap(foodMap, food, "carbohydrate");
            addNutrientToFoodMap(foodMap, food, "alcohol");
            addNutrientToFoodMap(foodMap, food, "organicAcids");
            addNutrientToFoodMap(foodMap, food, "sugarAlcohol");
            addNutrientToFoodMap(foodMap, food, "saturatedFat");
            addNutrientToFoodMap(foodMap, food, "fiber");
            addNutrientToFoodMap(foodMap, food, "sugar");
            addNutrientToFoodMap(foodMap, food, "salt");
            addNutrientToFoodMap(foodMap, food, "fat");
            addNutrientToFoodMap(foodMap, food, "protein");
        }
        
        return foodMap;
    }
    
    private void addNutrientToFoodMap(Map<String, Double> foodMap, Food food, String nutrient) {
        foodMap.put(nutrient, foodMap.getOrDefault(nutrient, 0.0) + food.getNutrient(nutrient));
    }
    
}
