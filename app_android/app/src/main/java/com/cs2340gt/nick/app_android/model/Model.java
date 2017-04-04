package com.cs2340gt.nick.app_android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ArmandoGonzalez on 2/15/17.
 */
public class Model {

    // Singleton instance of the model - not sure if best option yet
    private static final Model instance = new Model();
    // getter for this model instance
    public static Model getInstance() {
        return instance;
    };

    // list of accounts in the model
    private List<Account> accountList;
    // getter for accountList (read-only)
    public List<Account> getAccountList() {
        return accountList;
    }

    // list of waterReports in the model
    private List<WaterReport> reportList;
    //getter for reportList (read-only)
    public List<WaterReport> getReportList() { return reportList; }

    // list of water purity reports in the model
    private List<WaterPurityReport> purityReportList;
    //getter for purity report list (read-only)
    public List<WaterPurityReport> getPurityReportList() { return purityReportList; }

    // current account being worked with
    private static Account currentAccount;

    // current WaterReport being worked with
    private static WaterReport currentReport;
    // current WaterReport ID no.
    private static int nextReportId = 1;
    // current WaterPurityReport ID no.
    private static int nextPurityReportId = 1;

    // nextReportId getter
    public static int getNextReportId() { return nextReportId; }
    // getter for the next purity report id
    public static int getNextPurityReportId() { return nextPurityReportId; }

    /**
     * method to get the current account that's logged in
     * @return an Account that the model is currenlty logged in as
     */
    public static Account getCurrentAccount() {
        return currentAccount;
    }

    /**
     * method to set a current account that the model is logged in as
     * @param currentAccount the method to update the logged in status as
     */
    public void setCurrentAcc(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    /**
     * method to get the current report being used in the model
     * @return a WaterReport that we're currently using
     */
    public static WaterReport getCurrentReport() { return currentReport; }

    private Account tester = new Account("sean", "sean", "sean", Credential.MANAGER);
    private Account worker = new Account("bills", "bills", "bills", Credential.WORKER);

    /**
     * method to set the current report associated with the model at hand
     * @param _currentReport the updated current report
     */
    public void setCurrentReport(WaterReport _currentReport) { currentReport = _currentReport; }


    /**
     * no arg constructor for the model class...no need for a parameter constructor
     */
    public Model() {
        accountList = new ArrayList<>();
        reportList = new ArrayList<>();
        purityReportList = new ArrayList<>();
        accountList.add(tester);
        accountList.add(worker);
    }

    /**
     * A method to add accounts to the application. Checks to see
     * if the account already exists.
     *
     * @param newAcc the candidate account.
     * @return whether or not addition was successful.
     */
    public boolean addAccount(Account newAcc) {
        for (Account account: accountList) {
            if (newAcc.equals(account)) {
                return false;
            }
        }
        accountList.add(newAcc);
        return true;
    }

    /**
     * a method to add water reports to the application
     * will check first to see if a report already exists
     *
     * @param newReport the report attempting to be added
     * @return whether the add was successful
     */
    public boolean addReport(WaterReport newReport) {
        if (checkInvalidLocation(newReport.getLocation())) {
            return false;
        } else if (reportList.contains(newReport)) {
            return false;
        }
        reportList.add(newReport);
        Model.nextReportId++;
        return true;
    }

    /**
     * method to add a new purity report to the overall list of
     * water purity reports contained within the model
     * @param newRep the new purity report to add to the list
     * @return a boolean representing whether the report was added
     */
    public boolean addPurityReport(WaterPurityReport newRep) {
        if (checkInvalidLocation(newRep.getLocation())) {
            return false;
        } else if (purityReportList.contains(newRep)) {
            return false;
        }
        purityReportList.add(newRep);
        Model.nextPurityReportId++;
        return true;
    }

    /**
     * method to check whether a valid lat/long combination has been
     * entered into a report's submission page (works for both regular
     * reports and purity reports)
     * @param check the location to check the lat/long pair of
     * @return a boolean representing whether the location was invalid
     * (returns true if the input was invalid and false otherwise)
     */
    public boolean checkInvalidLocation(Location check) {
        if (check.getLatitude() > 90 || check.getLatitude() < -90
            || check.getLongitude() > 180 || check.getLongitude() < -180) {
            return true;
        }
        return false;
    }

    /**
     * A method to find an account by its id, using an O(n)
     * linear search.
     *
     * @param id the username being searched by.
     * @return the account, if found, or the null account.
     */
    public Account findAccountById(int id) {
        for (Account account: accountList) {
            if (account.getId() == id) {
                return account;
            }
        }
        return null;
    }

    /**
     * A method to find an account by its username, using an O(n)
     * linear search.
     *
     * @param username the username being searched by.
     * @return the account, if found, or the null account.
     */
    public Account findAccountByUser(String username) {
        for (Account account: accountList) {
            if (account.getUsername().equals(username)) {
                return account;
            }
        }
        return null;
    }

    /*
    * method to find a report by the account that submitted it
    *
    * @param user the username being searched for
    * @return a list of waterReports submitted by a particular user,
    * if found, or null otherwise
     */
    public List<WaterReport> findReportBySubmitter(Account user) {
        List<WaterReport> foundSubmissions = new ArrayList<>();
        for (WaterReport wr : reportList) {
            if (wr.getReporter().equals(user)) {
                foundSubmissions.add(wr);
            }
        }
        if (foundSubmissions.size() == 0) {
            return null;
        } else {
            return foundSubmissions;
        }
    }

    /*
    * method to find report by its id number
    *
    * @param id the id number being searched for (each report has
    * a unique id so this will only return one report)
    * @return the water report with the matching id, if there is one,
    * else null
     */
    public WaterReport findReportById(int id) {
        for (WaterReport report: reportList) {
            if (report.getId() == id) {
                return report;
            }
        }
        return null;
    }

    /*
    * method to find reports by their source
    *
    * @param sourceSpec the string value of the source type for
    * which we want to find all matching water reports
    * @return a list of water reports with source types matching
    * the specified type, null if there are no matches
     */
    public List<WaterReport> findReportBySource(String sourceSpec) {
        List<WaterReport> sourceMatches = new ArrayList<>();
        for (WaterReport wr : reportList) {
            if (wr.getSource().equals(sourceSpec)) {
                sourceMatches.add(wr);
            }
        }
        if (sourceMatches.size() == 0) {
            return null;
        } else {
            return sourceMatches;
        }
    }

    /*
    * method to find reports by their water conditions
    *
    * @param condSpec the string value of the water condition for
    * which we want to find all matching water reports
    * @return a list of all water reports with conditions matching
    * the specified type, null if there are no matches
     */
    public List<WaterReport> findReportByCondition(String condSpec) {
        List<WaterReport> conditionMatches = new ArrayList<>();
        for (WaterReport wr : reportList) {
            if (wr.getCondition().equals(condSpec)) {
                conditionMatches.add(wr);
            }
        }
        if (conditionMatches.size() == 0) {
            return null;
        } else {
            return conditionMatches;
        }
    }

}
