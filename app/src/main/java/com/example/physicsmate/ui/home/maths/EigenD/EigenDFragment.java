package com.example.physicsmate.ui.home.maths.EigenD;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
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

            matrixGrid.setVisibility(View.VISIBLE);
            btnCalc.setVisibility(View.VISIBLE);

        });

        TextView tvRes = new TextView(getContext());
        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();
        ll.addView(tvRes);
        tvRes.setVisibility(View.GONE);

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

            double[] eigenvalues = eigenVals(matrix);
            double[][] normalizedEigenVecs = normalizedEigenVecs(matrix);

            tvRes.setText(Html.fromHtml(HtmlNumberFormatter(requireContext(), "Eigenvalues:<br>" + Arrays.toString(eigenvalues) + "<br><br>Normalized Eigenvectors:<br>" + Arrays.deepToString(normalizedEigenVecs)), Html.FROM_HTML_MODE_LEGACY));
            tvRes.setVisibility(View.VISIBLE);
            sv.post(() -> sv.smoothScrollTo(0, tvRes.getBottom()));
        });



        View[] viewsToDisappear = {matrixGrid, btnCalc, tvRes};

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

        return view;

    }
}
