package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    @FXML Label recipeNameLabel;
    @FXML ImageView recipeImage;

    @FXML Label recipeTimeLabel;
    @FXML Label recipePriceLabel;
    @FXML Label recipeDescriptionLabel;

    @FXML ImageView recipeDifficultyImage;
    @FXML ImageView recipeCuisineImage;
    @FXML ImageView recipeMainIngredientImage;

    private RecipeSearchController parentController;
    private Recipe recipe;

    @FXML
    protected void onClick(Event event){
        parentController.openRecipeView(recipe);
    }

    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        recipeNameLabel.setText(recipe.getName());
        recipeImage.setImage(recipe.getFXImage());
        recipeTimeLabel.setText(recipe.getTime() + " minutes");
        recipePriceLabel.setText(recipe.getPrice() + " kr");
        recipeDescriptionLabel.setText(recipe.getDescription());

        var difficultyPath = RecipeRetriever.Difficulty.getPathByKey(recipe.getDifficulty());
        recipeDifficultyImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(difficultyPath)));
        var cuisinePath = RecipeRetriever.Cuisine.getPathByKey(recipe.getCuisine());
        recipeCuisineImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(cuisinePath)));
        var mainIngredientPath = RecipeRetriever.MainIngredient.getPathByKey(recipe.getMainIngredient());
        recipeMainIngredientImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(mainIngredientPath)));

        this.recipe = recipe;
        this.parentController = recipeSearchController;
    }
}
