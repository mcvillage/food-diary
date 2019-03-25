package ui;

import api.Fineli;
import domain.Food;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class TextUI {
    
    public static void start(Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("Anna haettavan elintarvikkeen nimi: ");
            String keyword = scanner.nextLine().trim();
            try {
                List<Food> foodList = Fineli.search(keyword);
                System.out.println("RUUAT\n======");
                for (Food food : foodList) {
                    System.out.println(food);
                }
            } catch (IOException ex) {
                System.out.println("Haussa tapahtui virhe: " + ex.getMessage());
            }
            
            System.out.println("Poistu painamalla ENTER tai jatka syöttämällä 'j'");
            String command = scanner.nextLine();
            if (!command.toLowerCase().equals("j")) {
                break;
            }
        }
    }
    
}
