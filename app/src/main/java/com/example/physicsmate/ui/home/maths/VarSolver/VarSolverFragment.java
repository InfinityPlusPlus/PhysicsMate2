package com.example.physicsmate.ui.home.maths.VarSolver;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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

public class VarSolverFragment extends Fragment {

    Button btnCalc, btnAddRow, btnCopy;
    EditText et1, etVars;
    TextView tvRes;
    LinearLayout rowsLinearLayout, MVsLinearLayout;
    ScrollView sv;
    MTMathView mv1;
    CustomKeyboard customKeyboard;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_var_solver2, container, false);
        LinearLayout linear_layout = view.findViewById(R.id.linearLayout);

        rowsLinearLayout = view.findViewById(R.id.var_solver_ets);
        tvRes = view.findViewById(R.id.var_solver_solution);
        sv = view.findViewById(R.id.var_solver_scroll_view);

        et1 = view.findViewById(R.id.var_solver_et1);
        et1.addTextChangedListener(new CustomTextWatcher());
        etVars = view.findViewById(R.id.var_solver_variables);

        btnAddRow = view.findViewById(R.id.var_solver_add_button);
        btnAddRow.setOnClickListener(v -> addRow());

        btnCopy = view.findViewById(R.id.btnCopy);
        btnCopy.setVisibility(View.GONE);

        btnCalc = view.findViewById(R.id.var_solver_solve_button);

        View[] viewsToDisappear = {btnCopy, tvRes};
        Collection<EditText> editTextList = new ArrayList<>();
        editTextList.add(et1);
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

        btnCalc.setOnClickListener(v ->
        {
            customKeyboard.hideKeyboard();
            solveEquations();
        });

        mv1 = view.findViewById(R.id.var_solver_mv1);
        MVsLinearLayout = view.findViewById(R.id.var_solver_MVs);

        tvRes.setVisibility(View.GONE);
        mv1.setVisibility(View.GONE);

        return view;
    }

    private void solveEquations() {
        int numOfEqns = rowsLinearLayout.getChildCount();
        Collection<String> eqns = new ArrayList<>();
        eqns.add(et1.getText().toString());
        for (int i = 1; i < numOfEqns; i++) {
            LinearLayout eqnRowItem = (LinearLayout) rowsLinearLayout.getChildAt(i);
            EditText et = (EditText) eqnRowItem.getChildAt(0);
            eqns.add(et.getText().toString());
        }

        String vars = etVars.getText().toString();
        AtomicReference<String> resultParsed = new AtomicReference<>("");

        Thread thread = new Thread(() -> {
            String result = solveEquationNumeric(eqns, vars);
            resultParsed.set(parseStringAfterSolvingEqn(getContext(), result));
        });
        thread.start();

        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                thread.join();
                tvRes.setText(Html.fromHtml(resultParsed.get(), Html.FROM_HTML_MODE_LEGACY));
                tvRes.setVisibility(View.VISIBLE);
                sv.post(() -> sv.smoothScrollTo(0, tvRes.getBottom()));
                btnCopy.setVisibility(View.VISIBLE);
            } catch (InterruptedException ignored) {
            }
        });

        btnCopy.setOnClickListener(view1 -> {
            ClipboardManager clipboard = getSystemService(view.getContext(), ClipboardManager.class);
            ClipData clip = ClipData.newPlainText("Copied Text", extractNumberFromString(tvRes.getText().toString()));
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Toast.makeText(view.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });
    }

    public void addRow() {
        @SuppressLint("InflateParams")
        LinearLayout rowItem = (LinearLayout) getLayoutInflater().inflate(R.layout.var_solver_rows, null, false);
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        rowItem.startAnimation(fadeIn);

        rowsLinearLayout.addView(rowItem);
        EditText et = (EditText) rowItem.getChildAt(0);
        et.addTextChangedListener(new CustomTextWatcher());
        setupEditTextForCustomKeyboard(customKeyboard, sv, et);

        @SuppressLint("InflateParams")
        MTMathView mvItem = (MTMathView) getLayoutInflater().inflate(R.layout.var_solver_mv2, null, false);
        MVsLinearLayout.addView(mvItem);

        int lastIndex = rowsLinearLayout.getChildCount() - 1;
        ImageButton deleteBtn = (ImageButton) rowItem.getChildAt(1);
        deleteBtn.setOnClickListener(v -> removeRowWithAnimation(lastIndex));

        solveButtonClickable();
    }

    public void removeRowWithAnimation(int index) {
        try {
            View rowView = rowsLinearLayout.getChildAt(index);
            View mvView = MVsLinearLayout.getChildAt(index);

            Animation fadeOut = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
            rowView.startAnimation(fadeOut);
            mvView.startAnimation(fadeOut);

            new Handler().postDelayed(() -> {
                rowsLinearLayout.removeViewAt(index);
                MVsLinearLayout.removeViewAt(index);
                solveButtonClickable();
            }, fadeOut.getDuration());
        } catch (RuntimeException e) {
            Toast.makeText(getContext(), "Error removing row", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    public void solveButtonClickable() {
        int numOfEqns = rowsLinearLayout.getChildCount();

        if (et1.getText().toString().isEmpty()) {
            setSolveButtonState(false, "Enter all the fields");
            mv1.setVisibility(View.GONE);
            return;
        }

        for (int i = 1; i < numOfEqns; i++) {
            LinearLayout eqnRowItem = (LinearLayout) rowsLinearLayout.getChildAt(i);
            EditText et = (EditText) eqnRowItem.getChildAt(0);
            if (et.getText().toString().isEmpty()) {
                setSolveButtonState(false, "Enter all the fields");
                mv1.setVisibility(View.GONE);
                return;
            }
        }

        setSolveButtonState(true, "Solve");
        mv1.setLatex(getTex(et1.getText().toString(), mv1, false));
        mv1.setVisibility(View.VISIBLE);

        for (int i = 1; i < numOfEqns; i++) {
            LinearLayout eqnRowItem = (LinearLayout) rowsLinearLayout.getChildAt(i);
            EditText et = (EditText) eqnRowItem.getChildAt(0);
            MTMathView mv = (MTMathView) MVsLinearLayout.getChildAt(i);
            mv.setLatex(getTex(et.getText().toString(), mv, false));
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

    public class CustomTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            solveButtonClickable();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            solveButtonClickable();
        }

        @Override
        public void afterTextChanged(Editable s) {
            solveButtonClickable();
        }
    }
}
