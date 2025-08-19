package com.example.physicsmate.ui.home.physics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.physicsmate.R;
import com.example.physicsmate.model.home.HomeTopic;
import com.example.physicsmate.model.home.HomeTopicAdapter;
import java.util.Arrays;
import java.util.List;

public class PhysicsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_physics, container, false);

        RecyclerView rv = view.findViewById(R.id.rvTopics);

        List<HomeTopic> homeTopicList = Arrays.asList(
                new HomeTopic("Special Relativity", R.id.specialRelativityFragment),
                new HomeTopic("Gravitation", R.id.gravitationFragment)
        );

        HomeTopicAdapter adapter = new HomeTopicAdapter(this, homeTopicList, true);
        rv.setAdapter(adapter);

        return view;
    }
}
