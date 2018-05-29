package com.example.marcin.kingofthemountain.OpenWeatherMapAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Marcin on 28-May-18.
 */

public class List {

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;


    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }


    public String getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }
}
