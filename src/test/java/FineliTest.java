import api.Fineli;
import domain.Food;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FineliTest {
    private List<Food> foodList;
    
    @Before
    public void setUp() {
        foodList = new ArrayList<>();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void searchReturnsFoodList() {
        boolean found = false;
        foodList = Fineli.search("omena");

        for (Food food : foodList) {
            // Omena-kaurahiutalepaistos
            if (food.getId() == 4002) found = true;
        }

        assertTrue(found);
    }
    
    @Test
    public void searchWithUnknownFoodReturnsEmptyList() {
        foodList.add(new Food());
        foodList = Fineli.search("aFoodThatDefinitelyDoesNotExist");
        
        assertEquals(0, foodList.size());
    }
}
