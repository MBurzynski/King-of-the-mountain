package com.example.marcin.kingofthemountain;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeScreen extends AppCompatActivity {

    public static int WELCOME_SCREEN_TIME = 1000;
    ImageView logo;
    ImageView mountains;
    Animation fromBottom;
    Animation fromTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);


        logo = findViewById(R.id.logo);
        mountains = findViewById(R.id.mountains);
        fromTop = AnimationUtils.loadAnimation(this,R.anim.from_top);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);
        logo.setAnimation(fromTop);
        mountains.setAnimation(fromBottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainScreen = new Intent(WelcomeScreen.this,MainActivity.class);
                startActivity(mainScreen);
                finish();
            }
        },WELCOME_SCREEN_TIME);

    }
}
