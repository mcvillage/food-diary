/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import dao.FoodDao;
import java.sql.SQLException;

/**
 *
 * @author valtteri
 */
public class FoodService {
    
    private FoodDao foodDao;

    public FoodService(FoodDao foodDao) throws SQLException {
        this.foodDao = foodDao;
        this.foodDao.initDatabase();
    }
    
    public void save(Food food, int amount) {
        try {
            this.foodDao.save(food, amount);
        } catch (SQLException ex) {
            System.out.println("Error when tried to save food");
            ex.printStackTrace();
        }
    }
    
}
