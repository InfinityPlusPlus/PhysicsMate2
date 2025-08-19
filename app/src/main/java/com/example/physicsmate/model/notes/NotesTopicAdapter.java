package com.example.physicsmate.model.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.physicsmate.R;

import java.util.ArrayList;
import java.util.List;

public class NotesTopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TOPIC = 0;
    private static final int TYPE_FORMULA = 1;

    private final List<Object> items; // mix of Topics + Formulas

    public NotesTopicAdapter(List<NotesTopic> notesTopics) {
        items = new ArrayList<>();
        for (NotesTopic t : notesTopics) {
            items.add(t);
            if (t.isExpanded()) items.addAll(t.getFormulas());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof NotesTopic) return TYPE_TOPIC;
        else return TYPE_FORMULA;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_TOPIC) {
            View view = inflater.inflate(R.layout.notes_item_topic, parent, false);
            return new TopicViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.notes_item_formula, parent, false);
            return new FormulaViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_TOPIC) {
            NotesTopic notesTopic = (NotesTopic) items.get(position);
            ((TopicViewHolder) holder).bind(notesTopic);
        } else {
            NotesFormula notesFormula = (NotesFormula) items.get(position);
            ((FormulaViewHolder) holder).bind(notesFormula);
        }
    }

    // ---------------------------
    // Topic ViewHolder
    // ---------------------------
    class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView arrow;

        TopicViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.topic_name);
            arrow = itemView.findViewById(R.id.topic_arrow);
        }

        void bind(NotesTopic notesTopic) {
            name.setText(notesTopic.getName());
            arrow.setRotation(notesTopic.isExpanded() ? 180 : 0);

            itemView.setOnClickListener(v -> {
                notesTopic.setExpanded(!notesTopic.isExpanded());
                rebuildList();
            });
        }
    }

    // ---------------------------
    // Formula ViewHolder
    // ---------------------------
    static class FormulaViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        com.agog.mathdisplay.MTMathView mathView;

        FormulaViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.formula_title);
            mathView = itemView.findViewById(R.id.formula_math);
        }

        void bind(NotesFormula notesFormula) {
            title.setText(notesFormula.getTitle());
            mathView.setLatex(notesFormula.getLatex()); // no $$ needed
        }
    }

    // ---------------------------
    // Helper to rebuild the list when expanding/collapsing
    // ---------------------------
    private void rebuildList() {
        List<NotesTopic> notesTopics = new ArrayList<>();
        for (Object obj : items) {
            if (obj instanceof NotesTopic) notesTopics.add((NotesTopic) obj);
        }
        items.clear();
        for (NotesTopic t : notesTopics) {
            items.add(t);
            if (t.isExpanded()) items.addAll(t.getFormulas());
        }
        notifyDataSetChanged();
    }
}

