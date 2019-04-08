package com.example.martin.waslny;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class splash_screen extends AppCompatActivity {

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img =findViewById(R.id.imageView3);
        img.setAnimation(AnimationUtils.loadAnimation(splash_screen.this,R.anim.translate1));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splash_screen.this,sign_in.class));
                finish();
            }
        },5000);
    }


}
