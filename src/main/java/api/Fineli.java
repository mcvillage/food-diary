package api;

import domain.Food;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Fineli {
    
    public static List<Food> search(String keyword) {
        keyword = keyword.replaceAll("\\s+", "%20");
        String url = "https://fineli.fi/fineli/api/v1/foods?q=" + keyword;
        
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonString = readAll(rd);
            List<Food> foodList = convertToFoodList(new JSONArray(jsonString));
            is.close();
            return foodList;
        } catch (IOException ex) {
            System.out.println("Error in Fineli search");
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public static ObservableList<Food> searchObservable(String keyword) {
        List<Food> foodList = search(keyword);
        return FXCollections.observableArrayList(foodList);
    }
    
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static Food convertToFood(JSONObject jsonObject) {
        Food food = new Food(jsonObject.getInt("id"), jsonObject.getJSONObject("name").getString("fi"));
        food.setNutrient("carbohydrate", jsonObject.getDouble("carbohydrate"));
        food.setNutrient("alcohol", jsonObject.getDouble("alcohol"));
        food.setNutrient("organicAcids", jsonObject.getDouble("organicAcids"));
        food.setNutrient("sugarAlcohol", jsonObject.getDouble("sugarAlcohol"));
        food.setNutrient("saturatedFat", jsonObject.getDouble("saturatedFat"));
        food.setNutrient("fiber", jsonObject.getDouble("fiber"));
        food.setNutrient("sugar", jsonObject.getDouble("sugar"));
        food.setNutrient("salt", jsonObject.getDouble("salt")/1000); //API returns salt in milligrams
        food.setNutrient("energy", jsonObject.getDouble("energy"));
        food.setNutrient("energyKcal", jsonObject.getDouble("energyKcal"));
        food.setNutrient("fat", jsonObject.getDouble("fat"));
        food.setNutrient("protein", jsonObject.getDouble("protein"));

        return food;
    }

    private static List<Food> convertToFoodList(JSONArray jsonArray) {
        List<Food> foodList = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            foodList.add(convertToFood(jsonArray.getJSONObject(i)));
        }
        return foodList;
    }
    
}
