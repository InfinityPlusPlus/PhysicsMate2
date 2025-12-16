package com.example.physicsmate.ui.home.maths.EigenD;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.agog.mathdisplay.MTMathView;
import com.airbnb.paris.Paris;
import com.example.physicsmate.R;
import com.example.physicsmate.ui.CustomKeyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.physicsmate.misc.Custom_methods.*;
import static com.example.physicsmate.misc.MainActivity.getKeyboard;
import static com.example.physicsmate.misc.MainActivity.setLinkedScrollView;

public class EigenDFragment extends Fragment {

    View view;
    GridLayout matrixGrid;
    LinearLayout ll;
    ScrollView sv;
    CustomKeyboard customKeyboard;
    int dim = 3; //default dimension

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);
        sv = view.findViewById(R.id.scrollView);
        ll = view.findViewById(R.id.linearLayout);

        TextView tv = new TextView(getContext());
        Paris.styleBuilder(tv).add(R.style.custom_textView).apply();
        tv.setText("Enter the dimension of the square matrix");
        ll.addView(tv);

        EditText etDim = new EditText(getContext());
        etDim.setInputType(InputType.TYPE_CLASS_NUMBER);
        etDim.setHint("3");
        Paris.styleBuilder(etDim).add(R.style.custom_edittext).apply();
        ll.addView(etDim);

        Button btnNext = new Button(getContext());
        Paris.styleBuilder(btnNext).add(R.style.custom_button_enabled).apply();
        btnNext.setText("Next");
        ll.addView(btnNext);

        matrixGrid = new GridLayout(getContext());
        matrixGrid.setRowCount(3); //default 3x3 matrix
        matrixGrid.setColumnCount(3);
        matrixGrid.setForegroundGravity(Gravity.CENTER);
        HorizontalScrollView svMatrix = new HorizontalScrollView(getContext());
        svMatrix.addView(matrixGrid);
        ll.addView(svMatrix);
        matrixGrid.setVisibility(View.GONE);

        Button btnCalc = new Button(getContext());
        Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();
        btnCalc.setText("Calculate");
        ll.addView(btnCalc);
        btnCalc.setVisibility(View.GONE);

        List<EditText> editTextList = new ArrayList<>();

        TextView tvRes = new TextView(getContext());
        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();
        ll.addView(tvRes);
        tvRes.setVisibility(View.GONE);

        View[] viewsToDisappear = {tvRes};

        MTMathView mtMathView = new MTMathView(getContext());
        ll.addView(mtMathView);
        mtMathView.setFontSize(60f);
        mtMathView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 30, 20, 40);
        mtMathView.setLayoutParams(params);
        mtMathView.setVisibility(View.GONE);

        view.post(() -> {
            customKeyboard = getKeyboard();
            if (customKeyboard != null) {
                customKeyboard.hideKeyboard();
                setupEditTextForCustomKeyboard(customKeyboard, sv, editTextList);
                setupEditTextChangeListener(viewsToDisappear, btnNext, customKeyboard, etDim);
                setupEditTextChangeListener(viewsToDisappear, btnCalc, customKeyboard, editTextList);
            }
        });

        setLinkedScrollView(sv);

        btnNext.setOnClickListener(view ->
        {
            String dimStr = etDim.getText().toString();
            dim = Integer.parseInt(dimStr);
            if (dim <= 0 || dim > 11) {
                Toast.makeText(getContext(), "Dimension must be between 1 and 11", Toast.LENGTH_SHORT).show();
                return;
            }

            matrixGrid.removeAllViews();
            matrixGrid.setRowCount(dim);
            matrixGrid.setColumnCount(dim);

            editTextList.clear();
            for (int i = 0; i < dim * dim; i++) {
                EditText et = new EditText(getContext());
                et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                et.setHint("0");
                Paris.styleBuilder(et).add(R.style.custom_edittext_mini).apply();


                // Add a text watcher
                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {

                        dynamicEtLength(s, et);
                    }
                });

                matrixGrid.addView(et);
                editTextList.add(et);
            }

            setupEditTextForCustomKeyboard(customKeyboard, sv, editTextList);
            setupEditTextChangeListener(viewsToDisappear, btnNext, customKeyboard, etDim);
            setupEditTextChangeListener(viewsToDisappear, btnCalc, customKeyboard, editTextList);

            matrixGrid.setVisibility(View.VISIBLE);
            btnCalc.setVisibility(View.VISIBLE);

        });

        btnCalc.setOnClickListener(v -> {
            customKeyboard.hideKeyboard();

            // Collect matrix entries
            double[][] matrix = new double[dim][dim];
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    String entryStr = editTextList.get(i * dim + j).getText().toString();
                    matrix[i][j] = entryStr.isEmpty() ? 0 : evalf(entryStr);
                }
            }

            String[] eigenvalues = eigenVals(matrix);
            String[][] eigenVecs = eigenVecs(matrix);

            tvRes.setText(Html.fromHtml(HtmlNumberFormatter(requireContext(), "Eigenvalues:<br>" + Arrays.toString(eigenvalues) + "<br><br>Eigenvectors:<br>" + Arrays.deepToString(eigenVecs)), Html.FROM_HTML_MODE_LEGACY));
            tvRes.setVisibility(View.VISIBLE);

            String strEigenVecsLatex = Arrays.deepToString(eigenVecs);
            strEigenVecsLatex = strEigenVecsLatex.replace("[[", "\\begin{bmatrix} ").replace("]]", " \\end{bmatrix}").replace("], [", " \\\\\n ").replace("[", "").replace("]", "").replace(", ", " & ");
            mtMathView.setLatex(strEigenVecsLatex + "^ T");
            /*mtMathView.setLatex("\\begin{bmatrix}\n" +
                    "    x_{11} & x_{12} & x_{13} & \\ldots  & x_{1n} \\\\\n" +
                    "    x_{21} & x_{22} & x_{23} & \\ldots  & x_{2n} \\\\\n" +
                    "    \\vdots & \\vdots & \\vdots & \\ddots & \\vdots \\\\\n" +
                    "    x_{d1} & x_{d2} & x_{d3} & \\ldots  & x_{dn}\n" +
                    "\\end{bmatrix}");*/

            System.out.println(getTex(Arrays.deepToString(eigenVecs), mtMathView));
            mtMathView.setVisibility(View.VISIBLE);
            sv.post(() -> sv.smoothScrollTo(0, tvRes.getBottom()));
        });

        return view;

    }
}
