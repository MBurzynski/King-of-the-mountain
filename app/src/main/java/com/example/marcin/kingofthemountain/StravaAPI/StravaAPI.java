package com.example.marcin.kingofthemountain.StravaAPI;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Marcin on 31-Mar-18.
 */

public interface StravaAPI {

    String BASE_URL = "https://www.strava.com/api/v3/";

    @GET("segments/explore")
    Call<SegmentRoot> getSegments(@Query("bounds") String bounds, @Query("access_token") String access_token);

    @GET("segments/1687463")
    Call<Segment> getSegment(@Query("access_token") String access_token);
    
    @GET("segments/starred")
    Call<List<Segment>> listStarredSegments (@Query("per_page") String per_page, @Query("access_token") String access_token);
    
    @PUT("segments/{id}/starred")
    Call<Segment> starSegment (@Path("id") String id, @Field("starred") boolean starred);


}
