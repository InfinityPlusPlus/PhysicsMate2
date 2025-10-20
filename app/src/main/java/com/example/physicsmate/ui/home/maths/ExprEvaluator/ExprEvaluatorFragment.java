package com.example.physicsmate.ui.home.maths.ExprEvaluator;

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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.physicsmate.misc.Custom_methods.*;
import static com.example.physicsmate.misc.MainActivity.getKeyboard;
import static com.example.physicsmate.misc.MainActivity.setLinkedScrollView;

public class ExprEvaluatorFragment extends Fragment {

    ScrollView sv;
    CustomKeyboard customKeyboard;
    Button btnCalc, btnCopy;
    TextView tvVars, tvFn;
    EditText etVars, etFn;
    LinearLayout linear_layout;
    TextView tvRes;

    List<String> LVars;
    List<Double> LVals;
    String varsAndVals, vars, vals;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);
        sv = view.findViewById(R.id.scrollView);

        tvVars = new TextView(view.getContext());
        tvFn = new TextView(view.getContext());
        etVars = new EditText(view.getContext());
        etFn = new EditText(view.getContext());

        etVars.setHint("x = pi, y = e");
        etFn.setHint("sin(x)*y");

        Paris.styleBuilder(tvVars).add(R.style.custom_textView).apply();
        Paris.styleBuilder(tvFn).add(R.style.custom_textView).apply();
        Paris.styleBuilder(etVars).add(R.style.custom_edittext).apply();
        Paris.styleBuilder(etFn).add(R.style.custom_edittext).apply();

        tvVars.setText("Enter the variables");
        tvFn.setText("Enter the function f(x, y)");

        varsAndVals = etVars.getText().toString();
        getVars(); //sets up vars
        etVarsTCL(); //uses vars

        linear_layout = view.findViewById(R.id.linearLayout);

        linear_layout.addView(tvVars, 0);
        linear_layout.addView(etVars, 1);
        linear_layout.addView(tvFn, 2);
        linear_layout.addView(etFn, 3);

        btnCopy = new Button(view.getContext());
        btnCalc = new Button(view.getContext());

        tvRes = new TextView(view.getContext());
        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();

        Paris.styleBuilder(btnCopy).add(R.style.custom_button_enabled).apply();
        btnCopy.setText("Copy");

        btnCopy.setVisibility(View.GONE);
        btnCalc.setVisibility(View.VISIBLE);
        tvRes.setVisibility(View.GONE);
        btnCopy.setVisibility(View.GONE);

        Paris.styleBuilder(btnCalc).add(R.style.custom_button_disabled).apply();

        btnCalc.setText("Please enter all the fields");

        linear_layout.addView(btnCalc, 4);

        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();

        linear_layout.addView(tvRes, 5);
        linear_layout.addView(btnCopy, 6);

        tvRes.setVisibility(View.GONE);

        View[] viewsToDisappear = {btnCopy, tvRes};
        Collection<EditText> editTextList = new ArrayList<>();
        editTextList.add(etFn);
        editTextList.add(etVars);

        linear_layout.post(() -> {
            customKeyboard = getKeyboard();
            if (customKeyboard != null) {
                customKeyboard.hideKeyboard();
                setupEditTextForCustomKeyboard(customKeyboard, sv, editTextList);
                setupEditTextChangeListener(viewsToDisappear, btnCalc, customKeyboard, editTextList);
            }
        });

        setLinkedScrollView(sv);

        btnCalc.setOnClickListener(v -> {
            customKeyboard.hideKeyboard();
        });

        tvRes.setVisibility(View.GONE);

        return view;
    }

    private void etVarsTCL() {
        etVars.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getVars();
                System.out.println("\n\nTCL: " + vars + "\n\n");
                tvFn.setText("Enter the function f(" + vars + ")");
            }
        });
    }

    private void getVars()
    {
        varsAndVals = etVars.getText().toString().trim();

        System.out.println("\n\ngetVars varsAndVals: " + varsAndVals + "\n\n");
        //using regex, get the left hand side of the = sign
        //"x = pi, y = e" -> "x, y"
        vars = varsAndVals.replaceAll(",", " , ");
        vars = vars.replaceAll("=([a-zA-Z0-9_]*)", "");
        vars = vars.trim();
        vars = vars.replaceAll(" , ", ",");

        vals = varsAndVals.replaceAll(",", " , ");
        vals = vals.replaceAll("([a-zA-Z0-9_]*)=", "");
        vals = vals.trim();
        vals = vals.replaceAll(" , ", ",");

        System.out.println("\n\ngetVars vars: " + vars + "\n\n");
        System.out.println("\n\ngetVars vals: " + vals + "\n\n");

        LVars = new ArrayList<>();
        LVals = new ArrayList<>();

        String[] varsArray = vars.split(",");
        Collections.addAll(LVars, varsArray);

        String[] valsArray = vals.split(",");
        for (String val : valsArray) {
            LVals.add(evalf(val, false));
        }
    }

    private void setSolveButtonState(boolean enabled, CharSequence message) {
        btnCalc.setEnabled(enabled);
        btnCalc.setClickable(enabled);
        btnCalc.setText(message);
        if (enabled) {
            Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();
        } else {
            btnCalc.setBackgroundResource(R.drawable.btn_disabled);
            btnCalc.setTextColor(getResources().getColor(R.color.app_bg));
        }
    }
}
