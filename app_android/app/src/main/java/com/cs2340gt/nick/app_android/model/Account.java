package com.cs2340gt.nick.app_android.model;

/**
 * Created by ArmandoGonzalez on 2/14/17.
 */
public class Account {

    private int id;
    private String username;
    private String password;
    private String emailAddress;
    private Credential credential;

    public Account() {
        this(0, "user",
                "pass",
                "example@gatech.edu",
                Credential.USER);
    }

    public Account(int id,
                   String username,
                   String password,
                   String emailAddress,
                   Credential credential) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.credential = credential;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    @Override
    public boolean equals(Object account) {
        Account a = (Account) account;
        return a.getUsername().equals(username);
    }

    @Override
    public String toString() {
        return username;
    }

}
