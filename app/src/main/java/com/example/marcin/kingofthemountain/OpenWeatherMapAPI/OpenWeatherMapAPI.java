package com.example.marcin.kingofthemountain.OpenWeatherMapAPI;

import com.example.marcin.kingofthemountain.StravaAPI.Segment;
import com.example.marcin.kingofthemountain.StravaAPI.SegmentRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Marcin on 03-Apr-18.
 */

public interface OpenWeatherMapAPI {

    String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    String TOKEN = "42e68ab8c25331b8982fa9bba1707b51";
    String UNITS = "metric";

    @GET("weather")
    Call<WeatherRoot> getWeatherByCoords(@Query("lat") String latitude, @Query("lon") String longitude, @Query("units") String units, @Query("appid") String access_token);

}
