package com.cs2340gt.nick.app_android.model;
import java.util.List;
import java.util.Arrays;

/**
 * Created by Sean Bills on 2/18/17.
 */

public class WaterReport {

    private static int nextNo = 1;
    private Account reporter;
    private int id;
    public static List<String> waterSources = Arrays.asList("Bottled", "Well",
            "Stream", "Lake", "Spring", "Other");
    private String source;
    public static List<String> waterCondition = Arrays.asList("Waste", "Treatable - Muddy",
            "Treatable - Clear", "Potable");
    private String condition;

    /*
    * need to figure out the location information
    * need to figure out the date/time data
     */

    public WaterReport(Account _reporter, String _source, String _condition) {
        reporter = _reporter;
        setCondition(_condition);
        setSource(_source);
        id = WaterReport.nextNo++;
    }

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


    @Override
    public boolean equals(Object o) {
        WaterReport wr = (WaterReport) o;
        return (wr.getReporter().equals(reporter)
                && wr.getCondition().equals(condition)
                && wr.getSource().equals(source) && wr.getId() == id);
    }

    @Override
    public String toString() { return (reporter + " " + condition + " " + source); }
}
