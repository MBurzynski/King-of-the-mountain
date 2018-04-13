package com.example.marcin.kingofthemountain;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.marcin.kingofthemountain.OpenWeatherMapAPI.Main;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private final static int REQUEST_FINE_LOCATION = 1;
    private final static float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Map<Polyline, Segment> visibleSegments;
    private List<Marker> visibleMarkers;
    private Wind currentWind;
    private Segment currentSegment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        visibleSegments = new HashMap<>();
        visibleMarkers = new ArrayList<>();
//
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();



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
        getCurrentLocation();


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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StravaAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StravaAPI stravaAPI = retrofit.create(StravaAPI.class);
        Call<SegmentRoot> call = stravaAPI.getSegments(bounds, StravaAPI.TOKEN);


        call.enqueue(new Callback<SegmentRoot>() {
            @Override
            public void onResponse(Call<SegmentRoot> call, Response<SegmentRoot> response) {
                SegmentRoot segmentsData = response.body();
                Toast.makeText(getApplicationContext(),"Udało się pobrać segmenty", Toast.LENGTH_SHORT).show();
                List<Segment> segments = segmentsData.getSegments();
                for( Segment segment:segments){
                    Log.d("ID ", segment.getId().toString());
                    Log.d("NAME ", segment.getName());
                    addSegmentToMap(segment.getPoints(), segment);
                }
            }

            @Override
            public void onFailure(Call<SegmentRoot> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Nie udało się pobrać segmentów: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Błąd: ", t.getMessage());
            }
        });
    }

    private void getWeather(List<Double> latLon){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OpenWeatherMapAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapAPI openWeatherMapAPI = retrofit.create(OpenWeatherMapAPI.class);
        Call<WeatherRoot> call = openWeatherMapAPI.getWeatherByCoords(latLon.get(0).toString(), latLon.get(1).toString(),
                OpenWeatherMapAPI.UNITS, OpenWeatherMapAPI.TOKEN);


        call.enqueue(new Callback<WeatherRoot>() {
            @Override
            public void onResponse(Call<WeatherRoot> call, Response<WeatherRoot> response) {
                WeatherRoot weatherData = response.body();
                Toast.makeText(getApplicationContext(),"Udało się pobrać dane pogodowe", Toast.LENGTH_SHORT).show();
                currentWind = new Wind(weatherData.getWind());
                calculateWindOnSegment();
//                Main weatherMainData = weatherData.getMain();
//                Toast.makeText(getApplicationContext(),
//                        "Temperatura: " + weatherMainData.getTemp().toString() + "\n Wiatr: " + currentWind.getDeg().toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<WeatherRoot> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Nie udało się pobrać danych pogodowych: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Błąd: ", t.getMessage());
            }
        });
    }



    private void moveCamera(LatLng latlng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this,boundsToString(mMap.getProjection().getVisibleRegion().latLngBounds),2*Toast.LENGTH_SHORT).show();
        Log.d("AAAAAAAAAAAAAAAAAAA: ", "pRZED REMOVESEGMENTS");
        removeSegmentsFromMap();
        Log.d("AAAAAAAAAAAAAAAAAAA: ", "PO REMOVESEGMENTS");
        getSegments(boundsToString(mMap.getProjection().getVisibleRegion().latLngBounds));
        Log.d("AAAAAAAAAAAAAAAAAAA: ", "PO GETSEGMENTS");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, "Wind " + String.valueOf(currentWind.getDeg())
                , Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        currentSegment = visibleSegments.get(polyline);
        getWeather(currentSegment.getStartLatlng());


    }

    private void calculateWindOnSegment(){
        WindOnSegment windOnSegment = new WindOnSegment(currentSegment, currentWind);
        Toast.makeText(this, "Wind on segment: " + String.valueOf(windOnSegment.getCurrentWind().getDeg())
                , Toast.LENGTH_SHORT).show();
        windOnSegment.computeWindOnSegment();
        Toast.makeText(this, "Procent trasy z wiatrem z przodu: " + String.valueOf(windOnSegment.getPercentageHeadWind())
                , Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Procent trasy z wiatrem z tyłu: " + String.valueOf(windOnSegment.getPercentageTailWind())
                , Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Procent trasy z wiatrem z prawej: " + String.valueOf(windOnSegment.getPercentageRightWind())
                , Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Procent trasy z wiatrem z lewej: " + String.valueOf(windOnSegment.getPercentageLeftWind())
                , Toast.LENGTH_SHORT).show();
    }




    private String boundsToString(LatLngBounds latLngBounds){
        String[] tempSW = latLngBounds.southwest.toString().split("[(]");
        String southWest = tempSW[1].substring(0,tempSW[1].length()-1);
        String[] tempNE = latLngBounds.northeast.toString().split("[(]");
        String northEast = tempNE[1].substring(0,tempNE[1].length()-1);
        return southWest + "," + northEast;
    }

    private void addSegmentToMap(String polyline, Segment segment){
        segment.setPointsDecoded(PolyUtil.decode(polyline));
        LatLng startPoint = new LatLng(segment.getStartLatlng().get(0),segment.getStartLatlng().get(1));
        LatLng endPoint = new LatLng(segment.getEndLatlng().get(0),segment.getEndLatlng().get(1));
        segment.setStartPoint(startPoint);
        segment.setEndPoint(endPoint);
        Polyline segmentLine = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .addAll(segment.getPointsDecoded()));
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(segment.getPointsDecoded().get(0))
                .title(segment.getName()));
        marker.setTag(segment);

        visibleSegments.put(segmentLine, segment);
        visibleMarkers.add(marker);

    }

    private void removeSegmentsFromMap(){
        for (Map.Entry<Polyline, Segment> entry : visibleSegments.entrySet()){
            entry.getKey().remove();
        }
        for(Marker marker : visibleMarkers){
            marker.remove();
        }
        visibleSegments.clear();
        visibleMarkers.clear();
    }

    public void updateSegmentsOnMap(View view){
        removeSegmentsFromMap();
        getSegments(boundsToString(mMap.getProjection().getVisibleRegion().latLngBounds));
    }



    @Override
    protected void onResume() {
        super.onResume();
    }
}
