package com.example.marcin.kingofthemountain;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcin.kingofthemountain.StravaAPI.Segment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Marcin on 14-Apr-18.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    
    private Context mContext;

    private Marker lastMarker;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        lastMarker = marker;
        View mWindow = ((Activity)mContext).getLayoutInflater().inflate(R.layout.custom_info_window, null);
        final Segment segment = (Segment) marker.getTag();

        final ImageButton star = mWindow.findViewById(R.id.buttonStar);
        TextView segmentName = mWindow.findViewById(R.id.segmentName);
        TextView headWind = mWindow.findViewById(R.id.headWind);
        TextView tailWind = mWindow.findViewById(R.id.tailWind);
        TextView leftWind = mWindow.findViewById(R.id.leftWind);
        TextView rightWind = mWindow.findViewById(R.id.rightWind);
        TextView distance = mWindow.findViewById(R.id.distance);
        TextView grade = mWindow.findViewById(R.id.grade);



        if(segment.getStarred()){
            star.setImageResource(android.R.drawable.btn_star_big_on);
        }

        segmentName.setText(segment.getName());
        distance.setText(String.format("Dystans: %d m", Math.round(segment.getDistance())));
        if(segment.getAvgGrade()!=null)
            grade.setText(String.format("Nachylenie: " + segment.getAvgGrade()) + " %");
        else
            grade.setText(String.format("Nachylenie: " + segment.getAverageGrade()) + " %");
        headWind.setText(String.format("Pod wiatr: %d %% trasy", Math.round(segment.getWindOnSegment().getPercentageHeadWind())));
        tailWind.setText(String.format("Z wiatrem: %d %% trasy", Math.round(segment.getWindOnSegment().getPercentageTailWind())));
        leftWind.setText(String.format("Wiatr z lewej: %d %% trasy", Math.round(segment.getWindOnSegment().getPercentageLeftWind())));
        rightWind.setText(String.format("Wiatr z prawej: %d %% trasy", Math.round(segment.getWindOnSegment().getPercentageRightWind())));
        return mWindow;
    }

    public Marker getLastMarker() {
        return lastMarker;
    }

}
