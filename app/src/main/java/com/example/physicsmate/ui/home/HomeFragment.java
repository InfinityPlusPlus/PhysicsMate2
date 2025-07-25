package com.example.physicsmate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.physicsmate.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        //setup view pager
        HomePagerAdapter adapter = new HomePagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        //setup tabs
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 1: tab.setText("Maths"); break;
                case 2: tab.setText("External Links"); break;
                default: tab.setText("Physics");
            }
        }).attach();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}