package com.example.marcin.kingofthemountain.OpenWeatherMapAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Marcin on 28-May-18.
 */

public class ForecastRoot {

    @SerializedName("list")
    @Expose
    private java.util.List<com.example.marcin.kingofthemountain.OpenWeatherMapAPI.List> list = null;



    public java.util.List<com.example.marcin.kingofthemountain.OpenWeatherMapAPI.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.example.marcin.kingofthemountain.OpenWeatherMapAPI.List> list) {
        this.list = list;
    }
}
