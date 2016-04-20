package seng202.group5.santa.gui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import seng202.group5.santa.data.CrimeRecord;
import seng202.group5.santa.data.Database;
import seng202.group5.santa.data.UserListDatabase;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


/**
 * Controller class for the main GUI panel
 * @author Group5
 */
public class RootController {


    @FXML
    private CheckMenuItem showFilterPanel;

    @FXML
    private CheckMenuItem showRecordPanel;


    @FXML
    private MenuItem export;

    @FXML
    private MenuItem createRecord;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab raw_data_viewer;

    @FXML
    private Tab generate_map_tab;

    @FXML
    private Tab generate_crime_stat_tab;

    @FXML
    private Button btngenMaps;

    @FXML
    private TextField viewIucr;

    @FXML
    private ResourceBundle resources;

    @FXML
    private TableColumn<CrimeRecord, String> arrestCol;

    @FXML
    private Button btnShowMap;

    @FXML
    private Button btnGenerateStatistics;

    @FXML
    private MenuButton mnuFilter;

    @FXML
    private MenuItem btnQuit;

    @FXML
    private MenuItem impFile;

    @FXML
    private AnchorPane map_tab;

    @FXML
    private TableColumn<CrimeRecord, String> caseNumberCol;

    @FXML
    private TableColumn<CrimeRecord, String> locationCol;

    @FXML
    private TableColumn<CrimeRecord, String> primaryCol;

    @FXML
    private TableColumn<CrimeRecord, String> secondaryCol;

    @FXML
    private TableView<CrimeRecord> tableView;

    @FXML
    private TableColumn<CrimeRecord, String> dateCol;

    @FXML
    private Button generateMap;

    @FXML
    private Button btnEditRecord;

    @FXML
    private VBox typeBox;

    @FXML
    private Button btnfilter;

    @FXML
    private CheckBox arrestedBox;

    @FXML
    private  CheckBox domesticBox;

    @FXML
    private TextField viewCase;

    @FXML
    private TextField viewDate;

    @FXML
    private TextField viewBlock;

    @FXML
    private CheckBox viewArrest;

    @FXML
    private CheckBox viewDomestic;

    @FXML
    private TextField viewBeat;

    @FXML
    private TextField viewWard;

    @FXML
    private TextField viewFbi;

    @FXML
    private TextField viewX;

    @FXML
    private TextField viewY;

    @FXML
    private TextField viewLat;

    @FXML
    private TextField viewLong;

    @FXML
    private TextField viewPrimary;

    @FXML
    private TextField viewSecondary;

    @FXML
    private TextField viewLocation;

    @FXML
    private Text header;

    @FXML
    private DatePicker dateFieldFrom;

    @FXML
    private DatePicker dateFieldTo;

    @FXML
    private  Button btnSearch;

    @FXML
    private  TextField txfSearch;

    @FXML
    private ChoiceBox UserListBox;

    @FXML
    private  CheckBox cboxAll;

    @FXML
    private AnchorPane filterBar;

    @FXML
    private ProgressIndicator progressWheel;

    @FXML
    private Label currentTasklb;

    @FXML
    private HBox progressBox;

    @FXML
    private Button btnMinimizeFilter;

    @FXML
    private Button btnMinimizeDataViewer;

    @FXML
    private AnchorPane dataViewer;

    @FXML
    private Label timeDiff;

    @FXML
    private Label distance;

    @FXML
    private Button btnAddDataSet;

    @FXML
    private Button btnDeleteDataSet;

    @FXML
    private Button btnDeleteRecord;

    @FXML
    private MenuButton fileMenu;

    @FXML
    private MenuItem userManual;

    // When true, indicates that the map should be reloaded
    // For example when a new filter is applied, updateMap will become true
    private boolean updateMap = false;

    // This CrimeRecord variable is used to keep track of the currently selected crime
    // It is used by generateMapForSingleCrime so that it knows which crime to display
    private CrimeRecord currentlySelectedCrime = null;

    // This ArrayList is used to keep track of the currently displayed crimes on the screen
    // It is used by the generateMapForMultipleCrimes function
    private ArrayList<CrimeRecord> currentlyDisplayedCrimes = null;

    private Database database = new Database();
    private UserListDatabase listDatabase = new UserListDatabase();
    private ArrayList<String> filterSettings = new ArrayList<String>();
    private ArrayList<CheckBox> checkBoxList = new ArrayList<CheckBox>();
    private Integer currentUserList = 0;

    public boolean inEdit = false;

