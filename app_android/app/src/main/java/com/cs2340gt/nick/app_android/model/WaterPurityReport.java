package com.cs2340gt.nick.app_android.model;

import java.util.List;
import java.util.Arrays;

/**
 * Created by SEAN on 3/12/17.
 * Edited by ARMANDO on 4/2/17.
 */

public class WaterPurityReport {

    private static int nextId = 0;

    // account that submitted this report
    private Account reporter;

    // date/time associated with the report's submission
    private String dateTime;

    // string value of the condition for this report
    private String condition;

    //the unique ID value of this report
    private int id;

    //list of all possible conditions for water
    public static List<String> waterConditions = Arrays.asList("Safe", "Treatable", "Unsafe");

    // viral PPM value
    private int viralPPM;

    // contaminant PPM value
    private int contaminantPPM;

    private Location location;

    /**
     * constructor to create a WaterPurityReport object
     * @param reporter the account/user reporting the water purity
     * @param condition the string value associated with the condition of the water
     *                   (i.e. safe, treatable, unsafe)
     * @param viralPPM the viral ppm of the water
     * @param contaminantPPM the contaminant ppm of the water
     * @param dateTime the date and time that the report is being submitted
     * @param location the location at which the report is being submitted for
     */
    public WaterPurityReport(Account reporter, String condition,
                             int viralPPM, int contaminantPPM, String dateTime,
                             Location location) {
        this(nextId++, reporter, condition, viralPPM, contaminantPPM, dateTime, location);
    }

    /**
     * constructor to create a WaterPurityReport object
     * @param id the unique id being assigned to this water purity report
     * @param reporter the account/user reporting the water purity
     * @param condition the string value associated with the condition of the water
     *                   (i.e. safe, treatable, unsafe)
     * @param viralPPM the viral ppm of the water
     * @param contaminantPPM the contaminant ppm of the water
     * @param dateTime the date and time that the report is being submitted
     * @param location the location at which the report is being submitted for
     */
    public WaterPurityReport(int id, Account reporter, String condition,
                             int viralPPM, int contaminantPPM, String dateTime,
                             Location location) {
        this.id = id;
        this.reporter = reporter;
        this.condition = condition;
        this.viralPPM = viralPPM;
        this.contaminantPPM = contaminantPPM;
        this.dateTime = dateTime;
        this.location = location;
    }

    /**
     * method to get the account associated with the report
     * @return the account of the reporter
     */
    public Account getReporter() { return reporter; }

    /**
     * method to update the account
     * @param reporter the new account associated with report
     */
    public void setReporter(Account reporter) { this.reporter = reporter; }

    /**
     * method to get the id of the report
     * @return the id vlaue for the report
     */
    public int getId() { return id; }

    /**
     * method to update the id value for the report
     * @param id the new id value
     */
    public void setId(int id) { this.id = id;}

    /**
     * method to get the viral ppm of the report
     * @return the viral ppm
     */
    public int getViralPPM() { return viralPPM; }

    /**
     * method to update the viral ppm
     * @param viralPPM the new viral ppm value
     */
    public void setViralPPM(int viralPPM) { this.viralPPM = viralPPM; }

    /**
     * method to get the contaminant ppm of the report
     * @return the contaminant ppm
     */
    public int getContaminantPPM() { return contaminantPPM; }

    /**
     * method to update the contaminant ppm for the report
     * @param contaminantPPM the updated contaminant ppm value
     */
    public void setContaminantPPM(int contaminantPPM) { this.contaminantPPM = contaminantPPM; }

    /**
     * method to get the date/time of the report
     * @return the date/time of the report
     */
    public String getDateTime() { return dateTime; }

    /**
     * method to update the date/time of the report
     * @param dateTime the date/time of the report
     */
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    /**
     * method to get the location of the report
     * @return the location
     */
    public Location getLocation() { return location; }

    /**
     * method to update the lcoation of the report
     * @param location the new location
     */
    public void setLocation(Location location) { this.location = location; }

    /**
     * method to get the condition of the report
     * @return the condition
     */
    public String getCondition() { return condition; }

    /**
     * method to set the condition of the report
     * @param condition the new condition
     */
    public void setCondition(String condition) {
        if (waterConditions.contains(condition)) {
            this.condition = condition;
        } else {
            // TODO: handle this error
        }
    }

    @Override
    public boolean equals(Object o) {
        WaterPurityReport wpr = (WaterPurityReport) o;
        return (wpr.getId() == id
                && wpr.getLocation().equals(location)
                && wpr.getReporter().equals(reporter)
                && wpr.getDateTime().equals(dateTime)
                && wpr.getViralPPM() == viralPPM && wpr.getContaminantPPM() == contaminantPPM);
        }

    @Override
    public String toString() { return ("Report No.: " + id + " Reporter: " + reporter.getEmailAddress()
        + "\n" + condition + "\n" + "Viral PPM: " + viralPPM + " Contaminant PPM: " + contaminantPPM
        + "\nLocation: " + location);
    }


}
