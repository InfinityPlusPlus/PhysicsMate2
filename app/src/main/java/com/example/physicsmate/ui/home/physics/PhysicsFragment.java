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
import com.example.physicsmate.model.Topic;
import com.example.physicsmate.model.TopicAdapter;
import java.util.Arrays;
import java.util.List;

public class PhysicsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_physics, container, false);

        RecyclerView rv = view.findViewById(R.id.rvTopics);

        List<Topic> topicList = Arrays.asList(
                new Topic("Special Relativity", R.id.specialRelativityFragment),
                new Topic("Gravitation", R.id.gravitationFragment)
        );

        TopicAdapter adapter = new TopicAdapter(this, topicList);
        rv.setAdapter(adapter);

        return view;
    }
}
