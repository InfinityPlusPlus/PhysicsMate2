package com.example.physicsmate.ui.home.physics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.NotNull;

public class PhysicsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView tv = new TextView(getContext());
        tv.setText("Physics Content");
        return tv;
    }
}
