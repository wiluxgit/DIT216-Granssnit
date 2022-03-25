
package addressbook;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.chalmers.cse.dat215.lab1.Presenter;

public class AddressBookController implements Initializable {

    private Presenter presenter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        presenter = new Presenter(
                listViewContacts,
                textFieldInfoFirstName,
                textFieldInfoLastName,
                textFieldInfoPhone,
                textFieldInfoEmail,
                textFieldInfoAddress,
                textFieldInfoPostCode,
                textFieldInfoCity
        );
        presenter.init();

        listViewContacts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                presenter.contactsListChanged();
            }
        });
        
        textFieldInfoFirstName.focusedProperty().addListener(new TextFieldListener(textFieldInfoFirstName));
        textFieldInfoLastName.focusedProperty().addListener(new TextFieldListener(textFieldInfoLastName));
        textFieldInfoPhone.focusedProperty().addListener(new TextFieldListener(textFieldInfoPhone));
        textFieldInfoEmail.focusedProperty().addListener(new TextFieldListener(textFieldInfoEmail));
        textFieldInfoAddress.focusedProperty().addListener(new TextFieldListener(textFieldInfoAddress));
        textFieldInfoPostCode.focusedProperty().addListener(new TextFieldListener(textFieldInfoPostCode));
        textFieldInfoCity.focusedProperty().addListener(new TextFieldListener(textFieldInfoCity));
    }

    @FXML
    protected void newContactActionPerformed(ActionEvent event){
        presenter.newContact();
    }

    @FXML
    protected void deleteContactActionPerformed(ActionEvent event){
        presenter.removeCurrentContact();
    }

    @FXML private MenuBar menuBar;
    @FXML private Button buttonNew;
    @FXML private Button buttonDelete;
    @FXML private ListView listViewContacts;

    @FXML private TextField textFieldInfoFirstName;
    @FXML private TextField textFieldInfoLastName;
    @FXML private TextField textFieldInfoPhone;
    @FXML private TextField textFieldInfoEmail;
    @FXML private TextField textFieldInfoAddress;
    @FXML private TextField textFieldInfoPostCode;
    @FXML private TextField textFieldInfoCity;

    @FXML 
    protected void openAboutActionPerformed(ActionEvent event) throws IOException{
    
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("addressbook/resources/AddressBook");
        Parent root = FXMLLoader.load(getClass().getResource("address_book_about.fxml"), bundle);
        Stage aboutStage = new Stage();
        aboutStage.setScene(new Scene(root));
        aboutStage.setTitle(bundle.getString("about.title.text"));
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.setResizable(false);
        aboutStage.showAndWait();
    }
    
    @FXML 
    protected void closeApplicationActionPerformed(ActionEvent event) throws IOException{
        
        Stage addressBookStage = (Stage) menuBar.getScene().getWindow();
        addressBookStage.hide();
    }

    private class TextFieldListener implements ChangeListener<Boolean> {
        private TextField textField;

        public TextFieldListener(TextField textField){
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue)
                presenter.textFieldFocusGained(textField);
            else
                presenter.textFieldFocusLost(textField);
        }
    }
}
