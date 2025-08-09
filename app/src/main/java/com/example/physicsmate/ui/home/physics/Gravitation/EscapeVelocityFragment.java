package com.example.physicsmate.ui.home.physics.Gravitation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.Html;
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

import java.util.ArrayList;
import java.util.Collection;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.physicsmate.misc.Custom_methods.*;
import static com.example.physicsmate.misc.MainActivity.getKeyboard;
import static com.example.physicsmate.misc.MainActivity.setLinkedScrollView;
import static com.example.physicsmate.ui.CustomKeyboard.G;
import static java.lang.Math.sqrt;

public class EscapeVelocityFragment extends Fragment {

    ScrollView sv;
    CustomKeyboard customKeyboard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);

        CheckBox chkEscVel, chkM, chkR;
        sv = view.findViewById(R.id.scrollView);

        chkEscVel = new CheckBox(view.getContext());
        chkM = new CheckBox(view.getContext());
        chkR = new CheckBox(view.getContext());

        Paris.styleBuilder(chkEscVel).add(R.style.custom_checkBox).apply();
        Paris.styleBuilder(chkM).add(R.style.custom_checkBox).apply();
        Paris.styleBuilder(chkR).add(R.style.custom_checkBox).apply();

        chkEscVel.setText("Escape Velocity");
        chkM.setText("M");
        chkR.setText("R");

        LinearLayout linear_layout = view.findViewById(R.id.linearLayout);

        linear_layout.addView(chkEscVel, 0);
        linear_layout.addView(chkM, 1);
        linear_layout.addView(chkR, 2);

        final int[] a = {0};
        int max_CB_Required = 2;

        Collection<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(chkEscVel);
        checkBoxes.add(chkM);
        checkBoxes.add(chkR);

        Button btnNext = new Button(view.getContext());
        Button btnClear = new Button(view.getContext());
        Button btnCopy = new Button(view.getContext());
        Button btnCalc = new Button(view.getContext());

        TextView tv1 = new TextView(view.getContext());
        TextView tv2 = new TextView(view.getContext());
        TextView tvRes = new TextView(view.getContext());

        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();

        EditText et1 = new EditText(view.getContext());
        EditText et2 = new EditText(view.getContext());

        btnNext.setEnabled(false);
        btnNext.setClickable(false);
        Paris.styleBuilder(btnNext).add(R.style.custom_button_disabled).apply();

        chkEscVel.setOnClickListener(view2 -> CB_MainMethod(btnNext, btnClear, a, max_CB_Required, checkBoxes, tv1, tv2, et1, et2, btnCalc, tvRes, btnCopy));
        chkM.setOnClickListener(view2 -> CB_MainMethod(btnNext, btnClear, a, max_CB_Required, checkBoxes, tv1, tv2, et1, et2, btnCalc, tvRes, btnCopy));
        chkR.setOnClickListener(view2 -> CB_MainMethod(btnNext, btnClear, a, max_CB_Required, checkBoxes, tv1, tv2, et1, et2, btnCalc, tvRes, btnCopy));

        Paris.styleBuilder(btnNext).add(R.style.custom_button_enabled).apply();
        Paris.styleBuilder(btnClear).add(R.style.custom_button_enabled).apply();
        Paris.styleBuilder(btnCopy).add(R.style.custom_button_enabled).apply();

        btnNext.setText("Next");
        btnClear.setText("Clear");
        btnCopy.setText("Copy");

        linear_layout.addView(btnNext, 3);
        linear_layout.addView(btnClear, 4);

        btnCopy.setVisibility(View.GONE);
        btnCalc.setVisibility(View.GONE);
        tvRes.setVisibility(View.GONE);
        btnCopy.setVisibility(View.GONE);

        Paris.styleBuilder(tv1).add(R.style.custom_textView).apply();
        Paris.styleBuilder(tv2).add(R.style.custom_textView).apply();

        Paris.styleBuilder(et1).add(R.style.custom_edittext).apply();
        Paris.styleBuilder(et2).add(R.style.custom_edittext).apply();

        linear_layout.addView(tv1, 5);
        linear_layout.addView(et1, 6);
        linear_layout.addView(tv2, 7);
        linear_layout.addView(et2, 8);

        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        et1.setVisibility(View.GONE);
        et2.setVisibility(View.GONE);

        Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();

        btnCalc.setText("Calculate");

        linear_layout.addView(btnCalc, 9);

        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();

        linear_layout.addView(tvRes, 10);
        linear_layout.addView(btnCopy, 11);

        tvRes.setVisibility(View.GONE);

        View[] viewsToDisappear = {btnCopy, tvRes};
        Collection<EditText> editTextList = new ArrayList<>();
        editTextList.add(et1);
        editTextList.add(et2);

        linear_layout.post(() -> {
            customKeyboard = getKeyboard();
            if (customKeyboard != null) {
                customKeyboard.hideKeyboard();
                setupEditTextForCustomKeyboard(customKeyboard, sv, editTextList);
                setupEditTextChangeListener(viewsToDisappear, btnCalc, customKeyboard, editTextList);
            }
        });

        setLinkedScrollView(sv);

        btnNext.setOnClickListener(view2 -> {
            customKeyboard.hideKeyboard();
            String s = checkBoxes.stream().filter(CB -> !CB.isChecked()).findFirst().map(CB -> CB.getText().toString()).orElse("Escape velocity");

            //find which checkbox is not checked

            //add textviews to linear layout which is checked
            switch (s) {
                case "Escape Velocity":
                    tv1.setText("M in si units");
                    tv2.setText("R in si units");
                    break;

                case "M":
                    tv1.setText("Escape velocity in si units");
                    tv2.setText("R in si units");
                    break;

                case "R":
                    tv1.setText("Escape velocity in si units");
                    tv2.setText("M in si units");
                    break;
            }

            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            et1.setVisibility(View.VISIBLE);
            et2.setVisibility(View.VISIBLE);

            btnCalc.setVisibility(View.VISIBLE);

            btnCalc.setOnClickListener(view1 -> {

                customKeyboard.hideKeyboard();
                double m, v, r;
                switch (s) {
                    case "Escape Velocity":
                        m = evalf(et1.getText().toString(), false);
                        r = evalf(et2.getText().toString(), false);

                        v = sqrt(2 * G * m / r);
                        tvRes.setText(Html.fromHtml(HtmlNumberFormatter(getContext(), "The Escape velocity in si units is " + v)));
                        break;

                    case "M":
                        v = evalf(et1.getText().toString(), false);
                        r = evalf(et2.getText().toString(), false);

                        m = v * v * r/ (2 * G);
                        tvRes.setText(Html.fromHtml(HtmlNumberFormatter(getContext(), "The mass of M in si units is " + m)));
                        break;

                    case "R":
                        v = evalf(et1.getText().toString(), false);
                        m = evalf(et2.getText().toString(), false);

                        r = 2 * G * m / (v*v);
                        tvRes.setText(Html.fromHtml(HtmlNumberFormatter(getContext(), "The distance in si units is " + r)));
                        break;
                }

                tvRes.setVisibility(View.VISIBLE);
                btnCopy.setVisibility(View.VISIBLE);

                sv.post(() -> sv.smoothScrollTo(0, btnCopy.getBottom()));

            });

            btnCopy.setOnClickListener(view1 -> {
                ClipboardManager clipboard = getSystemService(view.getContext(), ClipboardManager.class);
                ClipData clip = ClipData.newPlainText("Copied Text", extractNumberFromString(tvRes.getText().toString()));
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(view.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            });

        });

        btnClear.setOnClickListener(view2 -> {
            chkEscVel.setChecked(false);
            chkM.setChecked(false);
            chkR.setChecked(false);

            chkEscVel.setClickable(true);
            chkM.setClickable(true);
            chkR.setClickable(true);

            btnNext.setClickable(false);
            btnNext.setEnabled(false);
            btnNext.setBackgroundResource(R.drawable.btn_disabled);
            btnClear.setVisibility(View.GONE);

            a[0] = 0;
        });
        return view;
    }
}
