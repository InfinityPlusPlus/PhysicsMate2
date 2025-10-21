package com.example.physicsmate.ui.home.maths.IntegralCalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import com.agog.mathdisplay.MTMathView;
import com.airbnb.paris.Paris;
import com.example.physicsmate.R;
import com.example.physicsmate.ui.CustomKeyboard;

import java.util.ArrayList;
import java.util.List;

import static com.example.physicsmate.misc.Custom_methods.setupEditTextChangeListener;
import static com.example.physicsmate.misc.Custom_methods.setupEditTextForCustomKeyboard;
import static com.example.physicsmate.misc.MainActivity.getKeyboard;

public class IntegralCalculatorFragment extends Fragment
{

    CustomKeyboard customKeyboard = getKeyboard();
    EditText inputFunction, inputVar, nthDer, minX, maxX, etLowerBound, etUpperBound;
    CheckBox cbDefiniteIntegral, cbNumericEvaluation;
    TextView result, simpResult, derInttvMax, derInttvMin, DerInttv3, DerInttv2, DerInttv1, tvLowerBound, tvUpperBound, tvDefInt;
    MTMathView latexView1, latexView2, latexView3;

    Button calculateIntegral, btnGraph, btnGraphInfo, btnCopy, btnCopyDefInt;

    //MathView latexView1, latexView2, latexView3;
    ScrollView sv;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);

        sv = view.findViewById(R.id.scrollView);
        LinearLayout linear_layout = view.findViewById(R.id.linearLayout);

        TextView tv1 = new TextView(view.getContext());
        tv1.setText("Enter the function");

        inputFunction = new EditText(view.getContext());
        inputFunction.setHint("f(x)");

        Paris.styleBuilder(tv1).add(R.style.custom_textView).apply();
        Paris.styleBuilder(inputFunction).add(R.style.custom_edittext).apply();

        linear_layout.addView(tv1, 0);
        linear_layout.addView(inputFunction, 1);

        TextView tv2 = new TextView(view.getContext());
        tv2.setText("Enter the integrating variable");

        inputVar = new EditText(view.getContext());
        inputVar.setHint("x");

        Paris.styleBuilder(tv2).add(R.style.custom_textView).apply();
        Paris.styleBuilder(inputVar).add(R.style.custom_edittext).apply();

        linear_layout.addView(tv2, 2);
        linear_layout.addView(inputVar, 3);

        nthDer = new EditText(view.getContext());
        minX = new EditText(view.getContext());
        maxX = new EditText(view.getContext());
        etLowerBound = new EditText(view.getContext());
        etUpperBound = new EditText(view.getContext());

        cbDefiniteIntegral = new CheckBox(view.getContext());
        cbNumericEvaluation = new CheckBox(view.getContext());

        result = new TextView(view.getContext());
        simpResult = new TextView(view.getContext());
        derInttvMax = new TextView(view.getContext());
        derInttvMin = new TextView(view.getContext());
        DerInttv3 = new TextView(view.getContext());
        DerInttv2 = new TextView(view.getContext());
        DerInttv1 = new TextView(view.getContext());
        tvLowerBound = new TextView(view.getContext());
        tvUpperBound = new TextView(view.getContext());
        tvDefInt = new TextView(view.getContext());

        latexView1 = new MTMathView(view.getContext());
        latexView2 = new MTMathView(view.getContext());
        latexView3 = new MTMathView(view.getContext());

        calculateIntegral = new Button(view.getContext());
        btnGraph = new Button(view.getContext());
        btnGraphInfo = new Button(view.getContext());
        btnCopy = new Button(view.getContext());
        btnCopyDefInt = new Button(view.getContext());

        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(inputFunction);
        editTextList.add(inputVar);
        editTextList.add(nthDer);
        editTextList.add(minX);
        editTextList.add(maxX);
        editTextList.add(etLowerBound);
        editTextList.add(etUpperBound);

        View[] viewsToDisappear = {btnCopy, btnCopyDefInt};

        view.post(() -> {
            customKeyboard = getKeyboard();
            if (customKeyboard != null) {
                customKeyboard.hideKeyboard();
                setupEditTextForCustomKeyboard(customKeyboard, sv, editTextList);
                setupEditTextChangeListener(viewsToDisappear, calculateIntegral, customKeyboard, editTextList);
            }
        });

        return view;
    }
}
