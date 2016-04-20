package seng202.group5.santa.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import seng202.group5.santa.data.CrimeRecord;
import java.util.ArrayList;

/**
 * mapController is used to initialise and communicate with a Google Map instance.
 * The Google Map is placed inside a WebView (which itself is inside an anchor pane).
 * The communication is achieved using Javascript.
 * Created by Vincent on 15/09/2015.
 */
public class mapController extends Region {

    private JSObject doc;
    private WebEngine webEngine;
    private boolean ready;

    /**
     * The initial constructor for mapController,
     */
    public mapController(AnchorPane pane) {
        initMap(pane);
        initCommunication();
    }

    /**
     * Initialises the Google Map
     */
    private void initMap(AnchorPane pane) {
        final WebView webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(getClass().getResource("/map.html").toExternalForm());
        // These next 4 lines ensure that the map is resizable and sticks to the anchor pane
        AnchorPane.setLeftAnchor(webView, 0.0);
        AnchorPane.setTopAnchor(webView, 0.0);
        AnchorPane.setRightAnchor(webView, 0.0);
        AnchorPane.setBottomAnchor(webView, 0.0);
        pane.getChildren().add(webView);
        ready = false;
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(final ObservableValue<? extends Worker.State> observableValue,
                                final Worker.State oldState,
                                final Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    ready = true;
                }
            }
        });
    }

    /**
     *  Initialises communication between the javascript in map.html and mapController
     */
    private void initCommunication() {
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(final ObservableValue<? extends Worker.State> observableValue,
                                final Worker.State oldState,
                                final Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    doc = (JSObject) webEngine.executeScript("window");
                    doc.setMember("app", mapController.this);
                }
            }
        });
    }

    /**
     * This function will place a marker on the map for each CrimeRecord in the ArrayList passed as a parameter
     * @param crimeList ArrayList<CrimeRecord>
     */
    public void placeAllMarkers(final ArrayList<CrimeRecord> crimeList) {
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
        {
            public void changed(final ObservableValue<? extends Worker.State> observableValue,
                                final Worker.State oldState,
                                final Worker.State newState)
            {
                if (newState == Worker.State.SUCCEEDED)
                {
                    for (int i = 0; i < crimeList.size(); i++) {
                        // Check that the longitude and latitude are valid and if they are: call the setNewMarkerPosition function
                        if (crimeList.get(i).getLongitude() != 0.0 && crimeList.get(i).getLatitude() != 0.0) {
                            placeNewMarker(crimeList.get(i).getLatitude(), crimeList.get(i).getLongitude(), crimeList.get(i).getPrimaryDescription(), "", infoGenerator(crimeList.get(i)));
                        }
                        // Due to performance limitations on slower computers, the max number of crimes on the map at any one time is set to 400
                        if (i > 400) {
                            break;
                        }
                    }
                }
            }
        });
    }

    /**
     * Generates the content for the marker's info box and formats it.
     * @param crime CrimeRecord which we want the content generated for
     * @return String
     */
    private String infoGenerator(CrimeRecord crime) {
        String s = "";
        s += "<b>Primary: </b>";
        s += crime.getPrimaryDescription() + "<hr>";
        s += "<b>Secondary: </b>";
        s += crime.getSecondaryDescription() + "<hr>";
        s += "<b>Location: </b>";
        s += crime.getLocationDescription() + "<hr>";
        s += "<b>Time: </b>";
        s += crime.getCrimeDate();
        return s;
    }

    /**
     * This function instructs the map to display all markers that have been placed
     */
    public void displayMarkers() {
        invokeJS("displayMarkers()");
    }

    /**
     * This function will take a String parameter and processes it into a javascript call (if the webEngine is ready)
     * @param str The javascript query
     */
    private void invokeJS(final String str) {
        if(ready) {
            doc.eval(str);
        } else {
            webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
            {
                public void changed(final ObservableValue<? extends Worker.State> observableValue,
                                    final Worker.State oldState,
                                    final Worker.State newState)
                {
                    if (newState == Worker.State.SUCCEEDED)
                    {
                        doc.eval(str);
                    }
                }
            });
        }
    }

    /**
     * Places a new marker onto the Google Map
     * @param lat Latitude
     * @param lng Longitude
     * @param title Title
     * @param label Label
     * @param info Info Box content
     */
    private void placeNewMarker(double lat, double lng, String title, String label, String info) {
        String sLat = Double.toString(lat);
        String sLng = Double.toString(lng);
        //invokeJS("placeNewMarker(" + sLat + ", " + sLng + ", '" + title + "', '" + label + "')");
        webEngine.executeScript("placeNewMarker(" + sLat + ", " + sLng + ", '" + title + "', '" + label + "', '" + info + "')");
    }

    /**
     * Centers the map to the specified longitude and latitude
     * @param lat Latitude
     * @param lng Longitude
     */
    public void setMapCenter(double lat, double lng) {
        String sLat = Double.toString(lat);
        String sLng = Double.toString(lng);
        invokeJS("setMapCenter(" + sLat + ", " + sLng + ")");
    }

    /**
     * Switches the Google Map into Satellite view
     */
    public void switchSatellite() {
        invokeJS("switchSatellite()");
    }

    /**
     * Switches the Google Map into roadmap view
     */
    public void switchRoadmap() {
        invokeJS("switchRoadmap()");
    }

    /**
     * Switches the Google Map into hybrid view
     */
    public void switchHybrid() {
        invokeJS("switchHybrid()");
    }

    /**
     * Switches the Google Map into terrain view
     */
    public void switchTerrain() {
        invokeJS("switchTerrain()");
    }
}