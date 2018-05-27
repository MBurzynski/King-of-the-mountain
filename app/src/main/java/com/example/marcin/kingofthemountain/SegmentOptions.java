package com.example.marcin.kingofthemountain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class SegmentOptions extends AppCompatActivity{

    SeekBar minDistBar, maxDistBar;
    TextView minDistText, maxDistText, minGradeText, maxGradeText, minTailWindText;
    int minDistVal, maxDistVal;
    SeekBar minGrade, maxGrade, minWind;
    final static int MIN_GRADE = -30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment_options);
        minDistText = findViewById(R.id.textViewMinDistVal);
        maxDistText = findViewById(R.id.textViewMaxDistVal);
        minGradeText = findViewById(R.id.textViewMinGradeVal);
        maxGradeText = findViewById(R.id.textViewMaxGradeVal);
        minTailWindText = findViewById(R.id.textViewMinTailWind);
        minDistBar = findViewById(R.id.seekBarMinDist);
        maxDistBar = findViewById(R.id.seekBarMaxDist);
        minGrade = findViewById(R.id.seekBarMinGrade);
        maxGrade = findViewById(R.id.seekBarMaxGrade);
        minWind = findViewById(R.id.seekBarMinTailWind);
        minDistVal = 0;
        maxDistVal = 100;
        minDistText.setText("0 km");
        maxDistText.setText("100 km");
        minGradeText.setText(Integer.toString(MIN_GRADE + minGrade.getProgress()) + " %");
        maxGradeText.setText(Integer.toString(MIN_GRADE + maxGrade.getProgress()) + " %");
        minTailWindText.setText(Integer.toString(minWind.getProgress()) + " %");



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

        minGrade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int value = MIN_GRADE + i;
                minGradeText.setText(Integer.toString(value) + " %");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        maxGrade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int value = MIN_GRADE + i;
                maxGradeText.setText(Integer.toString(value) + " %");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        minWind.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                minTailWindText.setText(Integer.toString(i) + " %");
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
        intent.putExtra("minGrade", MIN_GRADE + minGrade.getProgress());
        intent.putExtra("maxGrade", MIN_GRADE + maxGrade.getProgress());
        intent.putExtra("minWind", minWind.getProgress());
        startActivity(intent);
    }




}
