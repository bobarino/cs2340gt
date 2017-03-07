package com.cs2340gt.nick.app_android.model;

/**
 * Created by SEAN on 3/7/17.
 */

public class Location {
    // variable to hold the latitude value
    private double _latitude;
    // variable to hold the longitude value
    private double _longitude;

    /**
     * constructor to create a Location (lat and long) of where
     * the report is being submitted from
     * @param lat variable to hold latitude value
     * @param longit variable to hold longitude value
     */
    public Location(double lat, double longit) {
        _latitude = lat;
        _longitude = longit;
    }

    /**
     * method to return the latitude value of the Location
     * @return latitude of Location
     */
    public double getLatitude() { return _latitude; }

    /**
     * method to return the longitude of the location
     * @return longitude of Location
     */
    public double getLongitude() { return _longitude; }

    @Override
    public String toString() { return "" + _latitude + ", " + _longitude; }

    @Override
    public boolean equals(Object o) {
        Location loc = (Location) o;
        return (loc.getLongitude() == _longitude && loc.getLatitude() == _latitude);
    }
}
