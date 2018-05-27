package com.example.marcin.kingofthemountain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marcin.kingofthemountain.OpenWeatherMapAPI.OpenWeatherMapAPI;
import com.example.marcin.kingofthemountain.OpenWeatherMapAPI.WeatherRoot;
import com.example.marcin.kingofthemountain.OpenWeatherMapAPI.Wind;
import com.example.marcin.kingofthemountain.StravaAPI.Segment;
import com.example.marcin.kingofthemountain.StravaAPI.SegmentRoot;
import com.example.marcin.kingofthemountain.StravaAPI.StravaAPI;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StarredSegmentsActivity extends AppCompatActivity {

    private ListView starredSegments;
    private ScrollListAdapter scrollListAdapter;
    public static List<Segment> segmentList;
    private String TAG = "STARRED";
    private Wind currentWind;

    Retrofit retrofitStrava;
    StravaAPI stravaAPI;
    Retrofit retrofitOpenWeather;
    OpenWeatherMapAPI openWeatherMapAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred_segments);

        starredSegments = findViewById(R.id.listViewStarredSegments);

        segmentList = new ArrayList<>();

        retrofitStrava = new Retrofit.Builder()
                .baseUrl(StravaAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stravaAPI = retrofitStrava.create(StravaAPI.class);

        retrofitOpenWeather = new Retrofit.Builder()
                .baseUrl(OpenWeatherMapAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherMapAPI = retrofitOpenWeather.create(OpenWeatherMapAPI.class);

        scrollListAdapter = new ScrollListAdapter(StarredSegmentsActivity.this, R.layout.list_element, segmentList);
        starredSegments.setAdapter(scrollListAdapter);
        starredSegments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "Segment CLICKED");
                Intent intent = new Intent(StarredSegmentsActivity.this, MapsActivity.class);
//                Segment segment = (Segment) adapterView.getItemAtPosition(i);
                intent.putExtra("starredSegmentID", i);
                 startActivity(intent);
            }
        });
        Log.d(TAG, "List adapter SET");

        getStarredSegments();





    }

    public void getStarredSegments(){

        Log.d(TAG, "Before API request");

        stravaAPI = retrofitStrava.create(StravaAPI.class);
        Call<List<Segment>> call = stravaAPI.listStarredSegments("100",AuthActivity.getStravaAccessToken(this));

        call.enqueue(new Callback<List<Segment>>() {
            @Override
            public void onResponse(Call<List<Segment>> call, Response<List<Segment>> response) {

                Log.d(TAG, "API request PASSED");
                final List<Segment> segments = response.body();

                for( final Segment segment : segments) {
                    stravaAPI = retrofitStrava.create(StravaAPI.class);
                    Call<Segment> callSegment = stravaAPI.getSegment(segment.getId().toString(),AuthActivity.getStravaAccessToken(getApplicationContext()));
                    callSegment.enqueue(new Callback<Segment>() {
                        @Override
                        public void onResponse(Call<Segment> call, Response<Segment> response) {
                            Segment segment1 = response.body();
                            segment1.setPoints(segment1.getMap().getPolyline());
                            segment1.setPointsDecoded(PolyUtil.decode(segment1.getPoints()));

                            getWeather(segment1);
                            Log.d(TAG, "Segment ADDED");

                        }


                        @Override
                        public void onFailure(Call<Segment> call, Throwable t) {

                        }
                    });

                }


            }

            @Override
            public void onFailure(Call<List<Segment>> call, Throwable t) {
                Log.d(TAG, "API request FAILED");
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT);
            }
        });

    }

    private void getWeather(final Segment segment){

        Call<WeatherRoot> call = openWeatherMapAPI.getWeatherByCoords(segment.getStartLatlng().get(0).toString()
                ,segment.getStartLatlng().get(1).toString()
                ,OpenWeatherMapAPI.UNITS, OpenWeatherMapAPI.TOKEN);


        call.enqueue(new Callback<WeatherRoot>() {
            @Override
            public void onResponse(Call<WeatherRoot> call, Response<WeatherRoot> response) {
                WeatherRoot weatherData = response.body();
                currentWind = new Wind(weatherData.getWind());
                segment.calculateWindOnSegment(currentWind);
                segmentList.add(segment);
                scrollListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<WeatherRoot> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Nie udało się pobrać danych pogodowych: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Błąd: ", t.getMessage());
            }
        });
    }

    public void displayStarredSegments(View view){
        Intent intent = new Intent(StarredSegmentsActivity.this, MapsActivity.class);
        ArrayList<String> segmentIds= new ArrayList<>();
        for(Segment segment : segmentList){
            segmentIds.add(segment.getId().toString());
        }
        intent.putStringArrayListExtra("starredSegments", segmentIds);
        startActivity(intent);
    }
}
