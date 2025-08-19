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
import com.example.physicsmate.model.home.HomeTopic;
import com.example.physicsmate.model.home.HomeTopicAdapter;

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

        List<HomeTopic> homeTopicList = Arrays.asList(
                new HomeTopic("Length Contraction", R.id.lengthContractionFragment),
                new HomeTopic("Time Dilation", R.id.timeDilationFragment)
        );

        HomeTopicAdapter adapter = new HomeTopicAdapter(this, homeTopicList, false);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
