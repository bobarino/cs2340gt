package com.cs2340gt.nick.app_android.model;
import java.util.List;
import java.util.Arrays;

/**
 * Created by Sean Bills on 2/18/17.
 */

public class WaterReport {

    private static int nextNo = 0;
    private int id;
    private Account reporter;
    private String date_time;
    public static List<String> waterSources = Arrays.asList("Bottled", "Well",
            "Stream", "Lake", "Spring", "Other");
    private String source;
    public static List<String> waterCondition = Arrays.asList("Waste", "Treatable - Muddy",
            "Treatable - Clear", "Potable");
    private String condition;
    private String location;

    public WaterReport() {
        // default constructor used only for Firebase database updates
    }

    /*
    // TODO: resolve the following
    * need to figure out the location information
    * need to figure out the date/time data
     */

    public WaterReport(Account reporter, String source, String condition,
                       String dateTime, String location) {
        this(nextNo++, reporter, source, condition, dateTime, location);
    }

    public WaterReport(int id, Account reporter, String source,
                       String condition, String dateTime, String location) {
        this.id = id;
        this.reporter = reporter;
        this.condition = condition;
        this.source = source;
        this.date_time = dateTime;
        this.location = location;
    }

    // getters and setters

    public Account getReporter() {
        return reporter;
    }
    public void setReporter(Account newReporter) {
        reporter = newReporter;
    }

    public int getId() {
        return id;
    }
    public void setId(int newID) {
        id = newID;
    }

    public String getDate_time() { return date_time; }
    public void setDate_time(String newDateTime) { date_time = newDateTime; }

    public String getLocation() { return location; }
    public void setLocation(String newLoc) { location = newLoc; }

    public int getNextNo() {return nextNo; }

    public String getSource() {
        return source;
    }
    public void setSource(String newSource) {
        if (waterSources.contains(newSource)) {
            source = newSource;
        }
    }

    public String getCondition() {
        return condition;
    }
    public void setCondition(String newCond) {
        if (waterCondition.contains(newCond)) {
            condition = newCond;
        }
    }

    // equals based on reporter, condition, and source
    @Override
    public boolean equals(Object o) {
        WaterReport wr = (WaterReport) o;
        return (wr.getReporter().equals(reporter)
                && wr.getCondition().equals(condition)
                && wr.getSource().equals(source) && wr.getId() == id);
    }

    @Override
    public String toString() { return ("Report No.: " + id + " " + reporter.getEmailAddress() + "\n"
            + " - " + condition + " - " + source
            + "\nLocation: " + location); }
}
