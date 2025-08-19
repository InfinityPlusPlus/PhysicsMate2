package com.example.physicsmate.model.home;

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

public class HomeTopicAdapter extends RecyclerView.Adapter<HomeTopicAdapter.TopicViewHolder> {

    private final List<HomeTopic> homeTopics;
    private final Fragment parentFragment;
    private final Boolean isMenu;

    public HomeTopicAdapter(Fragment parentFragment, List<HomeTopic> homeTopics, Boolean isMenu) {
        this.parentFragment = parentFragment;
        this.homeTopics = homeTopics;
        this.isMenu = isMenu;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isMenu)
        {
            view =LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_topic_menu, parent, false);
        }
        else
        {
            view =LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_topic, parent, false);
        }
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        HomeTopic homeTopic = homeTopics.get(position);
        holder.button.setText(homeTopic.title);
        holder.button.setOnClickListener(v ->
                NavHostFragment.findNavController(parentFragment)
                        .navigate(homeTopic.navId)
        );
    }

    @Override
    public int getItemCount() {
        return homeTopics.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        Button button;
        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.btnTopic);
        }
    }
}
