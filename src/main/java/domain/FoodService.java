package domain;

import dao.FoodDao;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoodService {
    
    private FoodDao foodDao;

    public FoodService(FoodDao foodDao) throws SQLException {
        this.foodDao = foodDao;
        this.foodDao.initDatabase();
    }
    
    public void saveWithCurrentDate(Food food, int amount) {
        save(food, LocalDate.now(), amount);
        
    }
    
    public void save(Food food, LocalDate date, int amount) {
        try {
            int foodId = this.foodDao.saveFood(food);
            int dateId = this.foodDao.saveDate(date);
            this.foodDao.saveFoodDate(foodId, dateId, amount);
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
    
}
