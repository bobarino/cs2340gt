package com.cs2340gt.nick.app_android.model;
import java.util.List;
import java.util.Arrays;

/**
 * Created by Sean Bills on 2/18/17.
 */

public class WaterReport {
    // the Account creating the water report
    private Account reporter;

    // the date and time the report was created
    private String date_time;

    // the unique ID value of this report
    private int id;

    // a list of all of the possible sources of water for the report
    public static List<String> waterSources = Arrays.asList("Bottled", "Well",
            "Stream", "Lake", "Spring", "Other");

    // a string value indicating the unique source of this report (from the options
    // in the waterSources list)
    private String source;

    // a list of possible waterConditions for the report
    public static List<String> waterCondition = Arrays.asList("Waste", "Treatable - Muddy",
            "Treatable - Clear", "Potable");

    // the actual condition of the water in the report
    // (value comes from list above)
    private String condition;

    // the Location (latitude and longitude) of the water report
    private Location location;


    /**
     * constructor to create a WaterReport object
     * @param _reporter the Account creating the report
     * @param _source the String value associated with the source of the water
     *                (well, spring, bottled, etc.)
     * @param _condition the condition of hte water (potable, muddy, etc.)
     * @param _dateTime the date/time string of when the report was created
     * @param place the Location instance of the report
     */
    public WaterReport(Account _reporter, String _source, String _condition, String _dateTime,
                       Location place) {
        reporter = _reporter;
        setCondition(_condition);
        setSource(_source);
        id = Model.getNextReportId();
        date_time = _dateTime;
        location = place;
    }

    /**
     * method to ge the Account associated with the reporter
     * that created this report
     * @return the Account of the user that created the report
     */
    public Account getReporter() {
        return reporter;
    }

    /**
     * method to set a new Account as the reporter (to be used
     * when updating a report)
     * @param newReporter the new Account reporter
     */
    public void setReporter(Account newReporter) {
        reporter = newReporter;
    }


    /**
     * method to return the ID no. of the curernt report
     * @return the ID no. of the report
     */
    public int getId() {
        return id;
    }

    /**
     * method to change ID value for the report
     * @param newID the new ID value to be assigned to this report
     */
    public void setId(int newID) {
        id = newID;
    }


    /**
     * method to get the date/time instance of this report
     * @return a string representing the date/time data for this report
     */
    public String getDate_time() { return date_time; }

    /**
     * method to change the date/time instance of this report to a new value
     * @param newDateTime the new String date/time value for this report
     */
    public void setDate_time(String newDateTime) { date_time = newDateTime; }


    /**
     * method to return the Location instance associated with this report
     * @return the location object asosciated with the report
     */
    public Location getLocation() { return location; }

    /**
     * method to change the location of the water report
     * @param newLoc the new Location object to be assigned to this report
     */
    public void setLocation(Location newLoc) { location = newLoc; }


    /**
     * method to return the source of this current water report
     * @return
     */
    public String getSource() {
        return source;
    }

    /**
     * method to set the source of the water to a new value
     * @param newSource the new source (bottled, spring, etc.)
     *                  of the WaterReport
     */
    public void setSource(String newSource) {
        if (waterSources.contains(newSource)) {
            source = newSource;
        }
    }

    /**
     * method to return the condition of the current water report
     * @return the condition of the water report in string format
     */
    public String getCondition() {
        return condition;
    }

    /**
     * method to set the condition fo the water report to a new value
     * @param newCond the new condition that you want to update the
     *                WaterReport to be
     */
    public void setCondition(String newCond) {
        if (waterCondition.contains(newCond)) {
            condition = newCond;
        }
    }


    @Override
    public boolean equals(Object o) {
        WaterReport wr = (WaterReport) o;
        return (wr.getReporter().equals(reporter)
                && wr.getCondition().equals(condition)
                && wr.getSource().equals(source) && wr.getId() == id
                && wr.getLocation().equals(location));
    }

    @Override
    public String toString() { return ("Report No.: " + id + " " + reporter.getUsername() + "\n"
            + " - " + condition + " - " + source
            + "\nLocation: " + location); }
}
