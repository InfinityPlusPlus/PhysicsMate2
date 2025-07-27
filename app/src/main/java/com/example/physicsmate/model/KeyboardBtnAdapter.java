package com.example.physicsmate.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.paris.Paris;
import com.example.physicsmate.R;

import java.util.List;

import static com.example.physicsmate.Custom_methods.convertDpToPx;
import static com.example.physicsmate.ui.CustomKeyboard.insertText;
import static com.example.physicsmate.ui.CustomKeyboard.targetEditText;

public class KeyboardBtnAdapter extends RecyclerView.Adapter<KeyboardBtnAdapter.KeyboardBtnHolder> {

    private final List<KeyboardBtn> keyboardBtns;
    private final Boolean isYellow;

    public KeyboardBtnAdapter(List<KeyboardBtn> keyboardBtns, Boolean isYellow) {
        this.keyboardBtns = keyboardBtns;
        this.isYellow = isYellow;
    }

    @NonNull
    @Override
    public KeyboardBtnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);

        return new KeyboardBtnHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyboardBtnHolder holder, int position) {
        KeyboardBtn keyboardBtn = keyboardBtns.get(position);
        holder.button.setText(keyboardBtn.label);
        if (isYellow) {
            Paris.styleBuilder(holder.button).add(R.style.custom_button_enabled).apply();
            holder.button.setWidth(convertDpToPx(80));
        }
        else
        {
            Paris.styleBuilder(holder.button).add(R.style.custom_button_enabled_keyboard).apply();
        }
        holder.button.setOnClickListener(view -> insertText(keyboardBtn.stringToInsert));
    }

    @Override
    public int getItemCount() {
        return keyboardBtns.size();
    }

    public static class KeyboardBtnHolder extends RecyclerView.ViewHolder {
        Button button;

        public KeyboardBtnHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.btnTopic);
        }
    }
}
