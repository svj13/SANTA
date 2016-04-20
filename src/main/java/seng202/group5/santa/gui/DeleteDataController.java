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
import seng202.group5.santa.data.AsyncTask;
import seng202.group5.santa.data.CrimeRecord;
import seng202.group5.santa.data.Database;
import seng202.group5.santa.data.UserListDatabase;
import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Used for deleting a data set and all corresponding crime records
 * @author Group5
 */
public class DeleteDataController {

    public RootController mainController;

    @FXML
    private Label displayLabel1;

    @FXML
    private Label dataSet;

    @FXML
    private TextField nameText;

    @FXML
    private Button btnYes;

    @FXML
    private Button btnNo;

    @FXML
    private ImageView image;

    public UserListDatabase dataSetDatabase = new UserListDatabase();
    public Database crimeDatabase = new Database();
    public Integer deleteDatabase = new Integer(0);

    /**
     * Threads the deleting task
     */
    public class deleteThread extends AsyncTask<String> {
        public void onPreExecute(){

            mainController.showBackgroundTask("Deleting Records");
            ArrayList< CrimeRecord> temp = new ArrayList<CrimeRecord>();
            mainController.setTableView(temp);
            mainController.clearViewer();
        }

        public String doInBackground() throws Exception {
            dataSetDatabase.deleteUserList(deleteDatabase);
            crimeDatabase.deleteRecord(deleteDatabase, "ALL");
            return null;
        }

        public void onPostExecute(String name, Exception ex) {
            mainController.hideBackgroundTask();
            mainController.setUpTypeFilter();
            mainController.setUpUserList();
        }

        public void progressCallback(Object... params) {
        }
    }

    /**
     * Set up the delete data set message
     */
    public static void showDeleteMessage (RootController controller) {
        Stage createStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(DeleteDataController.class.getResource("/DeleteDataSet.fxml"));
            Parent createForm = loader.load();
            DeleteDataController rootController = loader.getController();
            rootController.setRootController(controller);
            Scene scene = new Scene(createForm);
            createStage.setScene(scene);
            createStage.setTitle("Delete Data Set");
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
        deleteDatabase = mainController.getCurrentDataSet();
        dataSet.setText(mainController.getDataSetName());
    }

    /**
     * Runs the delete date set thread
     */
    @FXML
    public void yes (ActionEvent event) {
        deleteThread deleteCrimes = new deleteThread();
        deleteCrimes.execute();
        Node source = (Node) event.getSource();
        Stage thisStage = (Stage) source.getScene().getWindow();
        thisStage.close();
    }

    /**
     * Closes the window
     */
    @FXML
    public void no (ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage thisStage = (Stage) source.getScene().getWindow();
        thisStage.close();
    }
}
