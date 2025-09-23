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
import java.util.concurrent.atomic.AtomicReference;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.physicsmate.misc.Custom_methods.*;
import static com.example.physicsmate.misc.MainActivity.getKeyboard;
import static com.example.physicsmate.misc.MainActivity.setLinkedScrollView;

public class ExprEvaluatorFragment extends Fragment {

    ScrollView sv;
    CustomKeyboard customKeyboard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);

        TextView tvVars, tvFn;
        EditText etVars, etFn;
        sv = view.findViewById(R.id.scrollView);

        tvVars = new TextView(view.getContext());
        tvFn = new TextView(view.getContext());
        etVars = new EditText(view.getContext());
        etFn = new EditText(view.getContext());

        etVars.setText("x");

        Paris.styleBuilder(tvVars).add(R.style.custom_textView).apply();
        Paris.styleBuilder(tvFn).add(R.style.custom_textView).apply();
        Paris.styleBuilder(etVars).add(R.style.custom_edittext).apply();
        Paris.styleBuilder(etFn).add(R.style.custom_edittext).apply();

        tvVars.setText("Enter the list of variables");
        tvFn.setText("Enter the function f(" + etVars.getText() + ")");

        LinearLayout linear_layout = view.findViewById(R.id.linearLayout);

        linear_layout.addView(tvVars, 0);
        linear_layout.addView(etVars, 1);
        linear_layout.addView(tvFn, 2);
        linear_layout.addView(etFn, 3);

        Button btnCopy = new Button(view.getContext());
        Button btnCalc = new Button(view.getContext());

        TextView tvRes = new TextView(view.getContext());
        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();

        Paris.styleBuilder(btnCopy).add(R.style.custom_button_enabled).apply();
        btnCopy.setText("Copy");

        btnCopy.setVisibility(View.GONE);
        btnCalc.setVisibility(View.GONE);
        tvRes.setVisibility(View.GONE);
        btnCopy.setVisibility(View.GONE);

        Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();

        btnCalc.setText("Calculate");

        linear_layout.addView(btnCalc, 12);

        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();

        linear_layout.addView(tvRes, 13);
        linear_layout.addView(btnCopy, 14);

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
            solveEquations();
        });

        tvRes.setVisibility(View.GONE);

        return view;
    }

    @SuppressLint("SetTextI18n")
    public void solveButtonClickable() {
        int numOfEqns = rowsLinearLayout.getChildCount();

        for (int i = 0; i < numOfEqns; i++) {
            LinearLayout eqnRowItem = (LinearLayout) rowsLinearLayout.getChildAt(i);
            EditText et = (EditText) eqnRowItem.getChildAt(0);

            if (et.getText().toString().isEmpty()) {
                setSolveButtonState(false, "Enter all the fields");
                return;
            }
        }

        setSolveButtonState(true, "Solve");

        // Update LaTeX for all equations
        for (int i = 0; i < numOfEqns; i++) {
            LinearLayout eqnRowItem = (LinearLayout) rowsLinearLayout.getChildAt(i);
            EditText et = (EditText) eqnRowItem.getChildAt(0);
            MTMathView mv = (MTMathView) MVsLinearLayout.getChildAt(i);
            mv.setLatex(getTex(et.getText().toString(), mv, false));
            mv.setVisibility(View.VISIBLE);
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
