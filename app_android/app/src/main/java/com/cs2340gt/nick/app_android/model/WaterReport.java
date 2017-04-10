package com.cs2340gt.nick.app_android.model;
import java.util.List;
import java.util.Arrays;

/**
 * main class outline for all Water Reports
 * produced by Sean Bills on 2/18/17.
 */

public class WaterReport {

    private static int nextNo = 0;

    // the user who submitted this report
    private Account reporter;

    // the date and time the report was created
    private String dateTime;

    // the unique ID value of this report
    private int id;

    // a list of all of the possible sources of water for the report
    public static final List<String> waterSources = Arrays.asList("Bottled", "Well",
            "Stream", "Lake", "Spring", "Other");

    // a string value indicating the unique source of this report (from the options
    // in the waterSources list)
    private String source;

    // a list of possible waterConditions for the report
    public static final List<String> waterCondition = Arrays.asList("Waste", "Treatable - Muddy",
            "Treatable - Clear", "Potable");

    // the actual condition of the water in the report
    // (value comes from list above)
    private String condition;

    // the Location (latitude and longitude) of the water report
    private Location location;

    public WaterReport() {
        // default constructor used only for Firebase database updates
    }

    /**
     * Constructor used more often, since the ID is not known until construction.
     * @param reporter the Account creating the report
     * @param source the String value associated with the source of the water
     *                (well, spring, bottled, etc.)
     * @param condition the condition of hte water (potable, muddy, etc.)
     * @param dateTime the date/time string of when the report was created
     * @param location the Location instance of the report
     */
    public WaterReport(Account reporter, String source, String condition,
                       String dateTime, Location location) {
        this(nextNo++, reporter, source, condition, dateTime, location);
    }

    /**
     * constructor to create a WaterReport object
     * @param reporter the Account creating the report
     * @param source the String value associated with the source of the water
     *                (well, spring, bottled, etc.)
     * @param condition the condition of hte water (potable, muddy, etc.)
     * @param dateTime the date/time string of when the report was created
     * @param location the Location instance of the report
     */
    public WaterReport(int id, Account reporter, String source, String condition, String dateTime,
                       Location location) {
        this.id = id;
        this.reporter = reporter;
        this.source = source;
        this.condition = condition;
        this.dateTime = dateTime;
        this.location = location;
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
     * @param reporter the new Account reporter
     */
    public void setReporter(Account reporter) {
        this.reporter = reporter;
    }


    /**
     * method to return the ID no. of the current report
     * @return the ID no. of the report
     */
    public int getId() {
        return id;
    }

    /**
     * method to change ID value for the report
     * @param id the new ID value to be assigned to this report
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * method to get the date/time instance of this report
     * @return a string representing the date/time data for this report
     */
    public String getDateTime() { return dateTime; }

    /**
     * method to change the date/time instance of this report to a new value
     * @param dateTime the new String date/time value for this report
     */
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }


    /**
     * method to return the Location instance associated with this report
     * @return the location object associated with the report
     */
    public Location getLocation() { return location; }

    /**
     * method to change the location of the water report
     * @param location the new Location object to be assigned to this report
     */
    public void setLocation(Location location) { this.location = location; }


    /**
     * method to return the source of this current water report
     * @return the source associated with the report
     */
    public String getSource() {
        return source;
    }

    /**
     * method to set the source of the water to a new value
     * @param source the new source (bottled, spring, etc.)
     *                  of the WaterReport
     */
    public void setSource(String source) {
        if (waterSources.contains(source)) {
            this.source = source;
        } //else {
//            // TODO: handle this error
//        }
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
     * @param condition the new condition that you want to update the
     *                WaterReport to be
     */
    public void setCondition(String condition) {
        if (waterCondition.contains(condition)) {
            this.condition = condition;
        } //else {
//            // TODO: handle this error
//        }
    }

    // equals based on reporter, condition, and source
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WaterReport)) {
            return false;
        }
        WaterReport wr = (WaterReport) o;
        return (wr.getId() == id
                && wr.getReporter().equals(reporter)
                && wr.getCondition().equals(condition)
                && wr.getSource().equals(source)
                && wr.getLocation().equals(location));
    }

    @Override
    public String toString() { return ("Report No.: " + id + " " + reporter.getEmailAddress() + "\n"
            + " - " + condition + " - " + source
            + "\nLocation: " + location); }
}
