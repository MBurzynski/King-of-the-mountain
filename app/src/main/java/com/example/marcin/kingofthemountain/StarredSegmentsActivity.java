package com.example.marcin.kingofthemountain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    private ArrayList<Segment> segmentList;
    private String TAG = "STARRED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred_segments);

        starredSegments = findViewById(R.id.listViewStarredSegments);

        segmentList = new ArrayList<>();
        getStarredSegments();



    }

    private void getStarredSegments(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StravaAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d(TAG, "Before API request");

        StravaAPI stravaAPI = retrofit.create(StravaAPI.class);
        Call<List<Segment>> call = stravaAPI.listStarredSegments("100",AuthActivity.getStravaAccessToken(this));

        call.enqueue(new Callback<List<Segment>>() {
            @Override
            public void onResponse(Call<List<Segment>> call, Response<List<Segment>> response) {

                Log.d(TAG, "API request PASSED");
                List<Segment> segments = response.body();

                for( Segment segment : segments) {
                    segmentList.add(segment);
                    Log.d(TAG, "Segment ADDED");
                }
                scrollListAdapter = new ScrollListAdapter(StarredSegmentsActivity.this, R.layout.list_element, segmentList);
                starredSegments.setAdapter(scrollListAdapter);
                starredSegments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d(TAG, "Segment CLICKED");
                        Intent intent = new Intent(StarredSegmentsActivity.this, MapsActivity.class);
                        Segment segment = (Segment) adapterView.getItemAtPosition(i);
                        Integer id = segment.getId();
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });
                Log.d(TAG, "List adapter SET");
            }

            @Override
            public void onFailure(Call<List<Segment>> call, Throwable t) {
                Log.d(TAG, "API request FAILED");
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT);
            }
        });

    }
}
