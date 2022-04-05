
package recipesearch;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {
    @FXML private FlowPane recipeListFlowPane;
    @FXML ComboBox mainIngredient;
    @FXML ComboBox kitchen;
    @FXML RadioButton difficultyAll;
    @FXML RadioButton difficultyEasy;
    @FXML RadioButton difficultyMedium;
    @FXML RadioButton difficultyHard;
    @FXML Spinner maxPrice;
    @FXML Slider maxTime;
    @FXML Label chosenTimeLabel;

    @FXML Label recipeDetailPane;
    @FXML Label searchPane;

    @FXML Label recipeDetailLabel;
    @FXML ImageView recipeDetailImageView;
    @FXML Button recipeDetailButtonClose;

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeRetriever recipeRetriever;
    public static int globMaxPrice = 150;
    public static int globMaxTime = 120;

    @FXML
    public void closeRecipeView(){
        searchPane.toFront();
    }
    @FXML
    public void openRecipeView(Recipe recipe){
        populateRecipeDetailView(recipe);
        recipeDetailPane.toFront();
    }

    public void populateRecipeDetailView(Recipe recipe){
        recipeDetailLabel.setText(recipe.getName());
        recipeDetailImageView.setImage(recipe.getFXImage());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recipeRetriever = new RecipeRetriever();

        mainIngredient.getItems().addAll(RecipeRetriever.MainIngredient.getAllKeys());
        mainIngredient.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                recipeRetriever.setMainIngredient(newValue);
                updateRecipeList();
            }
        });
        kitchen.getItems().addAll(RecipeRetriever.Cuisine.getAllKeys());
        kitchen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                recipeRetriever.setCuisine(newValue);
                updateRecipeList();
            }
        });

        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        difficultyAll.setToggleGroup(difficultyToggleGroup);
        difficultyEasy.setToggleGroup(difficultyToggleGroup);
        difficultyMedium.setToggleGroup(difficultyToggleGroup);
        difficultyHard.setToggleGroup(difficultyToggleGroup);
        difficultyAll.setSelected(true);
        difficultyToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (difficultyToggleGroup.getSelectedToggle() != null) {
                RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                recipeRetriever.setDifficulty(selected.getText());
                updateRecipeList();
            }
        });

        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, globMaxPrice, globMaxPrice, 10
        );
        maxPrice.setValueFactory(spinnerValueFactory);
        maxPrice.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                recipeRetriever.setMaxPrice(newValue);
                updateRecipeList();
            }
        });
        maxPrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == false) {
                Integer value = Integer.valueOf(maxPrice.getEditor().getText());
                recipeRetriever.setMaxPrice(value);
                updateRecipeList();
            }
        });

        maxTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            int roundTime = (newValue.intValue()/10)*10;
            chosenTimeLabel.setText(roundTime+" Minuter");
            if(newValue != null && !newValue.equals(oldValue) /*&& maxTime.isValueChanging() == false*/) {
                recipeRetriever.setMaxTime(roundTime);
                updateRecipeList();
            }
        });

    }

    private void updateRecipeList(){
        recipeListFlowPane.getChildren().clear();
        for (var dbRecipe:recipeRetriever.query(db)) {
            RecipeListItem recipeItem = new RecipeListItem(dbRecipe, this);
            recipeListFlowPane.getChildren().add(recipeItem);
        }
    }

}