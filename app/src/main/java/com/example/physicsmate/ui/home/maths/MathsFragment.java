package com.example.physicsmate.ui.home.maths;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.physicsmate.R;
import org.jetbrains.annotations.NotNull;

public class MathsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView tv = new TextView(getContext());
        tv.setText("Maths Content");
        return tv;
    }
}
