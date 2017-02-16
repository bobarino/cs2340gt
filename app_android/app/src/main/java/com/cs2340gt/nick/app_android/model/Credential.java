package com.cs2340gt.nick.app_android.model;

/**
 * Created by ArmandoGonzalez on 2/14/17.
 */
public enum Credential {

    USER ("USER", "U"),
    WORKER ("WORKER", "W"),
    MANAGER ("MANAGER", "M"),
    ADMIN ("ADMIN", "A"),
    NULL ("NULL", "N");

    private String fullCredential;
    private String shortCredential;

    /**
     * Only constructor that should be used for this enum.
     *
     * @param fullCredential full string representation of the
     *                       credentials
     * @param shortCredential single-character representation
     *                        of the credentials
     */
    Credential(String fullCredential,
               String shortCredential) {
        this.fullCredential = fullCredential;
        this.shortCredential = shortCredential;
    }

    /**
     * A method used to identify the user in code, keeping
     * their credential level short to keep code clean.
     *
     * @return the character representing their credential level.
     */
    public String identify() {
        return shortCredential;
    }

    // use toString for labels or whenever you need to display it

    @Override
    public String toString() {
        return fullCredential;
    }

}
