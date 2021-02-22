package com.heet.shiningfox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.iv);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AlphaAnimation animation = new AlphaAnimation(0,1);
        //animation.setRepeatCount(4);
        animation.setDuration(500);
        getSupportActionBar().hide();
        imageView.startAnimation(animation);
        preferences = getApplicationContext().getSharedPreferences("Users",0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (preferences.contains("userMobile")){
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }
}