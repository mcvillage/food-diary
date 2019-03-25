package domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Food {
    
    private int id;
    private String name;
    
    /* All units are grams if not specified otherwise */
    
    private double carbohydrate;
    private double alcohol;
    private double organicAcids;
    private double sugarAlcohol;
    private double saturatedFat;
    private double fiber;
    private double sugar;
    private double salt; // in milligrams
    private double energy; // in kilojoules
    private double energyKcal; // in kilocalories
    private double fat;
    private double protein;
    
}