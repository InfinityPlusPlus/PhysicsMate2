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

public class GravitationalForceFragment extends Fragment {

    ScrollView sv;
    CustomKeyboard customKeyboard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);

        CheckBox chkForce, chkM1, chkM2, chkR;
        sv = view.findViewById(R.id.scrollView);

        chkForce = new CheckBox(view.getContext());
        chkM1 = new CheckBox(view.getContext());
        chkM2 = new CheckBox(view.getContext());
        chkR = new CheckBox(view.getContext());

        Paris.styleBuilder(chkForce).add(R.style.custom_checkBox).apply();
        Paris.styleBuilder(chkM1).add(R.style.custom_checkBox).apply();
        Paris.styleBuilder(chkM2).add(R.style.custom_checkBox).apply();
        Paris.styleBuilder(chkR).add(R.style.custom_checkBox).apply();

        chkForce.setText("Force");
        chkM1.setText("M1");
        chkM2.setText("M2");
        chkR.setText("R");

        LinearLayout linear_layout = view.findViewById(R.id.linearLayout);

        linear_layout.addView(chkForce, 0);
        linear_layout.addView(chkM1, 1);
        linear_layout.addView(chkM2, 2);
        linear_layout.addView(chkR, 3);

        final int[] a = {0};
        int max_CB_Required = 3;

        Collection<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(chkForce);
        checkBoxes.add(chkM1);
        checkBoxes.add(chkM2);
        checkBoxes.add(chkR);

        Button btnNext = new Button(view.getContext());
        Button btnClear = new Button(view.getContext());
        Button btnCopy = new Button(view.getContext());
        Button btnCalc = new Button(view.getContext());

        TextView tv1 = new TextView(view.getContext());
        TextView tv2 = new TextView(view.getContext());
        TextView tv3 = new TextView(view.getContext());
        TextView tvRes = new TextView(view.getContext());

        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();

        EditText et1 = new EditText(view.getContext());
        EditText et2 = new EditText(view.getContext());
        EditText et3 = new EditText(view.getContext());

        btnNext.setEnabled(false);
        btnNext.setClickable(false);
        Paris.styleBuilder(btnNext).add(R.style.custom_button_disabled).apply();

        chkForce.setOnClickListener(view2 -> CB_MainMethod(btnNext, btnClear, a, max_CB_Required, checkBoxes, tv1, tv2, tv3, et1, et2, et3, btnCalc, tvRes, btnCopy));
        chkM1.setOnClickListener(view2 -> CB_MainMethod(btnNext, btnClear, a, max_CB_Required, checkBoxes, tv1, tv2, tv3, et1, et2, et3, btnCalc, tvRes, btnCopy));
        chkM2.setOnClickListener(view2 -> CB_MainMethod(btnNext, btnClear, a, max_CB_Required, checkBoxes, tv1, tv2, tv3, et1, et2, et3, btnCalc, tvRes, btnCopy));
        chkR.setOnClickListener(view2 -> CB_MainMethod(btnNext, btnClear, a, max_CB_Required, checkBoxes, tv1, tv2, tv3, et1, et2, et3, btnCalc, tvRes, btnCopy));

        Paris.styleBuilder(btnNext).add(R.style.custom_button_enabled).apply();
        Paris.styleBuilder(btnClear).add(R.style.custom_button_enabled).apply();
        Paris.styleBuilder(btnCopy).add(R.style.custom_button_enabled).apply();

        btnNext.setText("Next");
        btnClear.setText("Clear");
        btnCopy.setText("Copy");

        linear_layout.addView(btnNext, 4);
        linear_layout.addView(btnClear, 5);

        btnCopy.setVisibility(View.GONE);
        btnCalc.setVisibility(View.GONE);
        tvRes.setVisibility(View.GONE);
        btnCopy.setVisibility(View.GONE);

        Paris.styleBuilder(tv1).add(R.style.custom_textView).apply();
        Paris.styleBuilder(tv2).add(R.style.custom_textView).apply();
        Paris.styleBuilder(tv3).add(R.style.custom_textView).apply();

        Paris.styleBuilder(et1).add(R.style.custom_edittext).apply();
        Paris.styleBuilder(et2).add(R.style.custom_edittext).apply();
        Paris.styleBuilder(et3).add(R.style.custom_edittext).apply();

        linear_layout.addView(tv1, 6);
        linear_layout.addView(et1, 7);
        linear_layout.addView(tv2, 8);
        linear_layout.addView(et2, 9);
        linear_layout.addView(tv3, 10);
        linear_layout.addView(et3, 11);

        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        tv3.setVisibility(View.GONE);
        et1.setVisibility(View.GONE);
        et2.setVisibility(View.GONE);
        et3.setVisibility(View.GONE);

        Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();

        btnCalc.setText("Calculate");

        linear_layout.addView(btnCalc, 12);

        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();

        linear_layout.addView(tvRes, 13);
        linear_layout.addView(btnCopy, 14);

        tvRes.setVisibility(View.GONE);

        View[] viewsToDisappear = {btnCopy, tvRes};
        Collection<EditText> editTextList = new ArrayList<>();
        editTextList.add(et1);
        editTextList.add(et2);
        editTextList.add(et3);

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
            String s = checkBoxes.stream().filter(CB -> !CB.isChecked()).findFirst().map(CB -> CB.getText().toString()).orElse("Force");

            //find which checkbox is not checked

            //add textviews to linear layout which is checked
            switch (s) {
                case "Force":
                    tv1.setText("M1 in si units");
                    tv2.setText("M2 in si units");
                    tv3.setText("Distance in si units");
                    break;

                case "M1":
                    tv1.setText("Force in si units");
                    tv2.setText("M2 in si units");
                    tv3.setText("Distance in si units");
                    break;

                case "M2":
                    tv1.setText("Force in si units");
                    tv2.setText("M1 in si units");
                    tv3.setText("Distance in si units");
                    break;

                case "R":
                    tv1.setText("Force in si units");
                    tv2.setText("M1 in si units");
                    tv3.setText("M2 in si units");
                    break;
            }

            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            tv3.setVisibility(View.VISIBLE);
            et1.setVisibility(View.VISIBLE);
            et2.setVisibility(View.VISIBLE);
            et3.setVisibility(View.VISIBLE);

            btnCalc.setVisibility(View.VISIBLE);

            btnCalc.setOnClickListener(view1 -> {

                customKeyboard.hideKeyboard();
                switch (s) {
                    case "Force":
                        double m1 = evalf(et1.getText().toString(), false);
                        double m2 = evalf(et2.getText().toString(), false);
                        double d = evalf(et3.getText().toString(), false);

                        double f = G * m1 * m2 / (d * d);
                        tvRes.setText(Html.fromHtml(HtmlNumberFormatter(getContext(), "The force in si units is " + f)));
                        break;

                    case "M1":
                        double f1 = evalf(et1.getText().toString(), false);
                        double m2_1 = evalf(et2.getText().toString(), false);
                        double d1 = evalf(et3.getText().toString(), false);

                        double m1_1 = f1 * d1 * d1 / (G * m2_1);
                        tvRes.setText(Html.fromHtml(HtmlNumberFormatter(getContext(), "The mass of M1 in si units is " + m1_1)));
                        break;

                    case "M2":
                        double f2 = evalf(et1.getText().toString(), false);
                        double m1_2 = evalf(et2.getText().toString(), false);
                        double d2 = evalf(et3.getText().toString(), false);

                        double m2_2 = f2 * d2 * d2 / (G * m1_2);
                        tvRes.setText(Html.fromHtml(HtmlNumberFormatter(getContext(), "The mass of M2 in si units is " + m2_2)));
                        break;

                    case "R":
                        double f3 = evalf(et1.getText().toString(), false);
                        double m1_3 = evalf(et2.getText().toString(), false);
                        double m2_3 = evalf(et3.getText().toString(), false);

                        double d3 = Math.sqrt((G * m1_3 * m2_3) / f3);
                        tvRes.setText(Html.fromHtml(HtmlNumberFormatter(getContext(), "The distance in si units is " + d3)));
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
            chkForce.setChecked(false);
            chkM1.setChecked(false);
            chkM2.setChecked(false);
            chkR.setChecked(false);

            chkForce.setClickable(true);
            chkM1.setClickable(true);
            chkM2.setClickable(true);
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
