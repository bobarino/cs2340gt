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

    // current account being worked with
    private static Account currentAccount;

    // current WaterReport being worked with
    private static WaterReport currentReport;

    // current account getter and setter
    public Account getCurrentAccount() {
        if (currentAccount == null) {
            return nullAcc;
        } else {
            return currentAccount;
        }
    }
    public void setCurrentAcc(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    // current report getter and setter
    public static WaterReport getCurrentReport() { return currentReport; }
    public void setCurrentReport(WaterReport _currentReport) { currentReport = _currentReport; }

    // created for the case of an error
    private final Account nullAcc = new Account(9999);

    // created for case of an error
    private final WaterReport nullReport =
            new WaterReport(nullAcc, "null", "null", "null", "null");

    // constructor for our model
    public Model() {
        accountList = new ArrayList<>();
        reportList = new ArrayList<>();
    }

    /**
     * A method to add accounts to the application. Only adds if the account does not exist.
     *
     * @param newAcc the candidate account.
     * @return whether or not addition was successful.
     */
    public boolean addAccountInfo(Account newAcc) {
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
        if (reportList.contains(newReport)) {
            return false;
        }
        reportList.add(newReport);
        return true;
    }

    /**
     * A method to find an account by its id, using an O(n)
     * linear search.
     *
     * @param id the username being searched by.
     * @return the account, if found, or the null account.
     */
    public Account findAccountById(int id) {
        if (accountList.size() == 0) {
            return nullAcc;
        } else {
            for (Account account: accountList) {
                if (account.getId() == id) {
                    return account;
                }
            }
            return nullAcc;
        }

    }

    /**
     * A method to find an account by its username, using an O(n)
     * linear search.
     *
     * @param email the email address being searched by.
     * @return the account, if found, or the null account.
     */
    public Account findAccountByEmail(String email) {
        if (accountList.size() == 0) {
            return nullAcc;
        } else {
            for (Account account: accountList) {
                if (account.getEmailAddress().equals(email)) {
                    return account;
                }
            }
            return nullAcc;
        }
    }

    /*
    * method to find a report by the account that submitted it
    *
    * @param user the username being searched for
    * @return a list of waterReports submitted by a particular user,
    * if found, or null otherwise
     */
    public List<WaterReport> findReportBySubmitter(Account account) {
        List<WaterReport> foundSubmissions = new ArrayList<>();
        for (WaterReport wr : reportList) {
            if (wr.getReporter().equals(account)) {
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
