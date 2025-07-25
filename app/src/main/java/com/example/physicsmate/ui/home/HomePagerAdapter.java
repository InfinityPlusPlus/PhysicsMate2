package com.example.physicsmate.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.physicsmate.ui.home.ext_links.ExtLinksFragment;
import com.example.physicsmate.ui.home.maths.MathsFragment;
import com.example.physicsmate.ui.home.physics.PhysicsFragment;

public class HomePagerAdapter extends FragmentStateAdapter {

    public HomePagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: return new MathsFragment();
            case 2: return new ExtLinksFragment();
            default: return new PhysicsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
