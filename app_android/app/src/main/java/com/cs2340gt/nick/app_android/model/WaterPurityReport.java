package com.cs2340gt.nick.app_android.model;

import java.util.List;
import java.util.Arrays;

/**
 * Created by SEAN on 3/12/17.
 */

public class WaterPurityReport {
    // account that is submitting the water purity report
    private Account account;

    // date/time associated with the report's submission
    private String date_time;

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
     * @param _reporter the account/user reporting the water purity
     * @param _condition the string value associated with the condition of the water
     *                   (i.e. safe, treatable, unsafe)
     * @param _viral the viral ppm of the water
     * @param _contaminant the contaminant ppm of the water
     * @param _dateTime the date and time that the report is being submitted
     * @param place the location at which the report is being submitted for
     */
    public WaterPurityReport(Account _reporter, String _condition,
                             int _viral, int _contaminant, String _dateTime,
                             Location place) {
        account = _reporter;
        setCondition(_condition);
        id = Model.getNextPurityReportId();
        viralPPM = _viral;
        contaminantPPM = _contaminant;
        date_time = _dateTime;
        location = place;
    }

    /**
     * method to get the account associated with the report
     * @return the account of the reporter
     */
    public Account getAccount() { return account; }

    /**
     * method to update the account
     * @param newUser the new account associated with report
     */
    public void setAccount(Account newUser) { account = newUser; }

    /**
     * method to get the id of the report
     * @return the id vlaue for the report
     */
    public int getId() { return id; }

    /**
     * method to update the id value for the report
     * @param newID the new id value
     */
    public void setId(int newID) { id = newID;}

    /**
     * method to get the viral ppm of the report
     * @return the viral ppm
     */
    public int getViralPPM() { return viralPPM; }

    /**
     * method to update the viral ppm
     * @param newViral the new viral ppm value
     */
    public void setViralPPM(int newViral) { viralPPM = newViral; }

    /**
     * method to get the contaminant ppm of the report
     * @return the contaminant ppm
     */
    public int getContaminantPPM() { return contaminantPPM; }

    /**
     * method to update the contaminant ppm for the report
     * @param newContaminant the updated contaminant ppm value
     */
    public void setContaminantPPM(int newContaminant) { contaminantPPM = newContaminant; }

    /**
     * method to get the date/time of the report
     * @return the date/time of the report
     */
    public String getDate_time() { return  date_time; }

    /**
     * method to update the date/time of the report
     * @param newDateTime the date/time of the report
     */
    public void setDate_time(String newDateTime) { date_time = newDateTime; }

    /**
     * method to get the lcoation of the report
     * @return the location
     */
    public Location getLocation() { return location; }

    /**
     * method to update the lcoation of the report
     * @param newLoc the new location
     */
    public void setLocation(Location newLoc) { location = newLoc; }

    /**
     * method to set the condition of the report
     * @param newCond the new condition
     */
    public void setCondition(String newCond) {
        if (waterConditions.contains(newCond)) {
            condition = newCond;
        }
    }

    @Override
    public boolean equals(Object o) {
        WaterPurityReport wpr = (WaterPurityReport) o;
        return (wpr.getLocation().equals(location)
                && wpr.getAccount().equals(account)
                && wpr.getDate_time().equals(date_time) && wpr.getId() == id
                && wpr.getViralPPM() == viralPPM && wpr.getContaminantPPM() == contaminantPPM);
        }

    @Override
    public String toString() { return ("Report No.: " + id + " Reporter: " + account.getUsername()
        + "\n" + condition + "\n" + "Viral PPM: " + viralPPM + " Contaminant PPM: " + contaminantPPM
        + "\nLocation: " + location);
    }


}
