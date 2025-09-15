package com.example.ratestation.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ratestation.Fragments.FragmentJuegos;
import com.example.ratestation.Fragments.FragmentPeliculas;
import com.example.ratestation.Fragments.FragmentPodcast;
import com.example.ratestation.Fragments.FragmentSeries;
import com.example.ratestation.Fragments.FragmentUser;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new FragmentPeliculas();
            case 1: return new FragmentSeries();
            case 2: return new FragmentJuegos();
            case 3: return new FragmentPodcast();
            case 4: return new FragmentUser();
            default: return new FragmentPeliculas();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}