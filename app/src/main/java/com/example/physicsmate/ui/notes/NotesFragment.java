package com.example.physicsmate.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.physicsmate.databinding.FragmentNotesBinding;
import com.example.physicsmate.model.notes.NotesTopicAdapter;

public class NotesFragment extends Fragment {

    private FragmentNotesBinding binding;
    private NotesTopicAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 1. ViewModel
        NotesViewModel notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        // 2. Inflate binding
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 3. Setup RecyclerView
        RecyclerView recyclerView = binding.recyclerNotes;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 4. Observe data
        notesViewModel.getNotesTopics().observe(getViewLifecycleOwner(), topics -> {
            // refresh data
            adapter = new NotesTopicAdapter(topics);
            recyclerView.setAdapter(adapter);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}