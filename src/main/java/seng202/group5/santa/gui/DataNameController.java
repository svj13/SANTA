package seng202.group5.santa.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seng202.group5.santa.data.UserListDatabase;
import javax.swing.text.html.ImageView;
import java.io.IOException;

/**
 * Gets the name of the data set and adds a record in the user list table for it
 * @author Group5
 */
public class DataNameController {

    public RootController mainController;

    @FXML
    private Label displayLabel1;

    @FXML
    private Label displayLabel2;

    @FXML
    private TextField nameText;

    @FXML
    private Button btnSubmit;

    @FXML
    private ImageView image;

    /**
     * Sets up the enter data set name message
     */
    public static void getName (RootController controller) {
        Stage createStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(DataNameController.class.getResource("/EnterDataName.fxml"));
            Parent createForm = loader.load();
            DataNameController name = loader.getController();
            name.setRootController(controller);
            Scene scene = new Scene(createForm);
            createStage.setScene(scene);
            createStage.setTitle("Enter Data Set Name");
            createStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets an instance of the root controller so that future function calls will be visible to the user
     * @param controller RootController
     */
    public void setRootController(RootController controller){
        mainController = controller;
    }

    /**
     * If the name entered is correct - adds it to the data set table
     */
    @FXML
    void submitButton (ActionEvent event) {
        UserListDatabase database = new UserListDatabase();
        String temp = nameText.getText();
        String dataSetName = temp.replaceAll("[-+.^():,']","");
        // checks to see that no name has been entered
        if (dataSetName.equals("")) {
            displayLabel1.setText("No name entered");
            displayLabel2.setText("Enter a new name");
            // checks to see if a Data set name is all ready taken
        } else if (database.getDistinct("DISPLAYTEXT").contains(dataSetName)) {
            displayLabel1.setText("Sorry, that name is");
            displayLabel2.setText("already taken");
        }
        else {
            database.addCrimeListId(dataSetName);
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();
            mainController.setUpUserList();
            mainController.setUserListBoxName(dataSetName);
        }
    }
}