package com.cs2340gt.nick.app_android.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ArmandoGonzalez on 2/14/17.
 */
@IgnoreExtraProperties
public class Account {

    public static int Next_Id = 0;

    private int id;
    private String emailAddress;
    private String password;
    private Credential credential;

    /**
     * no arg constructor to create a new Account
     */
    public Account() {
        Next_Id++;
        // default constructor used only for Firebase database updates
    }

    // constructor for general use with proper indexing
    public Account(String emailAddress, String password, Credential credential) {
        this(Next_Id++, emailAddress, password, credential);
    }

    /**
     * constructor to create a fully flushed out Account object
     * @param id the id to be assigned to this account
     * @param emailAddress the passed in email address of this account
     * @param password the passed in password of this account
     * @param credential the passed in Credential value of this account
     */
    public Account(int id, String emailAddress,
                   String password,
                   Credential credential) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.password = password;
        this.credential = credential;
    }

    // ONLY for use with creating the null account
    public Account(int id) {
        this.id = id;
        emailAddress = "null";
        password = "null";
        credential = Credential.NULL;
    }

    // getters and setters
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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
        return a.getEmailAddress().equals(emailAddress);
    }

    @Override
    public String toString() {
        return emailAddress;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("email", emailAddress);
        result.put("pass", password);
        result.put("credential", credential);
        return result;
    }

}
