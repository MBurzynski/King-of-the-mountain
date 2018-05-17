package com.example.marcin.kingofthemountain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.marcin.kingofthemountain.StravaAPI.Segment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcin on 15-May-18.
 */

public class ScrollListAdapter extends ArrayAdapter<Segment> {

    private Context mContext;
    private List<Segment> segmentList = new ArrayList<>();

    public ScrollListAdapter(Context context, int resource, List<Segment> list) {
        super(context, resource , list);
        mContext = context;
        segmentList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_element,parent,false);

        Segment currentSegment = segmentList.get(position);

        TextView name = listItem.findViewById(R.id.textViewSegmentName);
        name.setText(currentSegment.getName());

        TextView distance = listItem.findViewById(R.id.textViewDistance);
        distance.setText(currentSegment.getDistance().toString() + " m");

        TextView grade = listItem.findViewById(R.id.textViewGrade);
        grade.setText(currentSegment.getAverageGrade().toString() + " %");

//        TextView wind = listItem.findViewById(R.id.textViewDistance);
//        wind.setText(currentSegment.getWindOnSegment().getPercentageTailWind() + " %");

        return listItem;
    }
}
