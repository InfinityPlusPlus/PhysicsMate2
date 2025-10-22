package com.example.physicsmate.ui.home.maths;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.physicsmate.R;
import com.example.physicsmate.model.home.HomeTopic;
import com.example.physicsmate.model.home.HomeTopicAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MathsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maths, container, false);

        RecyclerView rv = view.findViewById(R.id.rvTopics);

        List<HomeTopic> homeTopicList = Arrays.asList(
                new HomeTopic("Variable Solver", R.id.VarSolverFragment),
                new HomeTopic("Expression Evaluator", R.id.ExprEvaluatorFragment),
                new HomeTopic("Integral Calculator", R.id.IntegralCalculatorFragment),
                new HomeTopic("Derivative Calculator", R.id.DerivativeCalculatorFragment)
        );

        HomeTopicAdapter adapter = new HomeTopicAdapter(this, homeTopicList, true);
        rv.setAdapter(adapter);

        return view;
    }
}