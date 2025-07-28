package com.example.physicsmate.ui.home.physics.SpecialRelativity;

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
import static com.example.physicsmate.Custom_methods.*;
import static com.example.physicsmate.MainActivity.getKeyboard;
import static com.example.physicsmate.MainActivity.setLinkedScrollView;

public class LengthContractionFragment extends Fragment {

    final double c = 299792458;
    ScrollView sv;
    CustomKeyboard customKeyboard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);

        CheckBox chkProperLength, chkContractedLength, chkVelocity;
        sv = view.findViewById(R.id.scrollView);

        chkProperLength = new CheckBox(view.getContext());
        chkContractedLength = new CheckBox(view.getContext());
        chkVelocity = new CheckBox(view.getContext());

        Paris.styleBuilder(chkProperLength).add(R.style.custom_checkBox).apply();
        Paris.styleBuilder(chkContractedLength).add(R.style.custom_checkBox).apply();
        Paris.styleBuilder(chkVelocity).add(R.style.custom_checkBox).apply();

        chkProperLength.setText("Proper Length");
        chkContractedLength.setText("Contracted Length");
        chkVelocity.setText("Velocity");

        LinearLayout linear_layout = view.findViewById(R.id.linearLayout);

        linear_layout.addView(chkProperLength, 0);
        linear_layout.addView(chkContractedLength, 1);
        linear_layout.addView(chkVelocity, 2);

        final int[] a = {0};
        int max_CB_Required = 2;

        Collection<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(chkProperLength);
        checkBoxes.add(chkContractedLength);
        checkBoxes.add(chkVelocity);

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

        chkProperLength.setOnClickListener(view2 -> CB_MainMethod(btnNext, btnClear, a, max_CB_Required, checkBoxes, tv1, tv2, et1, et2, btnCalc, tvRes, btnCopy));

        chkContractedLength.setOnClickListener(view2 -> CB_MainMethod(btnNext, btnClear, a, max_CB_Required, checkBoxes, tv1, tv2, et1, et2, btnCalc, tvRes, btnCopy));

        chkVelocity.setOnClickListener(view2 -> CB_MainMethod(btnNext, btnClear, a, max_CB_Required, checkBoxes, tv1, tv2, et1, et2, btnCalc, tvRes, btnCopy));

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
                setupEditTextForCustomKeyboard(customKeyboard, sv, et1, et2);
                setupEditTextChangeListener(viewsToDisappear, btnCalc, customKeyboard, editTextList);
            }
        });

        setLinkedScrollView(sv);

        btnNext.setOnClickListener(view2 -> {
            customKeyboard.hideKeyboard();
            String s = checkBoxes.stream().filter(CB -> !CB.isChecked()).findFirst().map(CB -> CB.getText().toString()).orElse("Contracted Length");

            //find which checkbox is not checked

            //add textviews to linear layout which is checked
            switch (s) {
                case "Proper Length":
                    tv1.setText("Enter the contracted length of the moving object, as viewed from the stationary observer");
                    tv2.setText("Enter the velocity of the moving object, as viewed from the stationary observer");
                    break;
                case "Contracted Length":
                    tv1.setText("Enter the proper length of the moving object, as viewed from the stationary observer");
                    tv2.setText("Enter the velocity of the moving object, as viewed from the stationary observer");
                    break;
                case "Velocity":
                    tv1.setText("Enter the proper length of the moving object, as viewed from the stationary observer");
                    tv2.setText("Enter the contracted length of the moving object, as viewed from the stationary observer");
                    break;
            }

            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            et1.setVisibility(View.VISIBLE);
            et2.setVisibility(View.VISIBLE);

            btnCalc.setVisibility(View.VISIBLE);

            btnCalc.setOnClickListener(view1 -> {

                customKeyboard.hideKeyboard();
                switch (s) {
                    case "Proper Length":
                        double contracted_length = evalf(et1.getText().toString());
                        double velocity = evalf(et2.getText().toString());

                        double beta = velocity / c;
                        double gamma = 1 / (Math.sqrt(1 - beta * beta));

                        double proper_length = gamma * contracted_length;

                        tvRes.setText(Html.fromHtml(HtmlNumberFormatter(view.getContext(), "The proper length of the moving object is " + proper_length + " m")));

                        break;

                    case "Contracted Length":
                        double proper_length1 = evalf(et1.getText().toString());
                        double velocity1 = evalf(et2.getText().toString());

                        double beta1 = velocity1 / c;

                        double gamma1 = 1 / (Math.sqrt(1 - beta1 * beta1));
                        System.out.println(gamma1);
                        double contracted_length1 = proper_length1 / gamma1;

                        tvRes.setText(Html.fromHtml(HtmlNumberFormatter(view.getContext(), "The contracted length of the moving object is " + contracted_length1 + " m")));

                        break;

                    case "Velocity":
                        double proper_length2 = evalf(et1.getText().toString());
                        double contracted_length2 = evalf(et2.getText().toString());

                        double velocity2 = c * Math.sqrt(1 - ((contracted_length2 / proper_length2) * (contracted_length2 / proper_length2)));

                        tvRes.setText(Html.fromHtml(HtmlNumberFormatter(view.getContext(), "The velocity of the moving object is " + velocity2 + " m/s")));

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
            chkProperLength.setChecked(false);
            chkContractedLength.setChecked(false);
            chkVelocity.setChecked(false);

            chkProperLength.setClickable(true);
            chkContractedLength.setClickable(true);
            chkVelocity.setClickable(true);

            btnNext.setClickable(false);
            btnNext.setEnabled(false);
            btnNext.setBackgroundResource(R.drawable.btn_disabled);
            btnClear.setVisibility(View.GONE);

            a[0] = 0;
        });
        return view;
    }

}
