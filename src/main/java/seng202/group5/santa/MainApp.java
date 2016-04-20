package seng202.group5.santa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import seng202.group5.santa.data.AsyncTask;

import java.io.IOException;


/**
 * The app that launches when the program is run. Sets up the GUI, ready for user input
 * @author Group5
 */
public class MainApp extends Application {

    /**
     * Stage for holding the main GUI
     */
    public Stage primaryStage = new Stage();

    /**
     * Main function, just launches the program
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * thread the start up of the root controller and show a start up logo
     */
    public class loadingThread extends AsyncTask<Parent> {
        Stage createStage;

        public void onPreExecute() {
            createStage = new Stage(StageStyle.UNDECORATED);
//            createStage.initModality(Modality.APPLICATION_MODAL);
//            createStage.initStyle(StageStyle.TRANSPARENT);
//            createStage.initStyle(StageStyle.TRANSPARENT);

            try {
                Parent loader = FXMLLoader.load(getClass().getResource("/loadingSanta.fxml"));
                Scene scene = new Scene(loader);
                createStage.setScene(scene);
                createStage.setTitle("SANTA");
                createStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public Parent doInBackground() throws Exception {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/Root.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return root;

        }

        public void onPostExecute(Parent result, Exception ex) {
            Scene scene = new Scene(result);
            primaryStage.setScene(scene);
            primaryStage.setTitle("SANTA");
            primaryStage.show();
            progressCallback();
            //createStage.close();
        }

        public  void progressCallback(Object... params) {
            createStage.close();
        }
    }

    /**
     * start the threading of the program start up
     */
    public void start(Stage primaryStage) {
        loadingThread loading = new loadingThread();
        loading.execute();
    }

}