    /**
     * Searches for records related to user's terms
     de* @param event search button clicked
     */
    public void search(ActionEvent event) {
        filterSettings.clear();
        String temp = txfSearch.getText();
        String text = temp.replaceAll("[-+.^():,']", "");
        setTableView(database.queryDatabase("SELECT * FROM CRIMETABLE WHERE ((LIST_ID = " + currentUserList + ") AND ((CRIME_ID like '%" + text + "%')" +
                " OR (PRIMARY_DESCRIPTION like '%" + text + "%') OR (SECONDARY_DESCRIPTION like '%" + text + "%')));"));
        updateMap = true;
    }

    /**
     * returns the data set value of the current selected data set
     */
    public Integer getCurrentDataSet () {
        return this.currentUserList;
    }

    /**
     * returns the name of the data set that is current selected
     */
    public String getDataSetName () {
        return UserListBox.getValue().toString();
    }

    /**
     * hides the filter bar
     */
    public void  minimizeFilter (){
        if (filterBar.getMaxWidth() == 0) {
            filterBar.setMaxWidth(250);
            filterBar.setMinWidth(250);
            showFilterPanel.setSelected(true);
        } else {
            filterBar.setMaxWidth(0);
            filterBar.setMinWidth(0);
            showFilterPanel.setSelected(false);
        }
    }

    /**
     * shows the filter bar
     */
    public void  minimizeDataViewer (){
        if (dataViewer.getMaxWidth() == 0) {
            dataViewer.setMaxWidth(400);
            dataViewer.setMinWidth(400);
            //btnMinimizeDataViewer.setText("Hide Data Viewer");
        } else {
            dataViewer.setMaxWidth(0);
            dataViewer.setMinWidth(0);
            //btnMinimizeDataViewer.setText("Show Data Viewer");
        }
    }

