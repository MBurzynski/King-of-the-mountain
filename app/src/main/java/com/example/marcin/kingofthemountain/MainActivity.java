package com.example.marcin.kingofthemountain;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView token = findViewById(R.id.textViewToken);
        token.setText(AuthActivity.getStravaAccessToken(this));


    }


    public void pickOptions(View view)
    {
        Intent intent = new Intent(MainActivity.this, com.example.marcin.kingofthemountain.SegmentOptions.class);
        startActivity(intent);
    }

    public void searchStarredSegments(View view)
    {
        Intent intent = new Intent(MainActivity.this, com.example.marcin.kingofthemountain.StarredSegmentsActivity.class);
        startActivity(intent);
    }


}
