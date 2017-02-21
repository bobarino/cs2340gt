package com.cs2340gt.nick.app_android.model;

/**
 * Created by ArmandoGonzalez on 2/14/17.
 */
public class Account {

    public static final int MAX_USER_LENGTH = 10;
    public static int Next_Id = 0;

    private int id;
    private String username;
    private String password;
    private String emailAddress;
    private Credential credential;

    // constructor for general use with proper indexing
    public Account(String username, String password,
                   String emailAddress, Credential credential) {
        id = Next_Id++;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.credential = credential;
    }

    // ONLY for use with creating the null account
    public Account(int id) {
        this.id = id;
        username = "null";
        password = "null";
        emailAddress = "null";
        credential = Credential.NULL;
    }

    // getters and setters
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

    // override equals and toString
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
