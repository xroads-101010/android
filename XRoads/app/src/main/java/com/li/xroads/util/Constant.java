package com.li.xroads.util;

/**
 * Created by sony on 28-02-2016.
 */
public interface Constant {

    String Latitude = "latitude";
    String Longitude = "longitude";
    String TRIP_ID = "tripId";
    String USER_ID = "userId";

    String EMPTY_JSON = "{}";

    // USer JSON Details
    String EMAIL_KEY = "email";
    String USER_MOBILE_KEY = "userMobile";
    String USER_NAME_KEY = "userName";
    String PASSWORD_KEY = "password";
    String IS_REGISTERED_KEY = "isRegistered";
    String TRIPS_KEY = "trips";
    String ID_KEY = "id";

    interface Trip {
        int CREATE_TRIP_REQUEST = 1;
        int UPDATE_TRIP_REQUEST = 1;
        String NAME = "name";
        String TRIP_PARCEL_URI = "com.trip";
    }

    String ACTION_CREATE = "CREATE";
    String ACTION_UPDATE = "UPDATE";
}
