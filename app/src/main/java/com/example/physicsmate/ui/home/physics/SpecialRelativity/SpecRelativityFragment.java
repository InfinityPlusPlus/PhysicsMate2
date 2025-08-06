package com.example.physicsmate.ui.home.physics.SpecialRelativity;

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

public class SpecRelativityFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.rv_fragment_blank, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        //ScrollView scrollView = view.findViewById(R.id.scrollView);

        List<Topic> topicList = Arrays.asList(
                new Topic("Length Contraction", R.id.lengthContractionFragment),
                new Topic("Time Dilation", R.id.timeDilationFragment)
        );

        TopicAdapter adapter = new TopicAdapter(this, topicList, false);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
