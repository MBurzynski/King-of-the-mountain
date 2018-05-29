package com.example.marcin.kingofthemountain;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.marcin.kingofthemountain.OpenWeatherMapAPI.ForecastRoot;
import com.example.marcin.kingofthemountain.OpenWeatherMapAPI.OpenWeatherMapAPI;
import com.example.marcin.kingofthemountain.OpenWeatherMapAPI.WeatherRoot;
import com.example.marcin.kingofthemountain.OpenWeatherMapAPI.Wind;
import com.example.marcin.kingofthemountain.StravaAPI.Segment;
import com.example.marcin.kingofthemountain.StravaAPI.SegmentRoot;
import com.example.marcin.kingofthemountain.StravaAPI.StravaAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private final static int REQUEST_FINE_LOCATION = 1;
    private final static float DEFAULT_ZOOM = 13f;
    private final static float CLOSER_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private List<Segment> receivedSegments;
    private List<Segment> visibleSegments;
    private List<Polyline> visiblePolylines;
    private List<Marker> visibleMarkers;
    private Wind currentWind;
    private Segment currentSegment;
    private int minDist, maxDist, minGrade, maxGrade, minWind;
    boolean now;
    private int year, month, day, hour;
    private boolean starredSegmentsActivity;
    private CustomInfoWindowAdapter customInfoWindowAdapter;

    Retrofit retrofitStrava;
    StravaAPI stravaAPI;
    Retrofit retrofitOpenWeather;
    OpenWeatherMapAPI openWeatherMapAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        receivedSegments = new ArrayList<>();
        visibleSegments = new ArrayList<>();
        visibleMarkers = new ArrayList<>();
        visiblePolylines = new ArrayList<>();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION);
        }

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnPolylineClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        customInfoWindowAdapter = new CustomInfoWindowAdapter(MapsActivity.this);
        mMap.setInfoWindowAdapter(customInfoWindowAdapter);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(customInfoWindowAdapter.getLastMarker() != null){
                    for(Polyline polyline:visiblePolylines){
                        if(polyline.getTag().equals(customInfoWindowAdapter.getLastMarker())){
                            polyline.setColor(Color.BLACK);
                            polyline.setZIndex(1);
                            break;
                        }
                    }
                }
            }
        });


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

        Bundle extras = getIntent().getExtras();
        minDist = extras.getInt("minDist");
        maxDist = extras.getInt("maxDist");
        minGrade = extras.getInt("minGrade");
        maxGrade = extras.getInt("maxGrade");
        minWind = extras.getInt("minWind");

        if(extras.containsKey("year")){
            year = extras.getInt("year");
            month = extras.getInt("month");
            day = extras.getInt("day");
            hour = extras.getInt("hour");
            now = false;
        }
        else
            now = true;

        if(extras.containsKey("starredSegments")){
            starredSegmentsActivity = true;
            for(Segment segment : StarredSegmentsActivity.segmentList){
                addSegmentToMap(segment);
            }
            LatLng startPoint = new LatLng(StarredSegmentsActivity.segmentList.get(0).getStartLatlng().get(0),StarredSegmentsActivity.segmentList.get(0).getStartLatlng().get(1));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint,DEFAULT_ZOOM),2000,null);
        }
        else if(extras.containsKey("starredSegmentID")){
            starredSegmentsActivity = true;
            int id = extras.getInt("starredSegmentID");
            Segment starredSegment = StarredSegmentsActivity.segmentList.get(id);
            Log.d("MAPS","Pobrano ulubiony segment");
            addSegmentToMap(starredSegment);
            LatLng startPoint = new LatLng(starredSegment.getStartLatlng().get(0),starredSegment.getStartLatlng().get(1));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint,DEFAULT_ZOOM),2000,null);
        }
        else {
            starredSegmentsActivity = false;
            getCurrentLocation();
        }
        updateMarkers();

    }

    private void getCurrentLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Location currentLocation = (Location) task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), DEFAULT_ZOOM);
                    }
                    else{
                        Toast.makeText(MapsActivity.this, "Nie udało się ustalić lokalizacji", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (SecurityException e){
            Log.e(TAG, "getCurrentLocation failed: " + e.getMessage());
        }

    }


    private void getSegments(String bounds){
        Call<SegmentRoot> call = stravaAPI.getSegments(bounds, AuthActivity.getStravaAccessToken(this));
        call.enqueue(new Callback<SegmentRoot>() {
            @Override
            public void onResponse(Call<SegmentRoot> call, Response<SegmentRoot> response) {
                SegmentRoot segmentsData = response.body();
                List<Segment> segments = segmentsData.getSegments();
                for( Segment segment : segments){
                    segment.setPointsDecoded(PolyUtil.decode(segment.getPoints()));
                    receivedSegments.add(segment);
                    Log.d("SEGMENT ", "ADDED 1 SEGMENT");
                }
                if(!receivedSegments.isEmpty())
                    getWeather();
            }

            @Override
            public void onFailure(Call<SegmentRoot> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Nie udało się pobrać segmentów: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Błąd: ", t.getMessage());
            }
        });
    }

    private void getWeather(){
        currentSegment = receivedSegments.get(0);
        if(now) {
            Call<WeatherRoot> call = openWeatherMapAPI.getWeatherByCoords(currentSegment.getStartLatlng().get(0).toString()
                    , currentSegment.getStartLatlng().get(1).toString()
                    , OpenWeatherMapAPI.UNITS, OpenWeatherMapAPI.TOKEN);


            call.enqueue(new Callback<WeatherRoot>() {
                @Override
                public void onResponse(Call<WeatherRoot> call, Response<WeatherRoot> response) {
                    WeatherRoot weatherData = response.body();

                    currentWind = new Wind(weatherData.getWind());
                    drawSegmentsRegardingWind();

                }

                @Override
                public void onFailure(Call<WeatherRoot> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Nie udało się pobrać danych pogodowych: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Błąd: ", t.getMessage());
                }
            });
        }
        else {
            final String dateAndTime;
            String monthS, hourS;
            if(hour<10)
                hourS = "0" + String.valueOf(hour);
            else
                hourS = String.valueOf(hour);

            if(month<10)
                monthS = "0" + String.valueOf(month);
            else
                monthS = String.valueOf(month);

            dateAndTime = String.valueOf(year) + "-" + monthS + "-" + String.valueOf(day) + " "
                    + hourS + ":00:00";

            Call<ForecastRoot> call = openWeatherMapAPI.getFutureWeather(currentSegment.getStartLatlng().get(0).toString()
                    , currentSegment.getStartLatlng().get(1).toString()
                    , OpenWeatherMapAPI.UNITS, OpenWeatherMapAPI.TOKEN);

            call.enqueue(new Callback<ForecastRoot>() {
                @Override
                public void onResponse(Call<ForecastRoot> call, Response<ForecastRoot> response) {
                    com.example.marcin.kingofthemountain.OpenWeatherMapAPI.List actualForecast = null;
                    ForecastRoot forecastRoot = response.body();
                    List<com.example.marcin.kingofthemountain.OpenWeatherMapAPI.List> listOfForecasts = forecastRoot.getList();
                    for(com.example.marcin.kingofthemountain.OpenWeatherMapAPI.List forecast : listOfForecasts){
                        if(dateAndTime.equals(forecast.getDtTxt())) {
                            actualForecast = forecast;
                            break;
                        }
                    }
                    try {
                        currentWind = new Wind(actualForecast.getWind());
                        drawSegmentsRegardingWind();
                    }
                    catch (NullPointerException e){
                        e.printStackTrace();
                        Toast.makeText(MapsActivity.this,"Nie znaleziono pogody dla podanego terminu", Toast.LENGTH_SHORT).show();
                    }



                }

                @Override
                public void onFailure(Call<ForecastRoot> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Nie udało się pobrać danych pogodowych: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Błąd: ", t.getMessage());

                }
            });
        }
    }

    private void drawSegmentsRegardingWind(){
        for (Segment receivedSegment : receivedSegments) {
            receivedSegment.calculateWindOnSegment(currentWind);
        }
        Iterator<Segment> i = receivedSegments.iterator();
        while (i.hasNext()) {
            Segment segment = i.next();
            if(!starredSegmentsActivity)
                if (!segment.segmentMeetsConditions(minDist, maxDist, minGrade, maxGrade, minWind))
                    i.remove();
        }
        if (receivedSegments.isEmpty())
            Toast.makeText(MapsActivity.this, "Brak segmentów spełniających podane kryteria w pobliżu", Toast.LENGTH_SHORT).show();
        else {
            for (Segment segment : receivedSegments) {
                addSegmentToMap(segment);
            }
        }
        updateMarkers();
    }



    private void moveCamera(LatLng latlng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked" + String.valueOf(minDist), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for(Polyline polyline:visiblePolylines){
            if(polyline.getTag().equals(marker)){
                polyline.setColor(Color.YELLOW);
                polyline.setZIndex(1);
            }
            else
                polyline.setColor(Color.BLACK);
        }
        return false;
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
        Segment segment = (Segment) marker.getTag();
        segment.setStarred(!segment.getStarred(), this);
        marker.showInfoWindow();
    }



    private String boundsToString(LatLngBounds latLngBounds){
        String[] tempSW = latLngBounds.southwest.toString().split("[(]");
        String southWest = tempSW[1].substring(0,tempSW[1].length()-1);
        String[] tempNE = latLngBounds.northeast.toString().split("[(]");
        String northEast = tempNE[1].substring(0,tempNE[1].length()-1);
        return southWest + "," + northEast;
    }

    private void addSegmentToMap(Segment segment){
        LatLng startPoint = new LatLng(segment.getStartLatlng().get(0),segment.getStartLatlng().get(1));
        LatLng endPoint = new LatLng(segment.getEndLatlng().get(0),segment.getEndLatlng().get(1));
        segment.setStartPoint(startPoint);

        segment.setEndPoint(endPoint);
        Polyline segmentLine = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .addAll(segment.getPointsDecoded()));
        segment.setPolyline(segmentLine);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(segment.getPointsDecoded().get(0))
                .title(segment.getName()));
        marker.setTag(segment);
        segmentLine.setTag(marker);

        visibleSegments.add(segment);
        visiblePolylines.add(segmentLine);
        visibleMarkers.add(marker);

    }

    private void updateMarkers(){
        for(Marker marker : visibleMarkers){
            Segment segmentOnMarker = (Segment) marker.getTag();
            if(segmentOnMarker.getWindOnSegment().getPercentageTailWind() > 50){
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            else if(segmentOnMarker.getWindOnSegment().getPercentageHeadWind() > 50){
                marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
            else{
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            }
        }
    }

    public void removeSegmentsFromMap(View view){
        for (Segment segment : visibleSegments){
            segment.getPolyline().remove();
        }
        for(Marker marker : visibleMarkers){
            marker.remove();
        }
        visibleSegments.clear();
        visiblePolylines.clear();
        visibleMarkers.clear();
    }

    public void updateSegmentsAndWeatherOnMap(View view){
        receivedSegments.clear();
        getSegments(boundsToString(mMap.getProjection().getVisibleRegion().latLngBounds));
    }



    @Override
    protected void onResume() {
        super.onResume();
    }
}
