package com.example.physicsmate.ui.home.maths.Orthonormalisation;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
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
import com.example.physicsmate.misc.Custom_methods;
import com.example.physicsmate.ui.CustomKeyboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.physicsmate.misc.Custom_methods.*;
import static com.example.physicsmate.misc.MainActivity.getKeyboard;
import static com.example.physicsmate.misc.MainActivity.setLinkedScrollView;

public class OrthonormalisationFragment extends Fragment {

    View view;

    Button btnNext, btnCopy, btnCalc;
    EditText etDimension;
    LinearLayout vectorsContainer, orthonormalisation_LL;
    ScrollView sv;
    CustomKeyboard customKeyboard;
    TextView tvRes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_orthonormalisation, container, false);

        // Initialize UI elements
        btnNext = view.findViewById(R.id.orthonormalisation_btn);
        btnCopy = view.findViewById(R.id.btnCopy);
        btnCalc = view.findViewById(R.id.btnCalc);
        tvRes = view.findViewById(R.id.tvRes);
        etDimension = view.findViewById(R.id.orthonormalisation_dim);
        vectorsContainer = view.findViewById(R.id.vectorsContainer);
        orthonormalisation_LL = view.findViewById(R.id.orthonormalisation_LL);
        sv = view.findViewById(R.id.orthonormalisation_scrollview);

        btnCopy.setVisibility(View.GONE);
        tvRes.setVisibility(View.GONE);
        btnCalc.setVisibility(View.GONE);

        View[] viewsToDisappear = {btnCopy, tvRes, vectorsContainer};

        orthonormalisation_LL.post(() -> {
            customKeyboard = getKeyboard();
            if (customKeyboard != null) {
                customKeyboard.hideKeyboard();
                setupEditTextForCustomKeyboard(customKeyboard, sv, etDimension);
                setupEditTextChangeListener(viewsToDisappear, btnNext, customKeyboard, etDimension);
            }
        });


        setLinkedScrollView(sv);

        // Set up button click listener
        btnNext.setOnClickListener(v -> {
            customKeyboard.hideKeyboard();
            String dimStr = etDimension.getText().toString();
            if (dimStr.isEmpty() || Integer.parseInt(dimStr) <= 0) {
                Toast.makeText(getContext(), "Enter a valid dimension", Toast.LENGTH_SHORT).show();
                return;
            }

            int dimension = Integer.parseInt(dimStr);
            createVectorInputFields(dimension);
            btnCalc.setVisibility(View.VISIBLE);
        });

        return view;
    }

    private void createVectorInputFields(int dimension) {
        vectorsContainer.removeAllViews();
        vectorsContainer.setVisibility(View.VISIBLE);
        Collection<EditText> editTextList = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            HorizontalScrollView scrollView = new HorizontalScrollView(requireContext());
            scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            LinearLayout vectorRow = new LinearLayout(requireContext());
            vectorRow.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < dimension; j++) {
                EditText componentInput = new EditText(requireContext());
                componentInput.setHint("v" + (i + 1) + "_" + (j + 1));
                Paris.styleBuilder(componentInput).add(R.style.custom_edittext_mini).apply();
                componentInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                vectorRow.addView(componentInput);
                editTextList.add(componentInput);
            }

            scrollView.addView(vectorRow);
            vectorsContainer.addView(scrollView);
        }

        btnCalc.setText("Orthonormalise");
        Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();
        View[] viewsToDisappear = {btnCopy, tvRes};

        orthonormalisation_LL.post(() -> {
            customKeyboard = getKeyboard();
            if (customKeyboard != null) {
                customKeyboard.hideKeyboard();
                setupEditTextForCustomKeyboard(customKeyboard, sv, editTextList);
                setupEditTextChangeListener(viewsToDisappear, btnCalc, customKeyboard, editTextList);
            }
        });


        setLinkedScrollView(sv);

        btnCalc.setOnClickListener(v -> performOrthonormalisation(dimension));
    }

    private void performOrthonormalisation(int dimension) {

        customKeyboard.hideKeyboard();
        List<List<Double>> vectors = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            HorizontalScrollView scrollView = (HorizontalScrollView) vectorsContainer.getChildAt(i);
            LinearLayout vectorRow = (LinearLayout) scrollView.getChildAt(0);
            List<Double> vector = new ArrayList<>();

            for (int j = 0; j < dimension; j++) {
                EditText componentInput = (EditText) vectorRow.getChildAt(j);
                String componentText = componentInput.getText().toString();
                if (componentText.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all components", Toast.LENGTH_SHORT).show();
                    return;
                }
                vector.add(Double.parseDouble(componentText));
            }
            vectors.add(vector);
        }

        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();

        tvRes.setText(Html.fromHtml(parseStringForOrthonormalisation(orthogonalizeWithSymja(vectors)), Html.FROM_HTML_MODE_LEGACY));
        tvRes.setVisibility(View.VISIBLE);

        btnCopy.setVisibility(View.VISIBLE);
    }

    private String parseStringForOrthonormalisation(String s) {
        //convert a string like {{3/Sqrt(10),1/Sqrt(10)},{-Sqrt(5/2)/5,3/5*Sqrt(5/2)}} into
        // "<font color=#CDDC39> (3/Sqrt(10),1/Sqrt(10) <br> (-Sqrt(5/2)/5,3/5*Sqrt(5/2) </font>"
        s = s.substring(1, s.length() - 1);
        s = s.replace("},{", "<br><br>");
        s = s.replace("{{", "");
        s = s.replace("}}", "");
        s = "<br>The orthonormalised vectors are <br><br>" + HtmlNumberFormatter(requireContext(), s);
        //remove any { or } left
        s = s.replace("{", "");
        s = s.replace("}", "");
        return s;
    }

    private String orthogonalizeWithSymja(List<List<Double>> vectors) {
        // Initialize Symja evaluator

        // Convert the input vectors to a Symja-compatible string format
        StringBuilder symjaInput = new StringBuilder("{");
        for (int i = 0; i < vectors.size(); i++) {
            symjaInput.append("{");
            for (int j = 0; j < vectors.get(i).size(); j++) {
                symjaInput.append(vectors.get(i).get(j));
                if (j < vectors.get(i).size() - 1) {
                    symjaInput.append(", ");
                }
            }
            symjaInput.append("}");
            if (i < vectors.size() - 1) {
                symjaInput.append(", ");
            }
        }
        symjaInput.append("}");

        // Call the Orthogonalize function in Symja
        String orthogonalizedVectors = Custom_methods.Orthogonalize(symjaInput.toString());
        return orthogonalizedVectors;
    }
}
