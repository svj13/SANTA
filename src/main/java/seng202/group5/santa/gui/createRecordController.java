package seng202.group5.santa.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import seng202.group5.santa.data.CrimeRecord;
import javafx.scene.Node;
import javafx.event.EventHandler;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import seng202.group5.santa.data.Database;
import seng202.group5.santa.data.IUCRParser;

/**
 * createRecordController allows a user to create and add a custom crime record
 * It works with the createRecord.fxml file and acts as a popup window
 * @author group5
 */
public class createRecordController {
    
    @FXML
    private Text header;

    @FXML
    private TextField locationField;

    @FXML
    private TextField latField;

    @FXML
    private TextField longField;

    @FXML
    private TextField blockField;

    @FXML
    private TextField wardField;

    @FXML
    private TextField iucrField;

    @FXML
    private TextField beatField;

    @FXML
    private TextField yField;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField caseField;

    @FXML
    private TextField xField;

    @FXML
    private MenuButton primarySelector;

    @FXML
    private MenuButton secondarySelector;

    @FXML
    private Button btnSubmit;

    @FXML
    private CheckBox arrest;

    @FXML
    private CheckBox domestic;

    @FXML
    private TextField fbiField;

    @FXML
    private MenuButton selectHour;

    @FXML
    private MenuButton selectMinute;

    public RootController mainController;

    private int currentData;

    private ArrayList<String[]> IUCRCodes = IUCRParser.Parsefile();

    private ArrayList<String> primaryTypes = new ArrayList<String>();

    private static CrimeRecord editing = null;

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd 'at' HH:mm");

    private Database database = new Database();

