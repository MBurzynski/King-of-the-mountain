package com.example.marcin.kingofthemountain;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button searchSegments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void searchSegments(View view)
    {
        Intent intent = new Intent(MainActivity.this, com.example.marcin.kingofthemountain.MapsActivity.class);
        startActivity(intent);
    }


}
