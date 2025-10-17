package com.example.ratestation.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ratestation.Adapters.MainPagerAdapter;
import com.example.ratestation.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Activity_Main extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        //Cambio de tamaño de fuente de toda la app, FragmentUserAjustes
        SharedPreferences prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String fontSize = prefs.getString("font_size", "normal");
        float scale = fontSize.equals("large") ? 1.2f : 1.0f;

        Configuration configuration = newBase.getResources().getConfiguration();
        configuration.fontScale = scale;

        Context context = newBase.createConfigurationContext(configuration);
        super.attachBaseContext(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        MainPagerAdapter adapter = new MainPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Películas");
                            break;
                        case 1:
                            tab.setText("Series");
                            break;
                        case 2:
                            tab.setText("Juegos");
                            break;
                        case 3:
                            tab.setText("Podcast");
                            break;
                        case 4:
                            tab.setIcon(R.drawable.ic_logo_user);
                            break;
                    }
                }).attach();


    }
}