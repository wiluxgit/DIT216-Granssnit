package recipesearch;

import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RecipeRetriever {
    private String cuisine;
    private String mainIngredient;
    private String difficulty;
    private Integer maxPrice;
    private Integer maxTime;

    /*public List<Recipe> query(RecipeDatabase db, int percentMatch){
        List<Recipe> allRecipes = query(db);
        return allRecipes.stream().takeWhile(x -> x.getMatch() >= percentMatch).toList();
    }*/
    public List<Recipe> query(RecipeDatabase db){
        return db.search(new SearchFilter(
                difficulty,
                Optional.ofNullable(maxTime).orElse(0),
                cuisine,
                Optional.ofNullable(maxPrice).orElse(0),
                mainIngredient)
        );
    }

    public RecipeRetriever() {
        cuisine = null;
        mainIngredient = null;
        difficulty = null;
        maxPrice = null;
        maxTime = null;
    }

    public RecipeRetriever setCuisine(String cuisine) {this.cuisine = cuisine; return this;}
    public RecipeRetriever setMaxTime(Integer maxTime) {this.maxTime = maxTime; return this;}
    public RecipeRetriever setMaxPrice(Integer maxPrice) {this.maxPrice = maxPrice; return this;}
    public RecipeRetriever setDifficulty(String difficulty) {this.difficulty = difficulty; return this;}
    public RecipeRetriever setMainIngredient(String mainIngredient) {this.mainIngredient = mainIngredient; return this;}
    /*public String getCuisine() {return this.cuisine;}
    public Integer getMaxTime() {return this.maxTime;}
    public Integer getMaxPrice() {return this.maxPrice;}
    public String getDifficulty() {return this.difficulty;}
    public String getMainIngredient() {return this.mainIngredient ;}
    */

    interface Keyable {
        public String key();
    }

    enum Cuisine implements Keyable {
        Sweden("Sverige","RecipeSearch/resources/icon_flag_sweden.png"),
        Greece("Grekland","RecipeSearch/resources/icon_flag_greece.png"),
        India("Indien","RecipeSearch/resources/icon_flag_india.png"),
        Asia("Asien","RecipeSearch/resources/icon_flag_asia.png"),
        Africa("Afrika","RecipeSearch/resources/icon_flag_africa.png"),
        France("Frankrike","RecipeSearch/resources/icon_flag_france.png");

        private final String dbKey;
        private final String iconPath;
        private Cuisine(String key, String iconPath){
            this.dbKey = key;
            this.iconPath = iconPath;
        }
        public String key() {
            return this.dbKey;
        }
        public String path() {
            return this.iconPath;
        }
        public static List<String> getAllKeys(){
            return (Arrays.stream(Cuisine.class.getEnumConstants())
                    .map(Cuisine::key)).toList();
        }
        public static String getPathByKey(String key){
            var match = (Arrays.stream(Cuisine.class.getEnumConstants())
                    .filter(x -> x.key().equals(key)).map(Cuisine::path)).toList();
            return match.get(0);
        }
    }
    enum MainIngredient implements Keyable {
        Meat("Kött","RecipeSearch/resources/icon_main_meat.png"),
        Fish("Fisk", "RecipeSearch/resources/icon_main_fish.png"),
        Chicken("Kyckling", "RecipeSearch/resources/icon_main_chicken.png"),
        Vegetarian("Vegetarisk", "RecipeSearch/resources/icon_main_veg.png");

        private final String dbKey;
        private final String iconPath;
        private MainIngredient(String key, String iconPath){
            this.dbKey = key;
            this.iconPath = iconPath;
        }
        public String key() {
            return this.dbKey;
        }
        public String path() {
            return this.iconPath;
        }
        public static List<String> getAllKeys(){
            return (Arrays.stream(MainIngredient.class.getEnumConstants())
                    .map(MainIngredient::key)).toList();
        }
        public static String getPathByKey(String key){
            var match = (Arrays.stream(MainIngredient.class.getEnumConstants())
                    .filter(x -> x.key().equals(key)).map(MainIngredient::path)).toList();
            return match.get(0);
        }
    }
    enum Difficulty implements Keyable {
        Easy("Lätt","RecipeSearch/resources/icon_difficulty_easy.png"),
        Medium("Mellan","RecipeSearch/resources/icon_difficulty_medium.png"),
        Hard("Svår","RecipeSearch/resources/icon_difficulty_hard.png");

        private final String dbKey;
        private final String iconPath;
        private Difficulty(String key, String iconPath){
            this.dbKey = key;
            this.iconPath = iconPath;
        }
        public String key() {
            return this.dbKey;
        }
        public String path() {
            return this.iconPath;
        }
        public static List<String> getAllKeys(){
            return (Arrays.stream(Difficulty.class.getEnumConstants()).map(Difficulty::key)).toList();
        }
        public static String getPathByKey(String key){
            var match = (Arrays.stream(Difficulty.class.getEnumConstants())
                    .filter(x -> x.key().equals(key)).map(Difficulty::path)).toList();
            return match.get(0);
        }
    }
}
