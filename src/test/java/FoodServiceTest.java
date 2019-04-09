import dao.FoodDao;
import domain.Food;
import domain.FoodService;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.junit.After;
import org.junit.Before;
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
    public void foodCanBeSavedWithCorrectDetails() throws SQLException {
        int id = 401;
        String name = "Omena, kuivattu";
        double carbohydrateAmount = 60.2;
        int amount = 50;
        
        Food food = new Food(id, name);
        food.setNutrient("carbohydrate", carbohydrateAmount);
        this.foodService.saveWithCurrentDate(food, amount);

        boolean found = false;
        List<Food> foodList = this.foodService.getFoodListByDate(LocalDate.now());
        for (Food f : foodList) {
            if (f.getId() == id && f.getAmount() == 50 && f.getNutrient("carbohydrate") == 30.1 && f.getName().equals(name)) {
                found = true;
                break;
            }
        }
        
        assertTrue(found);
   }
}
