package domain;

import dao.FoodDao;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            ex.printStackTrace();
        }
    }
    
    public List<Food> getFoodListByDate(LocalDate date) {
        List<Food> foodList = new ArrayList<>();
        
        try {
            foodList = this.foodDao.getFoodListByDate(date);
            
        } catch (SQLException ex) {
            System.out.println("Error when tried to get list of foods by date");
            ex.printStackTrace();
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
    
}
