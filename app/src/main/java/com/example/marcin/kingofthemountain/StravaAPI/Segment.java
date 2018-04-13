package com.example.marcin.kingofthemountain.StravaAPI;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Segment {

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

    List<LatLng> pointsDecoded;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResourceState() {
        return resourceState;
    }

    public void setResourceState(Integer resourceState) {
        this.resourceState = resourceState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClimbCategory() {
        return climbCategory;
    }

    public void setClimbCategory(Integer climbCategory) {
        this.climbCategory = climbCategory;
    }

    public String getClimbCategoryDesc() {
        return climbCategoryDesc;
    }

    public void setClimbCategoryDesc(String climbCategoryDesc) {
        this.climbCategoryDesc = climbCategoryDesc;
    }

    public Double getAvgGrade() {
        return avgGrade;
    }

    public void setAvgGrade(Double avgGrade) {
        this.avgGrade = avgGrade;
    }

    public List<Double> getStartLatlng() {
        return startLatlng;
    }

    public void setStartLatlng(List<Double> startLatlng) {
        this.startLatlng = startLatlng;
    }

    public List<Double> getEndLatlng() {
        return endLatlng;
    }

    public void setEndLatlng(List<Double> endLatlng) {
        this.endLatlng = endLatlng;
    }

    public Double getElevDifference() {
        return elevDifference;
    }

    public void setElevDifference(Double elevDifference) {
        this.elevDifference = elevDifference;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
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
}
