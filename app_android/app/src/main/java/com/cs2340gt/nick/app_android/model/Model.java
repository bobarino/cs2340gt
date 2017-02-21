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

    // current account being worked with
    private Account currentAccount;

    // current account getter and setter
    public Account getCurrentAccount() {
        return currentAccount;
    }
    public void setCurrentAcc(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    // created for the case of an error
    private final Account nullAcc = new Account(9999);

    // constructor for our model
    public Model() {
        accountList = new ArrayList<>();
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

    public boolean editAccountInfo(Account account) {
        Account existing = findAccountById(account.getId());

        // TODO: remove this major error check from working code
        if (existing != null) {
            return false;
        } else {
            existing.setUsername(account.getUsername());
            existing.setPassword(account.getPassword());
            existing.setEmailAddress(account.getEmailAddress());
            existing.setCredential(account.getCredential());
            return true;
        }

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
        return nullAcc;
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
        return nullAcc;
    }

}
