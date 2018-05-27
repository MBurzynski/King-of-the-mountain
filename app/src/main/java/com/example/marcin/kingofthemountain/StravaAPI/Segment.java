package com.example.marcin.kingofthemountain.StravaAPI;

import android.content.Context;
import android.util.Log;
import java.util.List;

import com.example.marcin.kingofthemountain.AuthActivity;
import com.example.marcin.kingofthemountain.OpenWeatherMapAPI.Wind;
import com.example.marcin.kingofthemountain.WindOnSegment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Segment{

    private final static int kmToM = 1000;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("resource_state")
    @Expose
    private Integer resourceState;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("climb_category")
    @Expose
    private Integer climbCategory;
    @SerializedName("climb_category_desc")
    @Expose
    private String climbCategoryDesc;
    @SerializedName("avg_grade")
    @Expose
    private Double avgGrade;
    @SerializedName("average_grade")
    @Expose
    private Double averageGrade;
    @SerializedName("start_latlng")
    @Expose
    private List<Double> startLatlng = null;
    @SerializedName("end_latlng")
    @Expose
    private List<Double> endLatlng = null;
    @SerializedName("elev_difference")
    @Expose
    private Double elevDifference;

    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("points")
    @Expose
    private String points;
    @SerializedName("starred")
    @Expose
    private Boolean starred;
    @SerializedName("map")
    @Expose
    private Map map;

    List<LatLng> pointsDecoded;
    Polyline polyline;

    WindOnSegment windOnSegment;

    LatLng startPoint;
    LatLng endPoint;



    /**
     * No args constructor for use in serialization
     *
     */
    public Segment() {
    }

    /**
     *
     * @param elevDifference
     * @param id
     * @param distance
     * @param avgGrade
     * @param startLatlng
     * @param starred
     * @param name
     * @param points
     * @param climbCategoryDesc
     * @param climbCategory
     * @param resourceState
     * @param endLatlng
     */
    public Segment(Integer id, Integer resourceState, String name, Integer climbCategory, String climbCategoryDesc, Double avgGrade, List<Double> startLatlng, List<Double> endLatlng, Double elevDifference, Double distance, String points, Boolean starred) {
        super();
        this.id = id;
        this.resourceState = resourceState;
        this.name = name;
        this.climbCategory = climbCategory;
        this.climbCategoryDesc = climbCategoryDesc;
        this.avgGrade = avgGrade;
        this.averageGrade = averageGrade;
        this.startLatlng = startLatlng;
        this.endLatlng = endLatlng;
        this.elevDifference = elevDifference;
        this.distance = distance;
        this.points = points;
        this.starred = starred;
    }

    public Integer getId() {
        return id;
    }

    public Integer getResourceState() {
        return resourceState;
    }

    public String getName() {
        return name;
    }

    public Integer getClimbCategory() {
        return climbCategory;
    }

    public String getClimbCategoryDesc() {
        return climbCategoryDesc;
    }

    public Double getAvgGrade() {
        return avgGrade;
    }
    
    public Double getAverageGrade() {
        return averageGrade;
    }

    public List<Double> getStartLatlng() {
        return startLatlng;
    }

    public List<Double> getEndLatlng() {
        return endLatlng;
    }

    public Double getElevDifference() {
        return elevDifference;
    }

    public Double getDistance() {
        return distance;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points){
        this.points = points;
    }

    public Boolean getStarred() {
        return starred;
    }

    public Map getMap() {
        return map;
    }

    public void setStarred(Boolean starred, Context context) {
        Retrofit retrofitStrava;
        StravaAPI stravaAPI;
        retrofitStrava = new Retrofit.Builder()
                .baseUrl(StravaAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stravaAPI = retrofitStrava.create(StravaAPI.class);
        Call<Segment> call = stravaAPI.starSegment(getId().toString(),starred, AuthActivity.getStravaAccessToken(context));
        call.enqueue(new Callback<Segment>() {
            @Override
            public void onResponse(Call<Segment> call, Response<Segment> response) {
                Log.d("SEGMENT","Segment set as starred/unstarred");
                Segment temporarySegment = response.body();
                Log.d("SEGMENT", temporarySegment.getStarred().toString());
            }

            @Override
            public void onFailure(Call<Segment> call, Throwable t) {
                Log.d("SEGMENT","Call FAILED");
            }
        });
        this.starred = starred;
    }

    public List<LatLng> getPointsDecoded() {
        return pointsDecoded;
    }

    public void setPointsDecoded(List<LatLng> pointsDecoded) {
        this.pointsDecoded = pointsDecoded;
    }

    public LatLng getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }
    public LatLng getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(LatLng endPoint) {
        this.endPoint = endPoint;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public WindOnSegment getWindOnSegment() {
        return windOnSegment;
    }

    public void setWindOnSegment(WindOnSegment windOnSegment) {
        this.windOnSegment = windOnSegment;
    }

    public void calculateWindOnSegment(Wind wind){
        WindOnSegment windOnSegment = new WindOnSegment(this, wind);
        this.setWindOnSegment(windOnSegment);
        windOnSegment.computeWindOnSegment();
    }

    public boolean segmentMeetsConditions (double minDist, double maxDist, double minGrade, double maxGrade, double minWind){
        if(getDistance() >= minDist * kmToM && getDistance() <= maxDist * kmToM && getAvgGrade() >= minGrade
                && getAvgGrade() <= maxGrade && getWindOnSegment().getPercentageTailWind() >= minWind)
            return true;
        return false;
    }


    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
