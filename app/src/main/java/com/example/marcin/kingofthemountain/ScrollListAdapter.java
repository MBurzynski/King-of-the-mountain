package com.example.marcin.kingofthemountain;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marcin.kingofthemountain.StravaAPI.Segment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Marcin on 15-May-18.
 */

public class ScrollListAdapter extends ArrayAdapter<Segment> {

    private Context mContext;

    List<Segment> segmentList = new ArrayList<>();

    public ScrollListAdapter(Context context, int resource, List<Segment> list) {
        super(context, resource , list);
        mContext = context;
        segmentList = list;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_element,parent,false);

        final Segment currentSegment = segmentList.get(position);

        final ImageButton star = listItem.findViewById(R.id.imageButton);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentSegment.getStarred()) {
                    currentSegment.setStarred(false, mContext);
                    star.setImageResource(android.R.drawable.btn_star_big_off);
                }
                else {
                    currentSegment.setStarred(true, mContext);
                    star.setImageResource(android.R.drawable.btn_star_big_on);
                }
            }
        });

        TextView name = listItem.findViewById(R.id.textViewSegmentName);
        name.setText(currentSegment.getName());

        TextView distance = listItem.findViewById(R.id.textViewDistance);
        String dist = String.valueOf(Math.round(currentSegment.getDistance()));
        distance.setText(dist + " m");

        TextView grade = listItem.findViewById(R.id.textViewGrade);
        grade.setText(currentSegment.getAverageGrade().toString() + " %");

        TextView wind = listItem.findViewById(R.id.textViewTailWind);
        String windValue = String.valueOf(Math.round(currentSegment.getWindOnSegment().getPercentageTailWind()));
        wind.setText("Wiatr z tyłu: " + windValue + " %");

        return listItem;
    }

    public List<Segment> getSegmentList() {
        return segmentList;
    }
}