    /**
     * Creates the type filter dynamically
     */
    public void setUpTypeFilter() {
        // gets the Distnct elements of the primary desriciption in the currently selected user list
        ArrayList<String> types = database.getDistnct(currentUserList, "PRIMARY_DESCRIPTION");
        cboxAll.setSelected(false);
        checkBoxList.clear();
        typeBox.getChildren().clear();
        // makes a checkBox for distnct primary description
        for (int i = 0; i < types.size(); i++) {
            final CheckBox tempbox = new CheckBox(types.get(i).toLowerCase());
            tempbox.selectedProperty().addListener(new ChangeListener() {
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if (cboxAll.selectedProperty().getValue().equals(true)) {
                        if (tempbox.selectedProperty().getValue().equals(false)) {
                            cboxAll.setSelected(false);
                        }
                    }
                }
            });
            checkBoxList.add(tempbox);
        }
        typeBox.getChildren().addAll(checkBoxList);
    }

    /**
     * hide buttons that can't be used because of background threads
     */
    public void hideButtons(){
        if (showFilterPanel.isSelected() == true) {
            minimizeFilter();
        }
        showFilterPanel.setDisable(true);
        btnAddDataSet.setDisable(true);
        btnDeleteDataSet.setDisable(true);
        btnAddDataSet.setDisable(true);
        UserListBox.setDisable(true);
        btnDeleteRecord.setDisable(true);
        btnEditRecord.setDisable(true);
        btnGenerateStatistics.setDisable(true);
        fileMenu.setDisable(true);
    }
    /**
     * shows buttons that can now be used after background threads have finished
     */
    public void showButtons(){
        minimizeFilter();
        showFilterPanel.setDisable(false);
        btnAddDataSet.setDisable(false);
        btnDeleteDataSet.setDisable(false);
        btnAddDataSet.setDisable(false);
        UserListBox.setDisable(false);
        btnDeleteRecord.setDisable(false);
        btnEditRecord.setDisable(false);
        btnGenerateStatistics.setDisable(false);
        fileMenu.setDisable(false);
    }


    /**
     * deletes the record that is currently select in the full data viewer
     */
    public void deleteRecord (ActionEvent event) {
        if (currentlySelectedCrime == null) {
            showError("There is no record selected");
        } else {
            database.deleteRecord(currentUserList, currentlySelectedCrime.getCaseNumber());
            filter();
            clearViewer();
        }
    }

    public void clearViewer() {
        TextField[] clearTheseFields = {viewCase, viewDate, viewPrimary, viewSecondary, viewLocation, viewBeat, viewWard,
                                        viewIucr, viewBlock, viewX, viewY, viewLat, viewLong, viewFbi};
        for (TextField clearTheseField : clearTheseFields) {
            clearTheseField.setText("");
        }

        viewDomestic.setSelected(false);
        viewArrest.setSelected(false);
        timeDiff.setText("Na");
        distance.setText("Na");
    }
    /**
     * Selects all filtering checkboxes and sets them to true or false
     * @param event select all clicked
     */
    public void selectAll(ActionEvent event) {
       if (cboxAll.isSelected()) {
           for (CheckBox aCheckBoxList : checkBoxList) {
               aCheckBoxList.setSelected(true);
           }
       } else {
           for (CheckBox aCheckBoxList : checkBoxList) {
               aCheckBoxList.setSelected(false);
           }
       }
   }

    /**
     * runs the Import controller
     * @param event import button selected
     */
    public void importFile(ActionEvent event) {
        ImportController importer = new ImportController();
        importer.ImportController(this);
    }

    /**
     * set the table in the raw data view to a given list of crimes
     * @param tableList the list of crime to be shown in the table view
     */
    void setTableView  (ArrayList<CrimeRecord> tableList)  {
        currentlyDisplayedCrimes = tableList;
        // stores the current select crime record for use later
        tableView.getItems().setAll(FXCollections.observableArrayList(tableList));
    }

    /**
     * show a description of the task that is running in the background
     * @param task a textual description of the task that is running in the background
     */
    public void showBackgroundTask (String task) {
        progressBox.setVisible(true);
        progressWheel.setProgress(-1.0f);
        currentTasklb.setText(task);
    }


    /**
     * hides the description of the task that is running in the background
     */
    void hideBackgroundTask() {
        progressBox.setVisible(false);
    }

    /**
     * Creates user lists and set up drop down user list box
     */
    public void setUpUserList() {
        ArrayList<String> tempStrings = listDatabase.getDistinct("DISPLAYTEXT");
        UserListBox.setItems(FXCollections.observableArrayList(tempStrings));
        UserListBox.getSelectionModel().select(0);
        for (int i = 0; i < tempStrings.size(); i++) {
            if (tempStrings.get(i).equals(UserListBox.getValue())) {
                currentUserList = listDatabase.getListNumber(tempStrings.get(i));
            }
        }
    }

    /**
     * changes the currently displayed data set
     * @return the name of the data set that to change to
     */
    public void setUserListBoxName (String name) {
        for (int i = 0; i < UserListBox.getItems().size() ; i++) {
            if (UserListBox.getItems().get(i).equals(name)){
                UserListBox.getSelectionModel().select(i);
            }
        }
    }

    /**
     * Gets the selected user list
     * @return the list which the user has selected
     */
    private  int getSelectedUserList () {
        String tempString =  listDatabase.getDistinct("DISPLAYTEXT").get(UserListBox.getSelectionModel().getSelectedIndex());
        return listDatabase.getListNumber(tempString);
    }

    /**
     * Adds a list of crime records to the database
     * @param toAddList list which records are to be added to
     */
    public void addToDatabse (ArrayList<CrimeRecord> toAddList) {
        database.addCrimeList(toAddList, currentUserList);
        setUpTypeFilter();
        filter();
    }


    /**
     * Launches a frame for editing
     * @param event edit record button clicked
     */
    public void editRecord (ActionEvent event) {
        btnEditRecord.setVisible(false);
        inEdit = true;
        createRecordController.edit(currentlySelectedCrime, this);

    }

    /**
     * Closes the program
     * @param event exit button's clicked
     */
    public void quitProgram(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Gets the selected filtering options and displays results in tableview
     */
    public void filter() {
        filterSettings.clear();
        // filter by time if selected
        if (dateFieldTo.getValue() != null && dateFieldFrom.getValue() != null) {
            LocalDate ld = dateFieldFrom.getValue();
            Date theDate = Date.from(ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            long From = theDate.getTime() / 1000;
            ld = dateFieldTo.getValue();
            theDate = Date.from(ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            long To = theDate.getTime() / 1000;
            if (From > To) {
                showError("Cant have from date before to date");
            } else {
                // adds the date filtering parameters to the filter list
                filterSettings.add("DATE,BETWEEN," + From + "," + To );
            }
        }
        // check to see if arrested need to be filter and adds it to the filter list
        if (arrestedBox.isSelected()) {
            filterSettings.add("ARREST,=,true");
        }
        // check to see if domestics need to be filter and adds it to the filter list
        if (domesticBox.isSelected()) {
            filterSettings.add("DOMESTIC,=,true");
        }
        // check each of the type boxes to see if they need to be filtered and adds them to the filter list
        if (! cboxAll.isSelected()) {
            for (int i = 0; i < checkBoxList.size(); i++) {
                if (checkBoxList.get(i).isSelected()) {
                    filterSettings.add("PRIMARY_DESCRIPTION,=," + checkBoxList.get(i).getText().toUpperCase());
                }
            }
        }
        // displays the crime records filtered by the database in the table
        refresh();
        updateMap = true;
    }

    /**
     * runs the delete data set controller
     */
    @FXML
    public void deleteUserList() {
        DeleteDataController.showDeleteMessage(this);
    }

    /**
     * Launches a new frame for the user to input new record data
     */
    public void createRecord() {
        createRecordController.create(this);
    }

    /**
     * Simply reloads the data in the table view to stay up-to-date
     */
    public void refresh() {
        setTableView(database.filterCrimes(currentUserList, filterSettings));
    }

    /**
     * Launches a new frame that displays an error to the user
     */
    public void showError (String error) {
        ErrorMessageController.showError(error);
    }

    /**
     *  Create a google map instance and add a marker for only the current selected record
     */
    public void generateMapForSingleCrime() {
        if (currentlySelectedCrime == null) {
            showError("There is no record selected");
        } else if (currentlySelectedCrime.getLatitude() == 0.0 || currentlySelectedCrime.getLongitude() == 0.0) {
            showError("This crime has no location data");
        } else {
            updateMap = false;
            mapController aMapController = new mapController(map_tab);
            ArrayList<CrimeRecord> crimes = new ArrayList<CrimeRecord>();
            crimes.add(currentlySelectedCrime);
            aMapController.placeAllMarkers(crimes);
            aMapController.displayMarkers();
            aMapController.setMapCenter(currentlySelectedCrime.getLatitude(), currentlySelectedCrime.getLongitude());
            // These next two lines make the tab pane automatically switch tabs into the map
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(generate_map_tab);
        }
    }

    /**
     *  Generate a google map instance which has markers for every currently displayed crime
     */
    public void generateMapForMultipleCrimes() {
        mapController aMapController = new mapController(map_tab);
        aMapController.placeAllMarkers(currentlyDisplayedCrimes);
        aMapController.displayMarkers();
    }

    /**
     * Generates a form which contains some basic statistics about the Crimes in the database.
     * @param event Button Pressed
     */
    @FXML
    void generateStatistics (ActionEvent event){
        //Launch the new record frame on top
        Stage createStage = new Stage();
        if (currentlyDisplayedCrimes.size() == 0)
        {
            showError("There are no crimes selected");
        }
        else
        {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatisticsForm.fxml"));
                Parent createForm = loader.load();
                Scene scene = new Scene(createForm);
                createStage.setScene(scene);
                createStage.setTitle("Data Analysis");
                createStage.show();
                StatisticsFormController c = loader.getController();

                // Passes the controller the currently selected data set, for use in generating statistics.
                String unfinishedQuery = database.getFilterQuery(currentUserList, this.filterSettings);
                int unfinishedQueryLen = unfinishedQuery.length();
                String query = unfinishedQuery.substring(31, unfinishedQueryLen-1);
                c.setListStats(query);
                //c.setCurrentFields(query, database); // Data set without the SQL functions (SELECT FROM WHERE etc.)

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * runs the data name controller
     */
    @FXML
    public void addUserList () {
        DataNameController.getName(this);
    }

    /**
     * Returns the user's filtering choices
     * @return the selection of filters
     */
    public ArrayList<String> getFilterSettings()
    {
        return filterSettings;
    }

    /**
     * Shows a single crime in the in-depth viewer
     * @param newValue the crime to be displayed
     */
    public void displayCrime (CrimeRecord newValue) {
        if (newValue != null) {
            CrimeRecord lastCrime;
            viewCase.setText(newValue.getCaseNumber());
            viewDate.setText(newValue.getCrimeDate());
            viewBlock.setText(newValue.getBlock());
            viewArrest.setSelected(Boolean.parseBoolean(newValue.getArrest()));
            viewIucr.setText(newValue.getIucr());
            viewDomestic.setSelected(Boolean.parseBoolean(newValue.getDomestic()));
            viewBeat.setText(newValue.getBeat().toString());
            viewWard.setText(newValue.getWard().toString());
            viewFbi.setText(newValue.getFbi_cd());
            viewX.setText(newValue.getXCoordinate().toString());
            viewY.setText(newValue.getYCoordinate().toString());
            viewLat.setText(newValue.getLatitude().toString());
            viewLong.setText(newValue.getLongitude().toString());
            viewPrimary.setText(newValue.getPrimaryDescription());
            viewSecondary.setText(newValue.getSecondaryDescription());
            viewLocation.setText(newValue.getLocationDescription());
            if (tableView.getSelectionModel().getFocusedIndex() != 0) {
                lastCrime = tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex() - 1);
                timeDiff.setText(newValue.getTimeDifference(lastCrime.getBetterCrimeDate()) + " m");
                if (newValue.getLongitude() != 0.0 && newValue.getLatitude() != 0.0 && lastCrime.getLongitude() != 0.0 && lastCrime.getLatitude() != 0.0) {
                    long number = Math.round(newValue.getDistanceDifference(lastCrime.getLatitude(), lastCrime.getLongitude()) * 100);
                    number = number / 100;
                    distance.setText(number + " km");
                } else {
                    distance.setText("Na");
                }
            } else {
                timeDiff.setText("Na");
                distance.setText("Na");
            }
        }
        // This next line updates the currentlySelectedCrime to the new value
        currentlySelectedCrime = newValue;

    }

    /**
     * Launches the Help Manual as a small text frame
     * @param event The user clicks on Help Manual in the GUI
     */
    @FXML
    public void openUserManual(ActionEvent event) {
        //Launch a stage

        Stage createStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(RootController.class.getResource("/HelpManual.fxml"));
            Parent createForm = loader.load();
            Scene scene = new Scene(createForm);
            createStage.setScene(scene);
            createStage.setTitle("User Help Manual");
            createStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up tableview and a listener for the dataviewer
     * Opens the database for viewing
     */
    public void initialize () {
        // opens the database and looks for crime records to display on start up
        database.makeTable();
        listDatabase.makeTable();
        if (listDatabase.getDistinct("DISPLAYTEXT").size() == 0) {
            listDatabase.addCrimeListId("Default Data List");
        }
        // sets up filtering data table and user list
        setUpUserList();
        setUpTypeFilter();
        filter();

        //Sets everything up for table view to work
        caseNumberCol.setCellValueFactory(new PropertyValueFactory<CrimeRecord, String>("caseNumber"));
        dateCol.setCellValueFactory(new PropertyValueFactory<CrimeRecord, String>("crimeDate"));
        primaryCol.setCellValueFactory(new PropertyValueFactory<CrimeRecord, String>("primaryDescription"));
        secondaryCol.setCellValueFactory(new PropertyValueFactory<CrimeRecord, String>("secondaryDescription"));
        locationCol.setCellValueFactory(new PropertyValueFactory<CrimeRecord, String>("locationDescription"));
        arrestCol.setCellValueFactory(new PropertyValueFactory<CrimeRecord, String>("arrest"));

        //Listener for showing crimes in depth
        final RootController mainController = this;
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CrimeRecord>() {
            public void changed(ObservableValue<? extends CrimeRecord> observable, CrimeRecord oldValue, CrimeRecord newValue) {
                if (!inEdit) {
                    displayCrime(newValue);
                    btnEditRecord.setVisible(true);
                    btnDeleteRecord.setVisible(true);
                }

            }
        });

        // This listener is used so that when the map tab is switched to, and it is time to update the map, a new map will be generated
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
           public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
               if (updateMap && t1.getId().equals("generate_map_tab")) {
                   generateMapForMultipleCrimes();
                   updateMap = false;
               }
           }
        }
        );
        // create a listener for the data set box
        UserListBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                ArrayList<String> tempStrings = listDatabase.getDistinct("DISPLAYTEXT");
                for (int i = 0; i < tempStrings.size(); i++) {
                    if (tempStrings.get(i).equals(UserListBox.getValue())) {
                        currentUserList = listDatabase.getListNumber(tempStrings.get(i));
                    }
                }
                setUpTypeFilter();
                filter();
            }
        });
        if (currentlySelectedCrime == null) {
            btnEditRecord.setVisible(false);
            btnDeleteRecord.setVisible(false);
        }
    }

    public void showEdit(){
        btnEditRecord.setVisible(true);
        btnDeleteRecord.setVisible(true);
        inEdit = false;
    }


    public ArrayList<CrimeRecord> getCurrentlyDisplayedCrimes() {
        return this.currentlyDisplayedCrimes;
    }

    public void export(){
        ExportController export = new ExportController();
        export.ExportController(this);
    }

    //Stage for file browser
    private Stage fileBrowserStage;

    /**
     * Creates a place to display the file browser
     * @param stage a container for the file browser
     */

    public void setStage(Stage stage) {
        fileBrowserStage = stage;
    }
}