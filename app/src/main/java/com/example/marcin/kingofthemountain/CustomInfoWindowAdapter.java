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

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void setWindowText(Marker marker, View view){
        Segment segment = (Segment) marker.getTag();

        TextView segmentName = view.findViewById(R.id.segmentName);
        TextView headWind = view.findViewById(R.id.headWind);
        TextView tailWind = view.findViewById(R.id.tailWind);
        TextView leftWind = view.findViewById(R.id.leftWind);
        TextView rightWind = view.findViewById(R.id.rightWind);
        TextView distance = view.findViewById(R.id.distance);
        TextView grade = view.findViewById(R.id.grade);

        segmentName.setText(segment.getName());
        distance.setText(segment.getDistance().toString() + " m");
        grade.setText(segment.getAvgGrade().toString() + " %");
        headWind.setText("Pod wiatr: " + String.valueOf(Math.round(segment.getWindOnSegment().getPercentageHeadWind())) + "% trasy");
        tailWind.setText("Z wiatrem: " + String.valueOf(Math.round(segment.getWindOnSegment().getPercentageTailWind())) + "% trasy");
        leftWind.setText("Wiatr z lewej: " + String.valueOf(Math.round(segment.getWindOnSegment().getPercentageLeftWind())) + "% trasy");
        rightWind.setText("Wiatr z prawej: " + String.valueOf(Math.round(segment.getWindOnSegment().getPercentageRightWind())) + "% trasy");
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        setWindowText(marker, mWindow);
        return mWindow;
    }
}
