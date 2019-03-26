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

        try {
            foodList = Fineli.search("omena");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        for (Food food : foodList) {
            // Omena-kaurahiutalepaistos
            if (food.getId() == 4002) found = true;
        }

        assertTrue(found);
    }
    
    @Test
    public void searchWithUnknownFoodReturnsEmptyList() {
        foodList.add(new Food());
        
        try {
            foodList = Fineli.search("aFoodThatDefinitelyDoesNotExist");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        assertEquals(0, foodList.size());
    }
}
