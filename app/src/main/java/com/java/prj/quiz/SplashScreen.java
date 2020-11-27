package com.java.prj.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

public class SplashScreen extends AppCompatActivity {

    public static ContainerClass.User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final SharedPreferences sharedPreferences = this.getSharedPreferences("user" , MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!sharedPreferences.getBoolean("logged", false)) {
                    Intent intent = new Intent(getApplicationContext(), LogIn.class);
                    startActivity(intent);
                }else{
                    user = new ContainerClass.User();
                    user.setCno(sharedPreferences.getString("cno" , ""));
                    user.setCno(sharedPreferences.getString("img" , ""));
                    user.setCno(sharedPreferences.getString("name" , ""));
                    user.setUid(sharedPreferences.getString("uid" , ""));

                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}