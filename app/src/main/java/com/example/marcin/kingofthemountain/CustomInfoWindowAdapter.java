package com.example.marcin.kingofthemountain;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.marcin.kingofthemountain.StravaAPI.Segment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Marcin on 14-Apr-18.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View mWindow = ((Activity)mContext).getLayoutInflater().inflate(R.layout.custom_info_window, null);
        Segment segment = (Segment) marker.getTag();

        TextView segmentName = mWindow.findViewById(R.id.segmentName);
        TextView headWind = mWindow.findViewById(R.id.headWind);
        TextView tailWind = mWindow.findViewById(R.id.tailWind);
        TextView leftWind = mWindow.findViewById(R.id.leftWind);
        TextView rightWind = mWindow.findViewById(R.id.rightWind);
        TextView distance = mWindow.findViewById(R.id.distance);
        TextView grade = mWindow.findViewById(R.id.grade);

        segmentName.setText(segment.getName());
        distance.setText(String.format("Dystans: %d m", Math.round(segment.getDistance())));
        grade.setText(String.format("Nachylenie: " + segment.getAvgGrade()) + " %");
        headWind.setText(String.format("Pod wiatr: %d %% trasy", Math.round(segment.getWindOnSegment().getPercentageHeadWind())));
        tailWind.setText(String.format("Z wiatrem: %d %% trasy", Math.round(segment.getWindOnSegment().getPercentageTailWind())));
        leftWind.setText(String.format("Wiatr z lewej: %d %% trasy", Math.round(segment.getWindOnSegment().getPercentageLeftWind())));
        rightWind.setText(String.format("Wiatr z prawej: %d %% trasy", Math.round(segment.getWindOnSegment().getPercentageRightWind())));
        return mWindow;
    }
}
