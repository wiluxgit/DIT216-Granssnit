
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
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

    @FXML AnchorPane recipeDetailPane;
    @FXML AnchorPane recipeDetailPaneBackground;
    @FXML SplitPane searchPane;

    @FXML Label recipeDetailLabel;
    @FXML ImageView recipeDetailImageView;
    @FXML ImageView recipeDetailMainIngredientImage;
    @FXML ImageView recipeDetailDifficultyImage;
    @FXML Label recipeDetailTimeLabel;
    @FXML Label recipeDetailPriceLabel;
    @FXML Label recipeDetailDescriptionLabel;
    @FXML Label recipeDetailInstructionsLabel;
    @FXML Label recipeDetailPortionsLabel;
    @FXML Label recipeDetailIngredientsLabel;
    @FXML ImageView recipeDetailCuisineImage;
    @FXML ImageView recipeDetailCloseImageView;

    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();
    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeRetriever recipeRetriever;
    public static int globMaxPrice = 150;
    public static int globMaxTime = 120;

    @FXML
    public void closeRecipeView(){
        searchPane.toFront();
    }
    public void openRecipeView(Recipe recipe){
        populateRecipeDetailView(recipe);
        recipeDetailPaneBackground.toFront();
    }

    public void populateRecipeDetailView(Recipe recipe){
        recipeDetailLabel.setText(recipe.getName());
        recipeDetailImageView.setImage(recipe.getFXImage());
        recipeDetailDescriptionLabel.setText(recipe.getDescription());
        recipeDetailInstructionsLabel.setText(recipe.getInstruction());
        recipeDetailPortionsLabel.setText("Portioner:"+recipe.getServings());
        recipeDetailTimeLabel.setText("Tid:"+recipe.getTime()+ " minuter");
        recipeDetailPriceLabel.setText("Pris:"+recipe.getTime()+ " kr");

        var difficultyPath = RecipeRetriever.Difficulty.getPathByKey(recipe.getDifficulty());
        if (difficultyPath != null)
            try {
                recipeDetailDifficultyImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(difficultyPath)));
            } catch (Exception e){recipeDetailDifficultyImage.setImage(null);}
        var mainIngredientPath = RecipeRetriever.MainIngredient.getPathByKey(recipe.getMainIngredient());
        if (mainIngredientPath != null) {
            try {
                recipeDetailMainIngredientImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(mainIngredientPath)));
            } catch (Exception e){recipeDetailMainIngredientImage.setImage(null);}
        }
        var cuisinePath = RecipeRetriever.Cuisine.getPathByKey(recipe.getCuisine());
        if (mainIngredientPath != null) {
            try {
                recipeDetailCuisineImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(cuisinePath)));
            } catch (Exception e){recipeDetailCuisineImage.setImage(null);}
        }

        StringBuilder sb = new StringBuilder();
        for (var x : recipe.getIngredients()){
            sb.append(x + "\n");
        }
        sb.deleteCharAt(sb.length()-1);
        recipeDetailIngredientsLabel.setText(sb.toString());
    }

    @FXML
    public void closeButtonMouseEntered(){
        recipeDetailCloseImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMousePressed(){
        //samma princip som ovan, ta rätt bild
        recipeDetailCloseImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_pressed.png")));
    }

    @FXML
    public void closeButtonMouseExited(){
        //samma princip som ovan, ta rätt bild. Denna metod ska återställa bilden
        //ifall användaren tar bort musen.
        recipeDetailCloseImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close.png")));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        recipeRetriever = new RecipeRetriever();

        for (Recipe recipe:recipeRetriever.query(db)) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }

        initMainIngredient();
        initKitchen();
        initDifficultiToggle();
        initMaxPrice();
        initMaxTime();

        populateMainIngredientComboBox();
        populateCuisineComboBox();
    }

    private void updateRecipeList(){
        recipeListFlowPane.getChildren().clear();
        for (Recipe recipe:recipeRetriever.query(db)) {
            RecipeListItem recipeItem = recipeListItemMap.get(recipe.getName());
            recipeListFlowPane.getChildren().add(recipeItem);
        }
    }

    private void initMaxTime(){
        maxTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            int roundTime = (newValue.intValue()/10)*10;
            chosenTimeLabel.setText(roundTime+" Minuter");
            maxTime.setValue(roundTime);
            if(newValue != null && !newValue.equals(oldValue) /*&& maxTime.isValueChanging() == false*/) {
                recipeRetriever.setMaxTime(roundTime);
                updateRecipeList();
            }
        });
    }

    public void initMainIngredient(){
        mainIngredient.getItems().add("Visa Alla");
        mainIngredient.getItems().addAll(RecipeRetriever.MainIngredient.getAllKeys());
        mainIngredient.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                recipeRetriever.setMainIngredient(newValue);
                updateRecipeList();
            }
        });
    }

    private void initKitchen(){
        kitchen.getItems().add("Visa Alla");
        kitchen.getItems().addAll(RecipeRetriever.Cuisine.getAllKeys());
        kitchen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                recipeRetriever.setCuisine(newValue);
                updateRecipeList();
            }
        });
    }

    private void initDifficultiToggle(){
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
    }

    private void initMaxPrice(){
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
    }

    private void populateMainIngredientComboBox(){
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override public ListCell<String> call(ListView<String> p) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                        if (item == null || empty) setGraphic(null);

                        else {
                            Image icon = null;
                            String iconPath;
                            try {
                                iconPath = RecipeRetriever.MainIngredient.getPathByKey(item);
                                icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                            } catch(NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredient.setButtonCell(cellFactory.call(null));
        mainIngredient.setCellFactory(cellFactory);
    }
    private void populateCuisineComboBox(){
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override public ListCell<String> call(ListView<String> p) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                        if (item == null || empty) setGraphic(null);

                        else {
                            Image icon = null;
                            String iconPath;
                            try {
                                iconPath = RecipeRetriever.Cuisine.getPathByKey(item);
                                icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                            } catch(NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        kitchen.setButtonCell(cellFactory.call(null));
        kitchen.setCellFactory(cellFactory);
    }
}
