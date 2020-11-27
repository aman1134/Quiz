package com.java.prj.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.java.prj.quiz.ContainerClass;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FrameLayout frameLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView menuicon;
    public static TextView title;
    public static ContainerClass.Quiz quiz = new ContainerClass.Quiz();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().getStringExtra("act") != null && getIntent().getStringExtra("act").equals("img")){
            FragmentManager fm4 = getSupportFragmentManager();
            FragmentTransaction ft4 = fm4.beginTransaction();
            ft4.replace(R.id.fragment, new FragmentProfile());
            ft4.commit();
        }

        frameLayout = (FrameLayout) findViewById(R.id.fragment);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        title = (TextView) findViewById(R.id.title);
        menuicon = (ImageView) findViewById(R.id.menu);

        frameLayout.removeAllViews();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, new FragmentHome());
        ft.commit();

        title.setText("Home");

        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);
                switch(item.getItemId()){
                    case R.id.home:
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragment, new FragmentHome());
                        ft.commit();
                        break;

                    case R.id.profile:
                        FragmentManager fm4 = getSupportFragmentManager();
                        FragmentTransaction ft4 = fm4.beginTransaction();
                        ft4.replace(R.id.fragment, new FragmentProfile());
                        ft4.commit();
                        break;

                    case R.id.About:
                        FragmentManager fm1= getSupportFragmentManager();
                        FragmentTransaction ft1 = fm1.beginTransaction();
                        ft1.replace(R.id.fragment, new FragmentAbout());
                        ft1.commit();
                        break;

                    case R.id.search_quiz:
                        FragmentManager fm3 = getSupportFragmentManager();
                        FragmentTransaction ft3 = fm3.beginTransaction();
                        ft3.replace(R.id.fragment, new FragmentSearch());
                        ft3.commit();
                        break;

                    case R.id.history:
                        FragmentManager fm2 = getSupportFragmentManager();
                        FragmentTransaction ft2 = fm2.beginTransaction();
                        ft2.replace(R.id.fragment, new FragmentHistory());
                        ft2.commit();
                        break;

                    case R.id.Logout:
                        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("user", MODE_PRIVATE);
                        sharedPreferences.edit().putBoolean("logged", false).apply();
                        FirebaseAuth.getInstance().signOut();
                        finishAffinity();
                        startActivity(new Intent(MainActivity.this,LogIn.class));
                        break;
                }
                return false;
            }
        });

    }
}