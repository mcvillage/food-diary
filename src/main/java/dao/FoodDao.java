package dao;

import domain.Food;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FoodDao {
    
    private String database;

    public FoodDao(String database) {
        this.database = database;
    }
    
    public void initDatabase() throws SQLException {
        Connection connection = getConnetion();
        PreparedStatement createDateTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS date ("
                + "id INTEGER PRIMARY KEY,"
                + "date DATE"
                + ");"
        );
        createDateTable.execute();
        createDateTable.close();
        
        PreparedStatement createFoodTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS food ("
                + "id INTEGER PRIMARY KEY,"
                + "carbohydrate DOUBLE,"
                + "alcohol DOUBLE,"
                + "organicAcids DOUBLE,"
                + "sugarAlcohol DOUBLE,"
                + "saturatedFat DOUBLE,"
                + "fiber DOUBLE,"
                + "sugar DOUBLE,"
                + "salt DOUBLE,"
                + "energy DOUBLE,"
                + "energyKcal DOUBLE,"
                + "fat DOUBLE,"
                + "protein DOUBLE,"
                + "amount INTEGER"
                + ");"
        );
        createFoodTable.execute();
        createFoodTable.close();
        
        PreparedStatement createFoodDateTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS fooddate ("
                + "food_id INTEGER,"
                + "date_id INTEGER ,"
                + "PRIMARY KEY(food_id, date_id),"
                + "FOREIGN KEY(food_id) REFERENCES food(id),"
                + "FOREIGN KEY(date_id) REFERENCES date(id)"
                + ");"
        );
        createFoodDateTable.execute();
        createFoodDateTable.close();
        connection.close();
        
    }
    
    public void save(Food food, int amount) throws SQLException {
        Connection connection = getConnetion();
        int dateId = getDateId(LocalDate.now());
        if (dateId < 0) {
            dateId = saveCurrentDate();
        }
        
        PreparedStatement saveFood = connection.prepareStatement("INSERT INTO food"
                + "(carbohydrate, alcohol, organicAcids, sugarAlcohol, saturatedFat, fiber, sugar, salt, energy, energyKcal, fat, protein, amount) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        saveFood.setDouble(1, food.getNutrient("carbohydrate"));
        saveFood.setDouble(2, food.getNutrient("alcohol"));
        saveFood.setDouble(3, food.getNutrient("organicAcids"));
        saveFood.setDouble(4, food.getNutrient("sugarAlcohol"));
        saveFood.setDouble(5, food.getNutrient("saturatedFat"));
        saveFood.setDouble(6, food.getNutrient("fiber"));
        saveFood.setDouble(7, food.getNutrient("sugar"));
        saveFood.setDouble(8, food.getNutrient("salt"));
        saveFood.setDouble(9, food.getNutrient("energy"));
        saveFood.setDouble(10, food.getNutrient("energyKcal"));
        saveFood.setDouble(11, food.getNutrient("fat"));
        saveFood.setDouble(12, food.getNutrient("protein"));
        saveFood.setDouble(13, amount);
        
        saveFood.executeUpdate();
        
        int foodId = getLastId(connection);
        
        PreparedStatement saveFoodDate = connection.prepareStatement("INSERT INTO fooddate(food_id, date_id) VALUES (?, ?);");
        saveFoodDate.setInt(1, foodId);
        saveFoodDate.setInt(2, dateId);
        
        saveFoodDate.executeUpdate();
        
        connection.close();
        
    }
    
    public int getDateId(LocalDate date) throws SQLException {
        Connection connection = getConnetion();
        PreparedStatement getDate = connection.prepareStatement("SELECT * FROM date "
                + "WHERE date == ?;"
        );
        getDate.setDate(1, Date.valueOf(date));
        ResultSet resultSet = getDate.executeQuery();

        int id = -1;
        while (resultSet.next()) {
            id = resultSet.getInt("id");
        }

        getDate.close();
        resultSet.close();

        connection.close();

        return id;
    }
    
    private int saveCurrentDate() throws SQLException {
        Connection connection = getConnetion();
        PreparedStatement saveCurrentDate = connection.prepareStatement("INSERT INTO date(date) VALUES(?);");
        saveCurrentDate.setDate(1, Date.valueOf(LocalDate.now()));
        saveCurrentDate.executeUpdate();
        saveCurrentDate.close();
        int id = getLastId(connection);
        connection.close();
        return id;
    }
    
    private int getLastId(Connection connection) throws SQLException {
        PreparedStatement getLastId = connection.prepareStatement("SELECT last_insert_rowid() AS id;");
        ResultSet resultSet = getLastId.executeQuery();
        
        int id = -1;
        while (resultSet.next()) {
            id = resultSet.getInt("id");
        }
        
        return id;
    }
    
    public Connection getConnetion() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }
        return DriverManager.getConnection("jdbc:sqlite:" + this.database);
    }
    
}
