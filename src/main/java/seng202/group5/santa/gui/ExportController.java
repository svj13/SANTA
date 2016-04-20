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
import seng202.group5.santa.data.*;
import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages the exporting of crimes into a .csv format
 * @author Group5
 */
public class ExportController {

    public RootController mainController;
    public String fileName;

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

    public void setRootController(RootController controller){
        mainController = controller;
    }

    /**
     * Checks that a given name is valid and then runs the export method
     */
    @FXML
    void yes (ActionEvent event) {
        String listName = nameText.getText();
        // checks to see no name has been entered listName == "" || listName.equals(null) || listName == null ||
        if (listName.equals("")) {
            displayLabel1.setText("No name entered");
            displayLabel2.setText("Enter a name");
        }else{
            fileName = listName;
            ExportThread importCrimes = new ExportThread();
            importCrimes.execute();
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();
        }
    }

    /**
     * Closes the export form
     */
    @FXML
    void no (ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage thisStage = (Stage) source.getScene().getWindow();
        thisStage.close();
    }

    /**
     * Sets up the export form
     */
    public void ExportController (RootController controller) {
        mainController = controller;
        //Creates a scene that the user will use to choose the name of the csv
        Stage createStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(DataNameController.class.getResource("/EnterExportName.fxml"));
            Parent createForm = loader.load();
            ExportController name = loader.getController();
            name.setRootController(controller);
            Scene scene = new Scene(createForm);
            createStage.setScene(scene);
            createStage.setTitle("Exporter");
            createStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Thread the export method
     */
    public class ExportThread extends AsyncTask<ArrayList<CrimeRecord>> {

        public  void onPreExecute(){
            mainController.showBackgroundTask("Exporting File");
            mainController.hideButtons();
        }

        public  ArrayList<CrimeRecord>  doInBackground() throws Exception{
            GenerateCsvFile.generateCsv(mainController.getCurrentlyDisplayedCrimes(),fileName);
            return null;
        }

        public  void onPostExecute(ArrayList<CrimeRecord>  result, Exception ex) {
            mainController.hideBackgroundTask();
            mainController.showButtons();
        }

        public  void progressCallback(Object... params) {}

        public ExportThread () {}
    }
}