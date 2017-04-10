package com.cs2340gt.nick.app_android.model;

/**
 * the major class outline for the Location object/class
 * produced by SEAN on 3/7/17.
 */

public class Location {

    private final double latitude;
    private final double longitude;

    /**
     * constructor to create a Location (lat and long) of where
     * the report is being submitted from
     * @param latitude variable to hold latitude value
     * @param longitude variable to hold longitude value
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * method to return the latitude value of the Location
     * @return latitude of Location
     */
    public double getLatitude() { return latitude; }

    /**
     * method to return the longitude of the location
     * @return longitude of Location
     */
    public double getLongitude() { return longitude; }

    @Override
    public String toString() { return "" + latitude + ", " + longitude; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Location)) {
            return false;
        }
        Location loc = (Location) o;
        return (loc.getLongitude() == longitude && loc.getLatitude() == latitude);
    }
}
