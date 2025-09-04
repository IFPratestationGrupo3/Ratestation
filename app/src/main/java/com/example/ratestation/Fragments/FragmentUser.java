package com.example.ratestation.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ratestation.R;
import com.example.ratestation.Adapters.UserPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentUser extends Fragment {

    private ViewPager2 userViewPager;
    private TabLayout userTabLayout;

    public FragmentUser() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);


        // Configuración del ViewPager2 y TabLayout interno
        userViewPager = view.findViewById(R.id.userViewPager);
        userTabLayout = view.findViewById(R.id.userTabLayout);

        UserPagerAdapter adapter = new UserPagerAdapter(this);
        userViewPager.setAdapter(adapter);


        // Apartados de Usuario
        new TabLayoutMediator(userTabLayout, userViewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Tablón");
                            break;
                        case 1:
                            tab.setText("Amigos");
                            break;
                        case 2:
                            tab.setText("Mi espacio");
                            break;
                        case 3:
                            tab.setText("Ajustes");
                            break;
                    }
                }).attach();

        return view;
    }
}
