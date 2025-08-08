package com.example.ratestation;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class UserPagerAdapter extends FragmentStateAdapter {

    public UserPagerAdapter(@NonNull Fragment faUser) {super(faUser);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new FragmentUserTablon();
            case 1: return new FragmentUserAmigos();
            case 2: return new FragmentUserEspacio();
            case 3: return new FragmentUserAjustes();
            default: return new FragmentUserTablon();
        }
    }

    @Override
    public int getItemCount() {return 4;}
}