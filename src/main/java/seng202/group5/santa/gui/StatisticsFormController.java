package seng202.group5.santa.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import seng202.group5.santa.data.CrimeStatistics;
import seng202.group5.santa.data.CrimeTuple;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StatisticsFormController {

    CrimeStatistics listStats;
    Boolean overviewSet = false; // Boolean showing whether the Overview tab has been setup yet
    Boolean typeSet = false; // Boolean showing whether the Type Breakdown tab has been setup yet
    Boolean trendSet = false; // Boolean showing whether the Trend tab has been setup yet

    @FXML
    private TabPane statsTabs;

    @FXML
    private Tab overviewTab;

    @FXML
    private Tab trendTab;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea breakdownTop;

    @FXML
    private TextArea breakdownDescription;

    @FXML
    private Tab typeBreakdownTab;

    @FXML
    private BarChart<String, Integer> breakdownBar;

    @FXML
    private TextArea breakdownTotals;

    @FXML
    private PieChart breakdownPie;

    @FXML
    private LineChart<String, Integer> trendGraph;

    @FXML
    private Text tutDesc;

    @FXML
    private Text overviewDesc;

    @FXML
    private Text typeDesc;

    @FXML
    private Text trendDesc;

    @FXML
    private Text overviewCats;

    @FXML
    private Text overviewData;

    /**
     * Populates the fields and graphs in the Type Breakdown tab.
     */
    private void setupTypeBreakdownTab() {
        ArrayList<CrimeTuple> typeStats = listStats.getTypeStats();
        String totalCrimes = listStats.getTotalCrimes();
        setBreakdownBar(typeStats, Integer.valueOf(totalCrimes));
        setBreakdownPie(typeStats, Integer.valueOf(totalCrimes));
        setBreakdownTop(typeStats);
        setBreakdownTotals(typeStats);
        breakdownDescription.setText("To the right are pie and bar graphs which show a comparison of the " +
                "totals of the various crime types. For these, if the crime types are beneath a certain " +
                "proportion of the total crimes (5%), they are classified as \"Other Crimes\".\n" +
                "Beneath is given both a breakdown of the crimes by type and " +
                "an overview of the most prevalent crime(s) in the dataset. \n");
        breakdownDescription.setFont(Font.font("Arial", 14));
    }

    /**
     * Populates the fields in the Overview Tab
     */
    private void setupOverviewTab()
    {
        overviewCats.setText("\n" +
                "\n" +
                "Total Crimes:\n" +
                "\n" +
                "Total Arrests:\n" +
                "\n" +
                "Total Domestic Incidents:\n" +
                "\n" +
                "Total Days (Range in selected data):\n" +
                "\n" +
                "Frequency of crimes (per day):\n" +
                "\n" +
                "First Day in Range:\n" +
                "\n" +
                "Last Day in Range:");
        overviewData.setText("\n" +
                "\n" +
                listStats.getTotalCrimes() +"\n" +
                "\n" +
                listStats.getTotalArrests() + "\n" +
                "\n" +
                listStats.getTotalDomestics() + "\n" +
                "\n" +
                listStats.getTotalDays() + "\n" +
                "\n" +
                listStats.getTotalFrequency()+"\n" +
                "\n" +
                listStats.getFirstDay()+"\n" +
                "\n" +
                listStats.getLastDay());
    }

    /**
     * Obtains the data for and populates the Type Breakdown Bar graph.
     * Groups crimes together if they are beneath (~5%) of total crimes.
     * @param stats ArrayList of crime statistics
     * @param totalCrimes The total number of crimes committed in dataset
     */
    private void setBreakdownBar(ArrayList<CrimeTuple> stats, int totalCrimes)
    {
        int lowCrimes = 0;
        XYChart.Series barSeries = new XYChart.Series();
        for (int i = 0; i < stats.size(); i++) {
            if (stats.get(i).getCount() != 0) {
                if (stats.get(i).getCount() >= (totalCrimes / 20))
                {
                    barSeries.getData().add(new XYChart.Data<String, Integer>(stats.get(i).getName(), stats.get(i).getCount()));
                }
                else
                {
                    lowCrimes += stats.get(i).getCount();
                }
            }
        }
        XYChart.Data d = new XYChart.Data<String, Integer>("OTHER CRIMES", lowCrimes);
        barSeries.getData().add(d);
        breakdownBar.getData().add(barSeries);
    }

    /**
     * Obtains the data for and populates the Type Breakdown Pie graph.
     * Groups crimes together if they are beneath (~5%) of total crimes.
     * @param stats ArrayList of crime statistics
     * @param totalCrimes Total number of crimes committed in dataset
     */
    private void setBreakdownPie(ArrayList<CrimeTuple> stats, int totalCrimes)
    {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int lowCrimes = 0;
        for (int i = 0; i < stats.size(); i++) {
            if (stats.get(i).getCount() != 0) {
                if (stats.get(i).getCount() >= (totalCrimes / 20))
                {
                    PieChart.Data d = new PieChart.Data(stats.get(i).getName(), stats.get(i).getCount());
                    pieChartData.add(d);
                }
                else
                {
                    lowCrimes += stats.get(i).getCount();
                }
            }
        }
        PieChart.Data d = new PieChart.Data("OTHER CRIMES", lowCrimes);
        pieChartData.add(d);
        breakdownPie.setData(pieChartData);
    }

    /**
     * Formats and displays statistics for different crime types.
     * @param stats ArrayList of crime statistics
     */
    private void setBreakdownTotals(ArrayList<CrimeTuple> stats)
    {
        int datalen = stats.size();
        String toAdd = "";
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < datalen; i++) {
            if (stats.get(i).getCount() != Integer.valueOf(0)) {
                toAdd += ("" + stats.get(i).getName() + ": \n");
                toAdd += ("    Total Incidences: " + Integer.toString(stats.get(i).getCount()) + "\n");
                toAdd += ("    Total Frequency (per day): " + String.valueOf(df.format(stats.get(i).getFrequency())) + "\n");
                toAdd += ("    Offenders Arrested (%): " + String.valueOf(df.format(stats.get(i).getPcArrests())) + "\n\n");
            }
        }
        breakdownTotals.setText(toAdd);
    }

    /**
     * Formats and displays information on (up to) the top three most prevalent crimes in dataset.
     * @param stats ArrayList of crime statistics
     */
    private void setBreakdownTop(ArrayList<CrimeTuple> stats)
    {
        String toAdd = "";
        CrimeTuple one = new CrimeTuple("",0,0,0);
        CrimeTuple two = new CrimeTuple("",0,0,0);
        CrimeTuple three = new CrimeTuple("",0,0,0);
        for (int i = 0; i < stats.size(); i++) {
            if (one == null) {
                one = stats.get(i);
            } else if (stats.get(i).getCount() > one.getCount()) {
                three = two;
                two = one;
                one = stats.get(i);
            } else {
                if ((two == null) && (stats.get(i).getCount() != Integer.valueOf(0))) {
                    two = stats.get(i);
                } else if (stats.get(i).getCount() > two.getCount()) {
                    three = two;
                    two = stats.get(i);
                } else {
                    if (three == null && stats.get(i).getCount() != Integer.valueOf(0)) {
                        three = stats.get(i);
                    } else if (stats.get(i).getCount() > three.getCount()) {
                        three = stats.get(i);
                    }
                }
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        if (one != null) {
            toAdd += ("1. " + one.getName() + ": \n");
            toAdd += ("    Total Incidences: " + Integer.toString(one.getCount()) + "\n");
            toAdd += ("    Total Frequency (per day): " + String.valueOf(df.format(one.getFrequency())) + "\n");
            toAdd += ("    Offenders Arrested (%): " + String.valueOf(df.format(one.getPcArrests())));
        }
        if (two.getCount() != Integer.valueOf(0)) {
            toAdd += ("\n\n2. " + two.getName() + ": \n");
            toAdd += ("    Total Incidences: " + Integer.toString(two.getCount()) + "\n");
            toAdd += ("    Total Frequency (per day): " + String.valueOf(df.format(two.getFrequency())) + "\n");
            toAdd += ("    Offenders Arrested (%): " + String.valueOf(df.format(two.getPcArrests())));
        }
        if (three.getCount() != Integer.valueOf(0)) {
            toAdd += ("\n\n3. " + three.getName() + ": \n");
            toAdd += ("    Total Incidences: " + Integer.toString(three.getCount()) + "\n");
            toAdd += ("    Total Frequency (per day): " + String.valueOf(df.format(three.getFrequency())) + "\n");
            toAdd += ("    Offenders Arrested (%): " + String.valueOf(df.format(three.getPcArrests())));
        }
        breakdownTop.setText(toAdd);
    }

    /**
     * Used by RootController to pass currently selected filters to StatisticsFormController,
     * which can then be used to generate statistics for the dataset.
     * @param query Query containing currently selected filters, for use in generating statistics
     */
    public void setListStats(String query)
    {
        listStats = new CrimeStatistics(query);
    }

    /**
     * Obtains the data required for populating the trend graph, then populates the graph.
     */
    private void setTrendGraph()
    {

        ObservableList<XYChart.Data<String, Integer>> lineData = FXCollections.observableArrayList();
        ArrayList<CrimeTuple> stats = listStats.getTotalsOverTime();
        XYChart.Series<String, Integer> lineSeries = new XYChart.Series<String, Integer>();
        for (int i = 0; i < 10; i++)
        {
            lineData.add(new XYChart.Data<String, Integer>(stats.get(i).getName(), stats.get(i).getCount()));
        }
        lineSeries.setData(lineData);
        trendGraph.getData().add(lineSeries);
    }

    /**
     * Sets up the trend tab for viewing.
     */
    private void setupTrendTab()
    {
        setTrendGraph();
    }

    @FXML
    void initialize() {
        // Runs when the Analysis pane launches, sets event handlers for changing tabs, and
        // Sets the text for the welcome screen for the analysis window.
        tutDesc.setText("\nThis window takes the chosen data set, and returns a list of statistics about it. All " +
                "statistics are based on the current list with applied filters.\n\n" +
                "To view any of the statistics about the crimes contained in the currently selected list, " +
                "first click on the tab containing the information that you desire, and then " +
                "please wait patiently as your statistics are loaded. \n" +
                "The data that may be viewed is as follows:\n");
        tutDesc.setFont(Font.font("Arial", 14));
        overviewDesc.setText("Overview:\n\n" +
                "This tab contains an overview of the data contained within the current data selection");
        overviewDesc.setFont(Font.font("Arial", 14));
        typeDesc.setText("Type Breakdown:\n\n" +
                "This tab contains an overview of the crimes by distinguishing type, ie. Primary Description.");
        typeDesc.setFont(Font.font("Arial", 14));
        trendDesc.setText("Trend Overview:\n\n" +
                "This tab contains an overview of the crimes over time, " +
                "broken into ten equal periods");
        trendDesc.setFont(Font.font("Arial", 14));
        //Event handler for generating overview stats -> Only generates stats when tab is selected
        overviewTab.setOnSelectionChanged(new EventHandler<Event>() {
            public void handle(Event event) {
                if (overviewTab.isSelected() && !overviewSet) {
                    overviewSet = true;
                    setupOverviewTab();
                }
            }
        });
        //Event handler for generating Type Breakdown stats -> Only generates stats when tab is selected
        typeBreakdownTab.setOnSelectionChanged(new EventHandler<Event>() {
            public void handle(Event event) {
                if (typeBreakdownTab.isSelected() && !typeSet)
                {
                    typeSet = true;
                    setupTypeBreakdownTab();
                }
            }
        });
        //Event handler for generating Trend stats -> Only generates stats when tab is selected.
        trendTab.setOnSelectionChanged(new EventHandler<Event>() {
            public void handle(Event event) {
                if (trendTab.isSelected() && !trendSet)
                {
                    trendSet = true;
                    setupTrendTab();
                }
            }
        });
    }
}