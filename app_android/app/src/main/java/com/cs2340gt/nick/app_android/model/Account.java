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

    public Account() {
        // default for firebase database updates
    }

    // constructor for general use with proper indexing
    public Account(String emailAddress, String password, Credential credential) {
        this(Next_Id++, "user", "pass", "example@gatech.edu", Credential.USER);
    }

    public Account(int id, String username,
                   String password,
                   String emailAddress,
                   Credential credential) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    // id should be read-only
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
