package com.example.physicsmate.ui.home.maths.VEAdd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.airbnb.paris.Paris;
import com.example.physicsmate.R;
import com.example.physicsmate.ui.CustomKeyboard;

import static com.example.physicsmate.misc.Custom_methods.*;
import static com.example.physicsmate.misc.MainActivity.getKeyboard;

public class VEAddFragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);
        CustomKeyboard customKeyboard = getKeyboard();

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

        setupEditTextChangeListener(null, btn1, customKeyboard, et1);
        setupEditTextChangeListener(null, btn2, customKeyboard, et1);
        setupEditTextChangeListener(null, btn3, customKeyboard, et1);

        btn1.setOnClickListener(v -> {
            Cartesian cart = new Cartesian();

            Bundle args = new Bundle();
            args.putString("num", String.valueOf(evalf(et1.getText().toString().trim())));
            cart.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.cl_content_main, cart) // Use your host container ID
                    .addToBackStack(null)
                    .commit();
        });

        btn2.setOnClickListener(v -> {
            Polar polar = new Polar();

            Bundle args = new Bundle();
            args.putInt("num", Integer.parseInt(String.valueOf(evalf(et1.getText().toString().trim()))));
            polar.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.cl_content_main, polar) // Use your host container ID
                    .addToBackStack(null)
                    .commit();

        });

        btn3.setOnClickListener(v -> {
            PolarDirectionCosines polDirCos = new PolarDirectionCosines();

            Bundle args = new Bundle();
            args.putString("num", String.valueOf(evalf(et1.getText().toString().trim())));
            polDirCos.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.cl_content_main, polDirCos) // Use your host container ID
                    .addToBackStack(null)
                    .commit();

        });

        return view;
    }

    //a method, which will surrounded a given text with brackets, if it is not already surrounded by brackets
    public static String addBrackets(String s) {
        if (s.charAt(0) != '(') {
            s = "(" + s;
        }
        if (s.charAt(s.length() - 1) != ')') {
            s = s + ")";
        }
        return s;
    }

    //a method to check if the given string is a scalar (no comma)
    public static boolean isScalar(String s) {
        return !s.contains(",");
    }

}