    /**
     * Collects the user's data and creates or edits a crimeRecord from this.
     * Submits the user's crimeRecord to the database and closes the popup window.
     * @param event The submit button is clicked
     */
    @FXML
    public void submitForm(ActionEvent event) {
        ArrayList<String> emptyEssentials = new ArrayList<String>();

        //Prepares new variables from user input
        String caseNumber = caseField.getText();
        if (caseNumber.equals("")){
            emptyEssentials.add("Case Number");
        }

        //Swaps from localDate to Calendar type
        Calendar crimeDate = null;
        try {
            LocalDate ld = dateField.getValue();
            Date theDate = Date.from(ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            crimeDate = Calendar.getInstance();
            crimeDate.setTime(theDate);

            //For setting time, once time picker is implemented
            crimeDate.set(Calendar.HOUR, Integer.parseInt(selectHour.getText()));
            crimeDate.set(Calendar.MINUTE, Integer.parseInt(selectMinute.getText()));
        } catch (Exception dateError) {
            emptyEssentials.add("Date/Time");
        }

        String block = blockField.getText();
        if (block.equals("")){
            emptyEssentials.add("Block");
        }

        String iucr = iucrField.getText();


        String primary = primarySelector.getText();
        if (primary.equals("Select Type")){
            emptyEssentials.add("Primary type");
        }

        String secondary = secondarySelector.getText();
        if (secondary.equals("Select Type")) {
            emptyEssentials.add("Secondary Type");
        }

        String locationDescription = locationField.getText();
        if (locationDescription.equals("")){
            locationDescription = "No description";
        }

        String fbiCd = fbiField.getText();
        if (fbiCd.equals("")){
            fbiCd = "NA";
        }

        //Can't be 'ignored'
        String arrestMade = String.valueOf(arrest.isSelected());
        String isDomestic = String.valueOf(domestic.isSelected());

        //Initialise numerical variables
        int beat;
        int ward;
        int xCoord;
        int yCoord;
        float longitude;
        float latitude;

        try {
            beat = Integer.parseInt(beatField.getText());
        } catch (Exception beatError) {
            beat = 0;
        }

        try {
            ward = Integer.parseInt(wardField.getText());
        } catch (Exception wardError){
            ward = 0;
        }

        try {
            xCoord = Integer.parseInt(xField.getText());
        } catch (Exception xError) {
            xCoord = 0;
        }

        try {
            yCoord = Integer.parseInt(yField.getText());
        } catch (Exception yError) {
            yCoord = 0;
        }

        try {
            longitude = Float.parseFloat(longField.getText());
        } catch (Exception longError) {
            longitude = 0;
        }

        try{
            latitude = Float.parseFloat(latField.getText());
        } catch (Exception latError) {
            latitude = 0;
        }

        if (emptyEssentials.isEmpty()){
            //There's no problems, pass to Database
            CrimeRecord newRecord = new CrimeRecord(caseNumber, crimeDate, block, iucr, primary, secondary, locationDescription, arrestMade,
                    isDomestic, beat, ward, fbiCd, xCoord, yCoord, latitude, longitude);
            ArrayList<CrimeRecord> submitList = new ArrayList<CrimeRecord>();
            submitList.add(newRecord);
            database.addCrimeList(submitList, currentData);

            //Show the new record
            mainController.refresh();
            mainController.displayCrime(newRecord);

            //Close the form
            Node source = (Node) event.getSource();
            Stage thisStage = (Stage) source.getScene().getWindow();
            thisStage.close();
            mainController.showEdit();

        } else {
            //Essential fields missing, cannot continue
            showError("Errors in the following essential fields: " + buildErrorList(emptyEssentials));
        }
    }

    /**
     * Creates a list of 'bad' fields to display to the user
     * @param fields Input fields which need attention
     * @return A textual list of the fields needing attention
     */
    public String buildErrorList(ArrayList<String> fields){
        String list = "\n";
        for(int i = 0; i < fields.size(); i++) {
            if (i != (fields.size() - 1)) {
                list = list + fields.get(i) + ",\n";
            } else {
                list = list + fields.get(i) +". ";
            }
        }
        return list;
    }

    /**
     * Creates the stage/scene, displays everything for creating a record
     * @param rootController the controller this was launched from
     */
    public static void create(RootController rootController) {
        Stage createStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(ErrorMessageController.class.getResource("/createRecord.fxml"));
            Parent createForm = loader.load();
            createRecordController controller = loader.getController();
            controller.setRootController(rootController);
            Scene scene = new Scene(createForm);
            createStage.setScene(scene);
            createStage.setTitle("Create New Record");
            createStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the parent of this window in order to access its functions
     * @param controller The controller that launched this window
     */
    public void setRootController(RootController controller) {
        mainController = controller;
        currentData = mainController.getCurrentDataSet();
    }

    /**
     * Shows an error - is utilised when there is bad input
     * @param error The error message to display
     */
    public void showError (String error) {
        ErrorMessageController.showError(error);
    }

    /**
     * Populate the secondary description selector from the primary selector
     * Called whenever the primary description is changed
     */
    private void setSecondaries() {
        secondarySelector.getItems().remove(0, secondarySelector.getItems().size());
        for(int i = 0; i < IUCRCodes.size(); i++){
            if (IUCRCodes.get(i)[1].equals(primarySelector.getText())) {
                final MenuItem sType = new MenuItem(IUCRCodes.get(i)[2]);
                secondarySelector.getItems().add(sType);
                sType.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        secondarySelector.setText(sType.getText());
                        getIucr(primarySelector.getText(), secondarySelector.getText());
                    }
                });
            }
        }
    }

    /**
     * Sets the IUCR code corresponding to the given descriptions
     * Sets it as UNFOUND if there is no valid code
     * @param primary Primary Crime Description
     * @param secondary Secondary Crime Description
     */
    private void getIucr(String primary, String secondary){
        Boolean found = false;
        int i = 0;
        String code = "UNFOUND";
        while (i < IUCRCodes.size() && !found) {
            String[] current = IUCRCodes.get(i);
            if (current[1].equals(primary) && current[2].equals(secondary)){
                //It matches!
                found = true;
                code = current[0];
            } else {
                //Move on to next code
                i++;
            }
        }
        iucrField.setText(code);
    }

    /**
     * Prepares the frame to be in edit mode
     * @param editingRecord the record to be altered
     * @param rootController the class which launched this one
     */
    public static void edit(final CrimeRecord editingRecord, final RootController rootController){
        Stage createStage = new Stage();
        try {
            editing = editingRecord;
            FXMLLoader loader = new FXMLLoader(ErrorMessageController.class.getResource("/createRecord.fxml"));
            Parent createForm = loader.load();
            createRecordController controller = loader.getController();
            controller.setRootController(rootController);
            Scene scene = new Scene(createForm);
            createStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    rootController.showEdit();
                    clearEdit();
                }
            });
            createStage.setScene(scene);
            createStage.setTitle("Editing Record");
            createStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void clearEdit(){
        editing = null;
    }

    /**
     * Fills in each field with the information from a given record
     * @param editingRecord The record used to populate the data fields
     */
    private void populateFields(CrimeRecord editingRecord){
        header.setText("Editing Record");
        caseField.setText(editingRecord.getCaseNumber());
        LocalDate date = LocalDate.parse(editingRecord.getCrimeDate(), dateFormat);
        dateField.setValue(date);
        selectHour.setText(Integer.toString(editingRecord.getBetterCrimeDate().getTime().getHours()));
        selectMinute.setText(Integer.toString(editingRecord.getBetterCrimeDate().getTime().getMinutes()));
        blockField.setText(editingRecord.getBlock());
        iucrField.setText(editingRecord.getIucr());
        primarySelector.setText(editingRecord.getPrimaryDescription());
        secondarySelector.setText(editingRecord.getSecondaryDescription());
        locationField.setText(editingRecord.getLocationDescription());
        beatField.setText(Integer.toString(editingRecord.getBeat()));
        wardField.setText(Integer.toString(editingRecord.getWard()));
        fbiField.setText(editingRecord.getFbi_cd());
        xField.setText(Integer.toString(editingRecord.getXCoordinate()));
        yField.setText(Integer.toString(editingRecord.getYCoordinate()));
        latField.setText(Float.toString(editingRecord.getLatitude()));
        longField.setText(Float.toString(editingRecord.getLongitude()));
        arrest.setSelected(Boolean.getBoolean(editingRecord.getArrest()));
        domestic.setSelected(Boolean.getBoolean(editingRecord.getDomestic()));
        setSecondaries();

    }


    /**
     * Sets the viewer for the user to create a record
     */
    private void prepareForCreation(){
        header.setText("Create New Record");
        caseField.setText(generateCrimeId());
    }


    /**
     * Generates a unique crime ID
     * @return the unique SANTA ID
     */
    private String generateCrimeId(){
        Boolean validIdMade = false;
        String acceptedId = "";

        while(!validIdMade) {
            String caseId = gen();
            ArrayList<String> ids = database.getDistnct(currentData, "CRIME_ID");
            if (ids.isEmpty()){
                //ID is valid
                acceptedId = caseId;
                validIdMade = true;
            }
        }
        return acceptedId;
    }

    /**
     * Generates a SANTA ID
     * @return a potential ID number
     */
    private String gen(){
        //There is a limit of unique 999,999 records
        Random generator = new Random();
        int idNum = generator.nextInt(1000000);
        String idString = "SANTA" + String.format("%06d", idNum);
        return idString;
    }

    /**
     * Populates the type selectors, as well as the time selectors
     * This removes some hard-coding
     */
    @FXML
    void initialize() {
        //Creates the list of primary crime types to choose from
        for (int i = 0; i < IUCRCodes.size(); i++){
            String current = IUCRCodes.get(i)[1];
            if (!primaryTypes.contains(current)) {
                primaryTypes.add(current);
            }
            Collections.sort(primaryTypes);
        }

        //Creates list of secondary types from selected primary type
        for (int i = 0; i < primaryTypes.size(); i++) {
            final MenuItem type = new MenuItem(primaryTypes.get(i));
            primarySelector.getItems().add(type);
            type.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    primarySelector.setText(type.getText());
                    secondarySelector.setText("Select Type");
                    setSecondaries();
                }
            });
        }

        //Populate hours and minutes selectors
        for (int i = 0; i < 24; i++){
            final MenuItem hour = new MenuItem(Integer.toString(i));
            selectHour.getItems().add(hour);
            hour.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    selectHour.setText(hour.getText());
                }
            });
        }

        for (int i = 0; i < 60; i++){
            final MenuItem minute = new MenuItem(Integer.toString(i));
            selectMinute.getItems().add(minute);
            minute.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    selectMinute.setText(minute.getText());
                }
            });
        }

        if (editing != null) {
            populateFields(editing);
        } else {
            prepareForCreation();
        }
    }
}
