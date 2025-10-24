package com.example.physicsmate.ui.home.maths.VEAdd;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.agog.mathdisplay.MTMathView;
import com.airbnb.paris.Paris;
import com.example.physicsmate.R;
import com.example.physicsmate.ui.CustomKeyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.physicsmate.misc.Custom_methods.*;
import static com.example.physicsmate.misc.MainActivity.getKeyboard;
import static com.example.physicsmate.misc.MainActivity.setLinkedScrollView;

public class VEAddFragment extends Fragment {

    CustomKeyboard customKeyboard = getKeyboard();
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);

        LinearLayout ll = view.findViewById(R.id.linearLayout);

        TextView tv1 = new TextView(getContext());
        tv1.setText("Enter the number of vectors to add");

        EditText et1 = new EditText(getContext());
        et1.setHint("3");

        Button btn1 = new Button(getContext());
        btn1.setText("Cartesian");

        Button btn2 = new Button(getContext());
        btn2.setText("Polar");

        Button btn3 = new Button(getContext());
        btn3.setText("Polar (Direction cosines)");

        Paris.styleBuilder(tv1).add(R.style.custom_textView).apply();
        Paris.styleBuilder(et1).add(R.style.custom_edittext).apply();
        Paris.styleBuilder(btn1).add(R.style.custom_button_enabled).apply();
        Paris.styleBuilder(btn2).add(R.style.custom_button_enabled).apply();
        Paris.styleBuilder(btn3).add(R.style.custom_button_enabled).apply();

        ll.addView(tv1, 0);
        ll.addView(et1, 1);
        ll.addView(btn1, 2);
        ll.addView(btn2, 3);
        ll.addView(btn3, 4);

        setupEditTextChangeListener(null, btn1, null, et1);
        setupEditTextChangeListener(null, btn2, null, et1);
        setupEditTextChangeListener(null, btn3, null, et1);

        btn1.setOnClickListener(v -> {


        });

        btn2.setOnClickListener(v -> {

        });

        btn3.setOnClickListener(v -> {

        });

        return view;
    }
}
