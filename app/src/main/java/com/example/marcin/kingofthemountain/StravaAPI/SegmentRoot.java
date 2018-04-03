package com.example.marcin.kingofthemountain.StravaAPI;

import java.util.List;

import com.example.marcin.kingofthemountain.StravaAPI.Segment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SegmentRoot {

    @SerializedName("segments")
    @Expose
    private List<Segment> segments = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public SegmentRoot() {
    }

    /**
     *
     * @param segments
     */
    public SegmentRoot(List<Segment> segments) {
        super();
        this.segments = segments;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

}