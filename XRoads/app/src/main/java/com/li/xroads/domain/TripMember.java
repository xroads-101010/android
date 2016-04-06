package com.li.xroads.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nagnath_p on 3/18/2016.
 */
public class TripMember {

    @SerializedName("id")
    private Integer id;
    @SerializedName("hasMemberJoined")
    private boolean hasMemberJoined;

    public TripMember(){

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isHasMemberJoined() {
        return hasMemberJoined;
    }

    public void setHasMemberJoined(boolean hasMemberJoined) {
        this.hasMemberJoined = hasMemberJoined;
    }
}
