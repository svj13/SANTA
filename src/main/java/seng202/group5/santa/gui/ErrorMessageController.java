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
import javafx.stage.StageStyle;
import javax.swing.text.html.ImageView;
import java.io.IOException;

/**
 * Manages the error message popup
 * @author Group5
 */
public class ErrorMessageController {

    public RootController mainController;

    @FXML
    private Label displayLabel1;

    @FXML
    private Label displayLabel2;

    @FXML
    private TextField nameText;

    @FXML
    private Button btnOk;

    @FXML
    private ImageView image;

    /**
     * Sets up the error message form
     */
    public static void showError (String error) {
        Stage createStage = new Stage(StageStyle.UTILITY);
        try {
            FXMLLoader loader = new FXMLLoader(ErrorMessageController.class.getResource("/ErrorMessage.fxml"));
            Parent createForm = loader.load();
            ErrorMessageController controller = loader.getController();
            controller.setRootController(error);
            Scene scene = new Scene(createForm);
            createStage.setScene(scene);
            createStage.setTitle("Error Message");
            createStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRootController(String error) {
        displayLabel1.setText(error);
    }

    /**
     * Closes the error message form
     */
    @FXML
    void submitButton (ActionEvent event) {
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();
    }
}
