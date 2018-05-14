package com.example.marcin.kingofthemountain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

public class SegmentOptions extends AppCompatActivity{

    SeekBar minDistBar, maxDistBar;
    TextView minDistText, maxDistText;
    int minDistVal, maxDistVal;
    ScrollableNumberPicker minGrade, maxGrade, minWind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment_options);
        minDistText = findViewById(R.id.textViewMinDistVal);
        maxDistText = findViewById(R.id.textViewMaxDistVal);
        minDistBar = findViewById(R.id.seekBarMinDist);
        maxDistBar = findViewById(R.id.seekBarMaxDist);
        minGrade = findViewById(R.id.numberPickerMinGrade);
        maxGrade = findViewById(R.id.numberPickerMaxGrade);
        minWind = findViewById(R.id.numberPickerMinWind);
        minDistVal = 0;
        maxDistVal = 100;
        minDistText.setText("0 km");
        maxDistText.setText("100 km");



        minDistBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                minDistText.setText(Integer.toString(i) + " km");
                minDistVal = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        maxDistBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                maxDistText.setText(Integer.toString(i) + " km");
                maxDistVal = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void searchSegments(View view)
    {
        Intent intent = new Intent(SegmentOptions.this, com.example.marcin.kingofthemountain.MapsActivity.class);
        intent.putExtra("minDist", minDistVal);
        intent.putExtra("maxDist", maxDistVal);
        intent.putExtra("minGrade", minGrade.getValue());
        intent.putExtra("maxGrade", maxGrade.getValue());
        intent.putExtra("minWind", minWind.getValue());
        startActivity(intent);
    }




}
