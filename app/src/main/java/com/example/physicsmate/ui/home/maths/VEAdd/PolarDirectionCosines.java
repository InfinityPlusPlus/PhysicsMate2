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
import androidx.fragment.app.Fragment;
import com.airbnb.paris.Paris;
import com.example.physicsmate.R;
import com.example.physicsmate.ui.CustomKeyboard;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.physicsmate.misc.Custom_methods.*;
import static com.example.physicsmate.misc.Custom_methods.HtmlNumberFormatter;
import static com.example.physicsmate.misc.MainActivity.getKeyboard;
import static com.example.physicsmate.misc.MainActivity.setLinkedScrollView;
import static com.example.physicsmate.ui.home.maths.VEAdd.VEAddFragment.addBrackets;

public class PolarDirectionCosines extends Fragment
{

    View viewPolarDC;
    CustomKeyboard customKeyboard;
    ScrollView sv;

    public void copyToClipboard(CharSequence string) {
        customKeyboard.hideKeyboard();
        ClipboardManager clipboard = getSystemService(getContext(), ClipboardManager.class);
        ClipData clip = ClipData.newPlainText("copied text", string);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "derivative copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        viewPolarDC = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);
        sv = viewPolarDC.findViewById(R.id.scrollView);
        LinearLayout linearLayout = viewPolarDC.findViewById(R.id.linearLayout);

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

        viewPolarDC.post(() -> {
            customKeyboard = getKeyboard();
            if (customKeyboard != null) {
                customKeyboard.hideKeyboard();
                setupEditTextForCustomKeyboard(customKeyboard, sv, editTextList);
                setupEditTextChangeListener(viewsToDisappear, btnCalc, customKeyboard, Arrays.asList(editTextList));
            }
        });

        setLinkedScrollView(sv);

        //add a textview at the top (child 0), asking the user to enter the vectors
        TextView tv = new TextView(requireContext());
        Paris.styleBuilder(tv).add(R.style.custom_textView).apply();
        tv.setText("Enter the vectors in polar coordinates");
        linearLayout.addView(tv, 0);

        TextView tvHelp = new TextView(requireContext());
        Paris.styleBuilder(tvHelp).add(R.style.custom_textViewSmallNotice).apply();
        tvHelp.setText("Use the format (r, cos(theta1), (theta2), ..., cos(theta(n-1))) [for n-dimensional vectors], where theta is in radians. \n\n theta1, theta2, ... theta(n) are measured from the vector to the x1, x2, ... x(n-1) axes. \n\n (The last cosine is automatically calculated)");
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

                //calculate the last cosine using formula cos(theta(n)) = sqrt(1 - cos^2(theta1) - cos^2(theta2) - ... - cos^2(theta(n-1)))
                //eg, the vector (4,0.6) is represented as (4,0.6,0.8) in polar coordinates for 2D
                //note that editTexts.length is the number of vectors to add, not the dimension of the vector

                String s = addBrackets(editTextList[i].getText().toString());

                //remove the brackets before splitting
                s = s.substring(1, s.length() - 1);
                String[] parts = s.split(",");
                System.out.println(Arrays.toString(parts));
                System.out.println(parts[1]);
                double sumSqrCosines = 0;
                for (int j = 1; j < parts.length; j++) {
                    sumSqrCosines += Math.pow(evalf(parts[j]), 2);
                }
                double lastCosine = Math.sqrt(1 - sumSqrCosines);


                //remove the last bracket before appending the last cosine
                editTextList[i].setText(editTextList[i].getText().toString().substring(0, editTextList[i].getText().toString().length() - 1));
                editTextList[i].setText(editTextList[i].getText().toString() + "," + lastCosine + ")");

                vectors[i] = editTextList[i].getText().toString();

            }
            //calculate the result
            String result = addPolarVectorsDirnCosines(vectors);
            tvRes.setText(Html.fromHtml(HtmlNumberFormatter(requireContext(), result), Html.FROM_HTML_MODE_LEGACY));

            tvRes.setVisibility(View.VISIBLE);
            btnCopy.setVisibility(View.VISIBLE);

            sv.post(() -> sv.smoothScrollTo(0, btnCopy.getBottom()));

        });

        btnCopy.setOnClickListener(v -> {
            customKeyboard.hideKeyboard();
            copyToClipboard(tvRes.getText().toString());
            Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        return viewPolarDC;
    }

}
