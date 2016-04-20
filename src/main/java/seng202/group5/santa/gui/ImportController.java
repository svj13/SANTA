package seng202.group5.santa.gui;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.group5.santa.data.AsyncTask;
import seng202.group5.santa.data.CrimeRecord;
import seng202.group5.santa.data.Database;
import seng202.group5.santa.data.Parser;
import java.io.File;
import java.util.ArrayList;

/**
 * Manages the importing procedure.
 * @author Group5
 */
public class ImportController {

    public RootController mainController;
    public Database crimeDatabase = new Database();
    private Stage fileBrowserStage;

    /**
     * Gets the file name and sets up the thread
     * @param controller The instance of the root controller that called this method
     */
    public void ImportController (RootController controller) {
        mainController = controller;
        //Creates a file chooser object to select a file to import
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Import");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(fileBrowserStage);
        // not working like it should
        if (selectedFile != null) {
            ImportThread importCrimes = new ImportThread(selectedFile);
            importCrimes.execute();
        }

    }

    /**
     * Threads the parsing and add to the database tasks
     */
    public class ImportThread extends AsyncTask<ArrayList<CrimeRecord>> {
        private File selectedFile;

        /**
         * Shows that the task is being run in the gui
         */
        public  void onPreExecute(){
            mainController.showBackgroundTask("Parsing File");
            mainController.hideButtons();
        }

        /**
         * Runs the data in the back ground
         */
        public  ArrayList<CrimeRecord>  doInBackground() throws Exception{
            String filePath = selectedFile.getAbsolutePath();
            Parser parser = new Parser();
            final ArrayList<CrimeRecord> importedList = parser.Parsefile(filePath);
            publishProgress(importedList);

            //Updates the info in the database
            crimeDatabase.addCrimeList(importedList, mainController.getCurrentDataSet());
            return importedList;
        }

        /**
         * Hide set up the gui after import is done
         */
        public  void  onPostExecute(ArrayList<CrimeRecord>  result, Exception ex) {
            mainController.hideBackgroundTask();
            mainController.setUpTypeFilter();
            mainController.showButtons();
        }

        /**
         * Shows the import crime in the table viewer
         */
        public  void progressCallback(Object... params) {
            mainController.showBackgroundTask("Updating Database ");
            mainController.setTableView((ArrayList<CrimeRecord>) params[0]);
        }

        public ImportThread (File selectedFile) {
            this.selectedFile = selectedFile;
        }
    }
}
