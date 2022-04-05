
package recipesearch;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {
    @FXML private FlowPane recipeListFlowPane;

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainIngredient.getItems().addAll(RecipeRetriever.MainIngredient.getAllKeys());
        mainIngredient.getItems().addAll(RecipeRetriever.MainIngredient.getAllKeys());
    }

    @FXML ComboBox mainIngredient;
    @FXML ComboBox kitchen;

    private void updateRecipeList(){
        recipeListFlowPane.getChildren().clear();
        RecipeRetriever recipeRetriver = new RecipeRetriever();
        for (var dbRecipe:recipeRetriver.query()) {
            RecipeListItem recipeItem = new RecipeListItem(dbRecipe, this);
            recipeListFlowPane.getChildren().add(recipeItem);
        }
    }

}