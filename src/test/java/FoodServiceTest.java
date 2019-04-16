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
    
    @Test
    public void foodCanBeSetToFoodService() {
        int id = 401;
        String name = "Omena, kuivattu";
        Food food = new Food(id, name);
        
        foodService.setFood(food);
        assertTrue(foodService.getFood().equals(food));
    }
    
    @Test
    public void dateCanBeSetToFoodService() {
        LocalDate date = LocalDate.now();
        
        foodService.setDate(date);
        assertTrue(foodService.getDate().isEqual(date));
    }
    
    @Test
    public void getFirstDayOfWeekReturnsMonday() {
        LocalDate monday = LocalDate.of(2019, 4, 15);
        LocalDate sunday = LocalDate.of(2019, 4, 21);
        
        foodService.setDate(monday);
        LocalDate firstDayOfWeekMon = foodService.getFirstDayOfWeek();
        
        foodService.setDate(sunday);
        LocalDate firstDayOfWeekSun = foodService.getFirstDayOfWeek();
        
        assertTrue(firstDayOfWeekMon.isEqual(monday));
        assertTrue(firstDayOfWeekSun.isEqual(monday));
    }
    
    @Test
    public void getDayOfWeekNameReturnsCorrectName() {
        LocalDate monday = LocalDate.of(2019, 4, 15);
        
        assertTrue(foodService.getDayOfWeekName(monday).equals("Ma"));
        assertTrue(foodService.getDayOfWeekName(monday.plusDays(1)).equals("Ti"));
        assertTrue(foodService.getDayOfWeekName(monday.plusDays(2)).equals("Ke"));
        assertTrue(foodService.getDayOfWeekName(monday.plusDays(3)).equals("To"));
        assertTrue(foodService.getDayOfWeekName(monday.plusDays(4)).equals("Pe"));
        assertTrue(foodService.getDayOfWeekName(monday.plusDays(5)).equals("La"));
        assertTrue(foodService.getDayOfWeekName(monday.plusDays(6)).equals("Su"));
    }
    
    @Test
    public void getTotalCaloriesByDateReturnsCorrectSum() {
        LocalDate date = LocalDate.of(1815, 12, 10);

        Food food = new Food(401, "Omena, kuivattu");
        food.setNutrient("energyKcal", 275);
        
        foodService.save(food, date, 200);
        
        assertEquals(2 * 275L, foodService.getTotalCaloriesByDate(date));
        
    }
 }
