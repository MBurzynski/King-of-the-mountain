package com.example.marcin.kingofthemountain.StravaAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Marcin on 26-May-18.
 */

public class Map{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("polyline")
    @Expose
    private String polyline;
    @SerializedName("resource_state")
    @Expose
    private Integer resourceState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public Integer getResourceState() {
        return resourceState;
    }

    public void setResourceState(Integer resourceState) {
        this.resourceState = resourceState;
    }

}
