package com.cs2340gt.nick.app_android.model;

/**
 * Created by ArmandoGonzalez on 2/14/17.
 */
public class Account {

    public static int MAX_USER_LENGTH = 12;
    private static int Next_Id = 0000;

    private int id;
    private String username;
    private String password;
    private String emailAddress;
    private Credential credential;

    // ONLY for use with a null account, for error handling
    public Account(int id) {
        this.id = id;
        username = "null";
        password = "null";
        emailAddress = "null";
        credential = Credential.NULL;
    }

    // general constructor with proper id assignment
    public Account(
                   String username,
                   String password,
                   String emailAddress,
                   Credential credential) {
        this.id = Account.Next_Id++;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.credential = credential;
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
        if (this == account) {
            return true;
        } else if (account instanceof Account) {
            Account a = (Account) account;
            return a.getId() == id;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return username;
    }

}
