package domain;

import dao.FoodDao;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides methods to easily create, read, update and
 * delete entries from database.
 */
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
    
    /**
     * Gets a finnish abbreviation for the day of the week.
     * Example: 2019-04-24 will return "Ke"
     * @param date the date to be used
     * @return two-letter day of the week
     */
    public String getDayOfWeekName(LocalDate date) {
        if (date == null) return "NotADate";
        
        String[] dayOfWeeks = new String[]{"Ma", "Ti", "Ke", "To", "Pe", "La", "Su"};
        return dayOfWeeks[date.getDayOfWeek().getValue() - 1];
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    /**
     * Returns Monday of the week by using previously set date.
     * @return Monday of the week
     */
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
    
    /**
     * Saves the food with provided amount to the database. 
     * Date will be set to the current date.
     * @param food food that will be saved
     * @param amount amount of food in grams
     */
    public void saveWithCurrentDate(Food food, int amount) {
        save(food, LocalDate.now(), amount);  
    }
    
    /**
     * Saves the food with provided amount to the database by date.
     * @param food food that will be saved
     * @param date date
     * @param amount amount of food in grams
     */
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
    
    /**
     * Removes the meal from a specific date from the database.
     * @param food food that will be removed
     * @param date date when the food was eaten
     */
    public void removeEntry(Food food, LocalDate date) {
        try {
            this.foodDao.removeEntry(food, date);
        } catch (SQLException ex) {
            System.out.println("Error when tried to remove entry");
        }
        
        System.out.println("REMOVED " + food.getName());
    }
    /**
     * Returns a list of foods from a specific date
     * @param date date which will be used to get the foods
     * @return list of foods
     */
    public List<Food> getFoodListByDate(LocalDate date) {
        List<Food> foodList = new ArrayList<>();
        
        try {
            foodList = this.foodDao.getFoodListByDate(date);
            
        } catch (SQLException ex) {
            System.out.println("Error when tried to get list of foods by date");
        }
        
        return foodList;
    }
    
    /**
     * Sums all the calories from a specific date
     * @param date date which will be used to get the foods
     * @return rounded value of calories
     */
    public long getTotalCaloriesByDate(LocalDate date) {
        List<Food> foodList = getFoodListByDate(date);
        double sum = 0;
        for (Food food : foodList) {
            sum += food.getNutrient("energyKcal");
        }
        return Math.round(sum);
    }
    /**
     * Sums all the nutrients from a specific date. Results will be
     * saved in a map (ex. "carbohydrate" is mapped to 432.4)
     * @param date date which will be used to get the nutrients
     * @return A map with summary of nutrients
     */
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
