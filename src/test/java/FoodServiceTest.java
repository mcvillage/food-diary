import dao.FoodDao;
import domain.Food;
import domain.FoodService;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class FoodServiceTest {
    
    private FoodDao foodDao;
    private FoodService foodService;
    
    @Before
    public void setUp() throws SQLException {
        this.foodDao = new FoodDao("food-test.db");
        this.foodService = new FoodService(foodDao);
    }
    
    @After
    public void tearDown() {
        File file = new File("food-test.db");
        file.delete();
    }

    @Test
    public void foodCanBeSaved() throws SQLException {
        int id = 401;
        String name = "Omena, kuivattu";
        double carbohydrateAmount = 60.2;
        int amount = 50;
        
        Food food = new Food(id, name);
        food.setNutrient("carbohydrate", carbohydrateAmount);
        this.foodService.saveWithCurrentDate(food, amount);

        boolean found = false;
        for (Food f : this.foodService.getFoodListByDate(LocalDate.now())) {
            if (f.getId() > 0) {
                found = true;
                break;
            }
        }
        
        assertTrue(found);
   }
}
