import domain.Food;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class FoodTest {
    
    private Food food;
    
    @Before
    public void setUp() {
        this.food = new Food(401, "Omena, kuivattu");
        this.food.setNutrient("energy", 1149);
        this.food.setNutrient("carbohydrate", 60.2);
        this.food.setNutrient("fat", 0.3);
        this.food.setNutrient("protein", 0.9);
        this.food.setNutrient("alcohol", 0);
    }
    
    @Test
    public void equalsMethodWorks() {
        Food anotherFood = new Food(401, "Omena, kuivattu");
        Food nullFood = null;
        String stringFood = "food";
        
        assertTrue(this.food.equals(anotherFood));
        assertFalse(this.food.equals(nullFood));
        assertFalse(this.food.equals(stringFood));
    }
    
}
