package com.example.marcin.kingofthemountain;

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
        View mWindow = mcontext.getLayoutInflater().inflate(R.layout.custom_info_window, null);
        Segment segment = (Segment) marker.getTag();

        TextView segmentName = mWindow.findViewById(R.id.segmentName);
        TextView headWind = mWindow.findViewById(R.id.headWind);
        TextView tailWind = mWindow.findViewById(R.id.tailWind);
        TextView leftWind = mWindow.findViewById(R.id.leftWind);
        TextView rightWind = mWindow.findViewById(R.id.rightWind);
        TextView distance = mWindow.findViewById(R.id.distance);
        TextView grade = mWindow.findViewById(R.id.grade);

        segmentName.setText(segment.getName());
        distance.setText(segment.getDistance().toString() + " m");
        grade.setText(segment.getAvgGrade().toString() + " %");
        headWind.setText("Pod wiatr: " + String.valueOf(Math.round(segment.getWindOnSegment().getPercentageHeadWind())) + "% trasy");
        tailWind.setText("Z wiatrem: " + String.valueOf(Math.round(segment.getWindOnSegment().getPercentageTailWind())) + "% trasy");
        leftWind.setText("Wiatr z lewej: " + String.valueOf(Math.round(segment.getWindOnSegment().getPercentageLeftWind())) + "% trasy");
        rightWind.setText("Wiatr z prawej: " + String.valueOf(Math.round(segment.getWindOnSegment().getPercentageRightWind())) + "% trasy");
        return mWindow;
    }
}
