package com.li.xroads.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by nagnath_p on 3/9/2016.
 */
public class Trip implements Parcelable {

    private static final String TAG = "Trip Domain";

    @SerializedName("tripId")
    private Integer  id;
    @SerializedName("championUserId")
    private Integer  championUserId;
    @SerializedName("tripName")
    private String name;
    @SerializedName("tripDestination")
    String destination;
    @SerializedName("tripDestinationLat")
    private String destinationLat;
    @SerializedName("tripDestinationLong")
    private String destinationLong;
    @SerializedName("startLocationForCurrentUser")
    private String startLocation;
    @SerializedName("startLocationForCurrentUserLat")
    private String startLocationLat;
    @SerializedName("startLocationForCurrentUserLong")
    private String startLocationLong;
    @SerializedName("hasTripStarted")
    private boolean isStarted;
    @SerializedName("startTime")
    private long startDate;
    @SerializedName("endTime")
    private long endDate;
    @SerializedName("tripMembers")
    private List<TripMember> tripMemberList;

    public Trip(){

    }
    private Trip(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.startLocation = in .readString();
        this.destination = in.readString();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChampionUserId() {
        return championUserId;
    }

    public void setChampionUserId(Integer championUserId) {
        this.championUserId = championUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(String destinationLat) {
        this.destinationLat = destinationLat;
    }

    public String getDestinationLong() {
        return destinationLong;
    }

    public void setDestinationLong(String destinationLong) {
        this.destinationLong = destinationLong;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getStartLocationLong() {
        return startLocationLong;
    }

    public void setStartLocationLong(String startLocationLong) {
        this.startLocationLong = startLocationLong;
    }

    public String getStartLocationLat() {
        return startLocationLat;
    }

    public void setStartLocationLat(String startLocationLat) {
        this.startLocationLat = startLocationLat;
    }

    public List<TripMember> getTripMemberList() {
        return tripMemberList;
    }

    public void setTripMemberList(List<TripMember> tripMemberList) {
        this.tripMemberList = tripMemberList;
    }


    public String getStartDateAsDateFormat(){
        if(startDate != 0){
            GregorianCalendar start = new GregorianCalendar();
            start.setTimeInMillis(startDate);
        }
        return null;
    }

    public JSONObject asJson() throws JSONException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        JSONObject tipJson = new JSONObject(gsonBuilder.create().toJson(this, this.getClass()));
        return tipJson;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.startLocation);
        dest.writeString(this.destination);
    }

    /**
     * Retrieving Trip data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {

        @Override
        public Trip createFromParcel(Parcel source) {
            return new Trip(source);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}
