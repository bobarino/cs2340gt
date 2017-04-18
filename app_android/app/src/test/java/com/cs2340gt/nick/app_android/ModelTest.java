package com.cs2340gt.nick.app_android;

/*
  Created by SEAN on 4/2/17.
 */

import com.cs2340gt.nick.app_android.model.Account;
import com.cs2340gt.nick.app_android.model.Credential;
import com.cs2340gt.nick.app_android.model.Model;
import com.cs2340gt.nick.app_android.model.Location;
import com.cs2340gt.nick.app_android.model.WaterPurityReport;
import com.cs2340gt.nick.app_android.model.WaterReport;

import org.junit.Assert;
import org.junit.Test;

public class ModelTest {
    /**
     * test fixture
     */
    private Model theModel;
    /**
     * this method runs before each test
     *  @throws java.lang.Exception in the case that a model facade cannot be produced
     */
    @org.junit.Before
    public void setUp() throws Exception {
        theModel = new Model();
    }

    /**
     * test method for Model.addAccount
     */
    @Test
    public void testAddAccount() {
        Assert.assertEquals("Initial size of reporter list is incorrect", 0,
                theModel.getAccountList().size());
        try {
            theModel.addAccountInfo(new Account(1, "mbills2@gmail.com", "mbills2", Credential.MANAGER));
            theModel.addAccountInfo(new Account(2, "madie", "madie", Credential.USER));
            theModel.addAccountInfo(new Account(3, "sbills3", "sbills3", Credential.ADMIN));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Should not have thrown exception here");
        }
        Assert.assertEquals("Total account list size wrong after adding", 3,
                theModel.getAccountList().size());
    }

    /**
     * test method for WaterReport.setLocation
     */
    @Test
    public void testSetLocation() {
        try {
            Location location = new Location(45, 50);
            WaterReport wr = new WaterReport(Model.getCurrentAccount(),
                    "Bottled", "Waste", "12:04", location);
            wr.setLocation(new Location(40, 35));
            Location check = new Location(40, 35);
            Assert.assertEquals("Incorrect location after setting new location", check,
                    wr.getLocation());
        } catch (Exception e) {
            Assert.fail("Set Location should not have thrown exception here");
            e.printStackTrace();
        }
    }

    /**
     * test method for Account.setPassword
     */
    @Test
    public void testAccountSetPassword() {
        try {
            Account temp = new Account("sbills3@gatech.edu", "password", Credential.USER);
            theModel.addAccountInfo(temp);
            String find = "sbills3@gatech.edu";
            Account there = theModel.findAccountByEmail(find);
            Assert.assertEquals("Incorrect Account returned", there, temp);
        } catch (Exception e) {
            Assert.fail("Set account password should not have failed here");
            e.printStackTrace();
        }
    }

    /**
     * test method for Model.addReport
     */
    @Test
    public void testAddingReport() {
        Assert.assertEquals("Incorrect initial water report list size", 0,
                theModel.getReportList().size());
        try {
            Account holder = new Account(1, "bills", "bills", Credential.ADMIN);
            WaterReport a = new WaterReport(holder, "Bottled", "Waste",
                    "Today", new Location(45, 50));
            WaterReport b = new WaterReport(holder, "Well",
                    "Potable", "Today", new Location(30, 25));
            WaterReport c = new WaterReport(holder, "Stream",
                    "Treatable - Muddy", "Today", new Location(20, 65));
            theModel.addReport(a);
            theModel.addReport(b);
            theModel.addReport(c);
            Assert.assertEquals("WaterReport list size incorrect after adding", 3,
                    theModel.getReportList().size());
        } catch (Exception e) {
            Assert.fail("adding water reports should not have failed here");
            e.printStackTrace();
        }
    }

    /**
     * test method for Model.addPurityReport
     */
    @Test
    public void testAddPurityReport() {
        Assert.assertEquals("incorrect initial purity report list size", 0,
                theModel.getPurityReportList().size());
        try {
            WaterPurityReport a = new WaterPurityReport(Model.getCurrentAccount(), "Safe",
                    400, 300, "Today", new Location(40, 30));
            WaterPurityReport b = new WaterPurityReport(Model.getCurrentAccount(), "Unsafe",
                    250, 100, "Today", new Location(25, 50));
            theModel.addPurityReport(a);
            theModel.addPurityReport(b);
            Assert.assertEquals("incorrect size of purity report list after adding", 2,
                    theModel.getPurityReportList().size());
        } catch (Exception e) {
            Assert.fail("Adding purity reports shouldn't have failed here");
            e.printStackTrace();
        }
    }

}
