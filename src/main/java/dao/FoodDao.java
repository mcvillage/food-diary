package dao;

import domain.Food;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods to handle different database operations.
 */
public class FoodDao {
    
    private String database;

    public FoodDao(String database) {
        this.database = database;
    }
    /**
     * Initializes database by creating necessary tables.
     */
    public void initDatabase() throws SQLException {
        initDateTable();
        initFoodTable();
        initFoodDateTable();
    }
    
    private void initDateTable() throws SQLException {
        Connection connection = getConnetion();
        PreparedStatement createDateTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS date ("
                + "id INTEGER PRIMARY KEY,"
                + "date DATE"
                + ");"
        );
        createDateTable.execute();
        createDateTable.close();
        connection.close();
    }
    
    private void initFoodTable() throws SQLException {
        Connection connection = getConnetion();
        PreparedStatement createFoodTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS food ("
                + "id INTEGER PRIMARY KEY,"
                + "name VARCHAR(255), "
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
                + "protein DOUBLE"
                + ");"
        );
        createFoodTable.execute();
        createFoodTable.close();
        connection.close();
    }
    
    private void initFoodDateTable() throws SQLException {
        Connection connection = getConnetion();
        PreparedStatement createFoodDateTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS fooddate ("
                + "food_id INTEGER,"
                + "date_id INTEGER ,"
                + "amount INTEGER, "
                + "PRIMARY KEY(food_id, date_id),"
                + "FOREIGN KEY(food_id) REFERENCES food(id),"
                + "FOREIGN KEY(date_id) REFERENCES date(id)"
                + ");"
        );
        createFoodDateTable.execute();
        createFoodDateTable.close();
        connection.close();
    }
    
    /**
     * Saves food to SQLite database and if the food has already 
     * been saved updates the amount of that food.
     * @param food food that will be saved
     * @return id of the food
     */
    public int saveFood(Food food) throws SQLException {
        Connection connection = getConnetion();
        PreparedStatement saveFood = connection.prepareStatement("INSERT INTO food\n"
                + "(carbohydrate, alcohol, organicAcids, sugarAlcohol, saturatedFat, fiber, sugar, salt, energy, energyKcal, fat, protein, id, name)\n"
                + "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?\n"
                + "WHERE NOT EXISTS(SELECT 1 FROM food WHERE id = ?);");
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
        saveFood.setInt(13, food.getId());
        saveFood.setString(14, food.getName());
        saveFood.setInt(15, food.getId());
        
        saveFood.executeUpdate();
        saveFood.close();
        connection.close();
        
        return food.getId();
        
    }
    /**
     * Removes the entry by date
     * @param food food that was eaten.
     * @param date date 
     */
    public void removeEntry(Food food, LocalDate date) throws SQLException {
        Connection connection = getConnetion();
        PreparedStatement remove = connection.prepareStatement("DELETE FROM fooddate\n"
                + "WHERE fooddate.food_id = ? AND fooddate.date_id = (SELECT date.id as id FROM date WHERE date.date = ?)");
        remove.setInt(1, food.getId());
        remove.setDate(2, Date.valueOf(date));
        
        remove.executeUpdate();
        remove.close();
        connection.close();
    }
    
    /**
     * Saves a new date to the database.
     * @param date date that will be saved
     * @return id of the date
     */
    public int saveDate(LocalDate date) throws SQLException {
        Connection connection = getConnetion();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO date(date)\n"
                + "SELECT ?\n"
                + "WHERE NOT EXISTS(SELECT 1 FROM date WHERE date = ?);");
        statement.setDate(1, Date.valueOf(date));
        statement.setDate(2, Date.valueOf(date));
        statement.executeUpdate();
        statement.close();
        int dateId = getDateId(date);
        connection.close();
        
        return dateId;
    }
    
    /**
     * Saves the entry (how much food and when) to the database.
     * @param foodId id of the food
     * @param dateId id of the date
     * @param amount amount of the food in grams
     */
    public void saveFoodDate(int foodId, int dateId, int amount) throws SQLException {
        Connection connection = getConnetion();
        PreparedStatement saveFoodDate = connection.prepareStatement("INSERT OR REPLACE INTO fooddate (food_id, date_id, amount)\n"
                + "VALUES (?, ?, ? + COALESCE((SELECT amount FROM fooddate WHERE food_id = ? AND date_id = ?), 0));");
        saveFoodDate.setInt(1, foodId);
        saveFoodDate.setInt(2, dateId);
        saveFoodDate.setInt(3, amount);
        saveFoodDate.setInt(4, foodId);
        saveFoodDate.setInt(5, dateId);

        saveFoodDate.executeUpdate();
        connection.close();
    }
    /**
     * Returns a list of foods from a specific date
     * @param date date which will be used to get the foods
     * @return list of foods
     */
    public List<Food> getFoodListByDate(LocalDate date) throws SQLException {
        Connection connection = getConnetion();
        PreparedStatement statement = connection.prepareStatement("SELECT fooddate.amount, food.* FROM fooddate\n"
                + "INNER JOIN food ON fooddate.food_id = food.id\n"
                + "INNER JOIN date ON fooddate.date_id = date.id\n"
                + "WHERE date.date = ?");
        statement.setDate(1, Date.valueOf(date));
        ResultSet resultSet = statement.executeQuery();
        
        List<Food> foodList = new ArrayList<>();
        while (resultSet.next()) {
            Food food = new Food(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("amount"));
            food.setNutrient("carbohydrate", resultSet.getDouble("carbohydrate"));
            food.setNutrient("alcohol", resultSet.getDouble("alcohol"));
            food.setNutrient("organicAcids", resultSet.getDouble("organicAcids"));
            food.setNutrient("sugarAlcohol", resultSet.getDouble("sugarAlcohol"));
            food.setNutrient("saturatedFat", resultSet.getDouble("saturatedFat"));
            food.setNutrient("fiber", resultSet.getDouble("fiber"));
            food.setNutrient("sugar", resultSet.getDouble("sugar"));
            food.setNutrient("salt", resultSet.getDouble("salt"));
            food.setNutrient("energy", resultSet.getDouble("energy"));
            food.setNutrient("energyKcal", resultSet.getDouble("energyKcal"));
            food.setNutrient("fat", resultSet.getDouble("fat"));
            food.setNutrient("protein", resultSet.getDouble("protein"));
            
            foodList.add(food);
        }
        
        return foodList;
    }
    
    private int getDateId(LocalDate date) throws SQLException {
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
    /**
     * Connects to the database
     * @return connection to the database
     */
    public Connection getConnetion() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }
        return DriverManager.getConnection("jdbc:sqlite:" + this.database);
    }
    
}
