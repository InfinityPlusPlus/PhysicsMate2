package com.example.physicsmate.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.physicsmate.R;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private final List<Topic> topics;
    private final Fragment parentFragment;

    public TopicAdapter(Fragment parentFragment, List<Topic> topics) {
        this.parentFragment = parentFragment;
        this.topics = topics;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topics.get(position);
        holder.button.setText(topic.title);
        holder.button.setOnClickListener(v ->
                NavHostFragment.findNavController(parentFragment)
                        .navigate(topic.navId)
        );
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        Button button;
        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.btnTopic);
        }
    }
}
