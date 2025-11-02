package com.example.physicsmate.ui.home.maths.VEAdd;

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
import com.airbnb.paris.Paris;
import com.example.physicsmate.R;
import com.example.physicsmate.ui.CustomKeyboard;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.physicsmate.misc.Custom_methods.*;
import static com.example.physicsmate.misc.MainActivity.getKeyboard;
import static com.example.physicsmate.misc.MainActivity.setLinkedScrollView;

public class Cartesian extends VEAddFragment {

    View viewCartesian;
    CustomKeyboard customKeyboard;
    ScrollView sv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewCartesian = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);

        LinearLayout linearLayout = viewCartesian.findViewById(R.id.linearLayout);
        sv = viewCartesian.findViewById(R.id.scrollView);

        TextView tvRes = new TextView(requireContext());
        Button btnCalc = new Button(requireContext());
        Button btnCopy = new Button(requireContext());
        ImageView img = new ImageView(requireContext());

        btnCopy.setVisibility(View.GONE);
        tvRes.setVisibility(View.GONE);
        img.setVisibility(View.GONE);

        btnCopy.setVisibility(View.GONE);
        tvRes.setVisibility(View.GONE);
        //mv.setVisibility(View.GONE);
        img.setVisibility(View.GONE);

        Paris.styleBuilder(tvRes).add(R.style.answer_textView).apply();
        Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();
        btnCalc.setClickable(true);

        Bundle args = getArguments();
        int num = args.getInt("num", 2);

        EditText[] editTextList = new EditText[num];

        //add edittexts based on the number of vectors
        for (int i = 0; i < num; i++) {
            EditText et = new EditText(requireContext());
            Paris.styleBuilder(et).add(R.style.custom_edittext).apply();
            linearLayout.addView(et);
            editTextList[i] = et;
        }

        View[] viewsToDisappear = {btnCopy, tvRes};

        viewCartesian.post(() -> {
            customKeyboard = getKeyboard();
            if (customKeyboard != null) {
                keyboardSetter(customKeyboard);
                setupEditTextForCustomKeyboard(customKeyboard, sv, editTextList);
                setupEditTextChangeListener(viewsToDisappear, btnCalc, customKeyboard, Arrays.asList(editTextList));
            }
        });

        setLinkedScrollView(sv);

        //add a textview at the top (child 0), asking the user to enter the vectors
        TextView tv = new TextView(requireContext());
        Paris.styleBuilder(tv).add(R.style.custom_textView).apply();
        tv.setText("Enter the vectors in cartesian coordinates");
        linearLayout.addView(tv, 0);

        TextView tvHelp = new TextView(requireContext());
        Paris.styleBuilder(tvHelp).add(R.style.custom_textViewSmallNotice).apply();
        tvHelp.setText("Use the format (x,y,z,...)");
        linearLayout.addView(tvHelp, 1);

        AtomicReference<Double> scalarSum = new AtomicReference<>((double) 0);
        final boolean[] isScalar = {false};

        //linearLayout.addView(img, 2);
        linearLayout.addView(btnCalc);
        linearLayout.addView(tvRes);
        linearLayout.addView(btnCopy);


        btnCalc.setOnClickListener(v -> {
            customKeyboard.hideKeyboard();
            //add the vectors
            String[] vectors = new String[editTextList.length];
            for (int i = 0; i < editTextList.length; i++) {

                vectors[i] = addBrackets(editTextList[i].getText().toString());

                //check if the vector is a scalar
                if (isScalar(vectors[i])) {
                    int finalI = i;
                    //if it is a scalar, add it to the scalar sum
                    scalarSum.updateAndGet(v2 -> v2 + evalf(vectors[finalI]));
                    isScalar[0] = true;
                }
            }

            //calculate the result
            if (isScalar[0]) {
                tvRes.setText(Html.fromHtml(HtmlNumberFormatter(requireContext(), scalarSum.get().toString()), Html.FROM_HTML_MODE_LEGACY));
            } else {
                String result = addVectors(vectors);
                tvRes.setText(Html.fromHtml(HtmlNumberFormatter(requireContext(), result), Html.FROM_HTML_MODE_LEGACY));
            }

            tvRes.setVisibility(View.VISIBLE);
            btnCopy.setVisibility(View.VISIBLE);
            sv.post(() -> sv.smoothScrollTo(0, btnCopy.getBottom()));

        });

        btnCopy.setOnClickListener(v -> {
            customKeyboard.hideKeyboard();
            copyToClipboard(tvRes.getText().toString());
            Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        return viewCartesian;
    }



    public void copyToClipboard(CharSequence string) {
        customKeyboard.hideKeyboard();
        ClipboardManager clipboard = getSystemService(getContext(), ClipboardManager.class);
        ClipData clip = ClipData.newPlainText("copied text", string);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "derivative copied to clipboard", Toast.LENGTH_SHORT).show();
    }

}
