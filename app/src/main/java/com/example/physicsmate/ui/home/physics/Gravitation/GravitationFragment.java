package com.example.physicsmate.ui.home.physics.Gravitation;

import android.os.Bundle;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.physicsmate.R;
import com.example.physicsmate.model.Topic;
import com.example.physicsmate.model.TopicAdapter;
import com.example.physicsmate.ui.CustomKeyboard;

import java.util.Arrays;
import java.util.List;

public class GravitationFragment extends Fragment {

    static final float fontSize = 72.f;
    protected static CustomKeyboard customKeyboard;
    protected static ScrollView sv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.rv_fragment_blank, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        List<Topic> topicList = Arrays.asList(
                new Topic("Gravitational Force", R.id.gravitationalForceFragment),
                new Topic("Gravitational Potential", R.id.gravitationalPotentialFragment),
                new Topic("Gravitational Potential Energy", R.id.gravitationalPotentialEnergyFragment),
                new Topic("Gravitational Acceleration", R.id.gravitationalAccelerationFragment),
                new Topic("Escape Velocity", R.id.escapeVelocityFragment),
                new Topic("Orbital velocity", R.id.orbitalVelocityFragment),
                new Topic("Kepler's Law", R.id.keplersLawFragment),
                new Topic("Jeans's Mass", R.id.jeansMassFragment),
                new Topic("Schwarzschild Radius", R.id.schwarzschildRadiusFragment)
        );

        TopicAdapter adapter = new TopicAdapter(this, topicList, false);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;

    }

}
