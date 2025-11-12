package com.example.physicsmate.ui.home.maths.EigenD;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.physicsmate.R;
import com.example.physicsmate.ui.CustomKeyboard;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

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
        tv.setText("Enter the dimension of the square matrix");
        ll.addView(tv);
        EditText etDim = new EditText(getContext());
        etDim.setInputType(InputType.TYPE_CLASS_NUMBER);
        etDim.setHint("3");
        ll.addView(etDim);

        Button btnNext = new Button(getContext());
        btnNext.setText("Next");
        ll.addView(btnNext);

        matrixGrid = new GridLayout(getContext());
        matrixGrid.setRowCount(3); //default 3x3 matrix
        matrixGrid.setColumnCount(3);
        ll.addView(matrixGrid);
        matrixGrid.setVisibility(View.GONE);

        Button btnCalc = new Button(getContext());
        btnCalc.setText("Calculate Eigenvalues and Eigenvectors");
        ll.addView(btnCalc);
        btnCalc.setVisibility(View.GONE);

        List<EditText> editTextList = Collections.emptyList();

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
                matrixGrid.addView(et);
                editTextList.add(et);
            }

            matrixGrid.setVisibility(View.VISIBLE);
            btnCalc.setVisibility(View.VISIBLE);

        });

        TextView tvRes = new TextView(getContext());
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

            tvRes.setText("replace with eigenvalue and eigenvector calculation result");
            tvRes.setVisibility(View.VISIBLE);
            sv.post(() -> sv.smoothScrollTo(0, tvRes.getBottom()));
        });



        View[] viewsToDisappear = {matrixGrid, btnCalc, tvRes};

        view.post(() -> {
            customKeyboard = getKeyboard();
            if (customKeyboard != null) {
                customKeyboard.hideKeyboard();
                setupEditTextForCustomKeyboard(customKeyboard, sv, editTextList);
                setupEditTextChangeListener(viewsToDisappear, btnCalc, customKeyboard, editTextList);
            }
        });

        setLinkedScrollView(sv);

        return view;

    }
}
