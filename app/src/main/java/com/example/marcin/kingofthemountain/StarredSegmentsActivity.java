package com.example.marcin.kingofthemountain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.marcin.kingofthemountain.StravaAPI.Segment;
import com.example.marcin.kingofthemountain.StravaAPI.SegmentRoot;
import com.example.marcin.kingofthemountain.StravaAPI.StravaAPI;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StarredSegmentsActivity extends AppCompatActivity {

    private ListView starredSegments;
    private ScrollListAdapter scrollListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred_segments);

        starredSegments = (ListView) findViewById(R.id.listViewStarredSegments);

        ArrayList<Segment> segmentList = new ArrayList<>();


    }

    private void getStarredSegments(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StravaAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StravaAPI stravaAPI = retrofit.create(StravaAPI.class);
        Call<List<Segment>> call = stravaAPI.listStarredSegments("100",AuthActivity.getStravaAccessToken(this));

        call.enqueue(new Callback<List<Segment>>() {
            @Override
            public void onResponse(Call<List<Segment>> call, Response<List<Segment>> response) {
                List<Segment> segments = response.body();

                for( Segment segment : segments) {
                    segment.setPointsDecoded(PolyUtil.decode(segment.getPoints()));
                }
            }

            @Override
            public void onFailure(Call<List<Segment>> call, Throwable t) {

            }
        });

    }
}
