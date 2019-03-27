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
import org.json.JSONArray;
import org.json.JSONObject;

public class Fineli {
    
    public static List<Food> search(String keyword) throws IOException {
        keyword = keyword.replaceAll("\\s+", "%20");
        String url = "https://fineli.fi/fineli/api/v1/foods?q=" + keyword;
        InputStream is = new URL(url).openStream();
        
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonString = readAll(rd);
            List<Food> foodList = convertToFoodList(new JSONArray(jsonString));
            return foodList;
        } finally {
            is.close();
        }
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
        Food food = new Food(jsonObject.getInt("id"),
                jsonObject.getJSONObject("name").getString("fi"),
                jsonObject.getDouble("carbohydrate"),
                jsonObject.getDouble("alcohol"),
                jsonObject.getDouble("organicAcids"),
                jsonObject.getDouble("sugarAlcohol"),
                jsonObject.getDouble("saturatedFat"),
                jsonObject.getDouble("fiber"),
                jsonObject.getDouble("sugar"),
                jsonObject.getDouble("salt"),
                jsonObject.getDouble("energy"),
                jsonObject.getDouble("energyKcal"),
                jsonObject.getDouble("fat"),
                jsonObject.getDouble("protein")
        );
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
