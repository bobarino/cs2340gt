package com.cs2340gt.nick.app_android.model;

/**
 * Created by ArmandoGonzalez on 2/14/17.
 */
public class Account {

    //unique ID value for the Account
    private int id;

    // unique username for the account
    private String username;

    // unique password for the account
    private String password;

    //unique email address for the Account
    private String emailAddress;

    //unique Credential for the Account (from user, worker, manager, admin)
    private Credential credential;

    // variable to keep track of the next number for the account IDs
    private static int nextNo = 0;

    /**
     * no arg constructor to create a new Account
     */
    public Account() {
        this("user", "pass", "example@gatech.edu", Credential.USER);
    }

    /**
     * constructor to create a fully flushed out Account object
     * @param username the passed in username of this account
     * @param password the passed in password of this account
     * @param emailAddress the passed in emailAddress of this account
     * @param credential the passed in Credential value of this account
     */
    public Account(String username,
                   String password,
                   String emailAddress,
                   Credential credential) {
        this.id = nextNo++;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.credential = credential;
    }

    /**
     * method to get username for this Account
     * @return the username for this Account
     */
    public String getUsername() {
        return username;
    }

    /**
     * method to set the username to a new value
     * @param username the new username value
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * method to get the password from this account
     * @return the password for this account
     */
    public String getPassword() {
        return password;
    }

    /**
     * method to update the password
     * @param password new password value
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * method to return the email address of the account
     * @return the email address for this account
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * method to change the email address for this account
     * @param emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * mthod to get the credential for this account
     * @return credential for the account
     */
    public Credential getCredential() {
        return credential;
    }

    /**
     * method to update the credential
     * @param credential value to update to
     */
    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    /**
     * method to get the id of the curernt account
     * @return id value for account
     */
    public int getId() {
        return id;
    }

    // override equals and toString
    @Override
    public boolean equals(Object account) {
        Account a = (Account) account;
        return a.getUsername().equals(username);
    }

    @Override
    public String toString() {
        return id + " " + username + " " + emailAddress + " " + credential;
    }

}
