package com.cs2340gt.nick.app_android.model;

/**
 * class intended to serve as the major outline for the Credential enum
 * produced by Armando Gonzalez on 2/14/17
 */
public enum Credential {

    USER ("USER"),
    WORKER ("WORKER"),
    MANAGER ("MANAGER"),
    ADMIN ("ADMIN"),
    NULL ("NULL");

    private final String fullCredential;

    /**
     * Only constructor that should be used for this enum.
     *
     * @param fullCredential full string representation of the
     *                       credentials
     */
    Credential(String fullCredential) {
        this.fullCredential = fullCredential;
    }

    // use toString for labels or whenever you need to display it

    @Override
    public String toString() {
        return fullCredential;
    }

}
