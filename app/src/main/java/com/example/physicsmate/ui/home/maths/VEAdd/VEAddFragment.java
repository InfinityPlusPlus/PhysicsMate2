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
import static com.example.physicsmate.misc.MainActivity.getKeyboard;
import static com.example.physicsmate.misc.MainActivity.setLinkedScrollView;

public class VEAddFragment extends Fragment {

    CustomKeyboard customKeyboard = getKeyboard();
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sv_ll_fragment_blank, container, false);

        LinearLayout ll = view.findViewById(R.id.linearLayout);

        TextView tv1 = new TextView(getContext());
        tv1.setText("Enter the number of vectors to add");

        EditText et1 = new EditText(getContext());
        et1.setHint("3");

        Button btn1 = new Button(getContext());
        btn1.setText("Cartesian");

        Button btn2 = new Button(getContext());
        btn2.setText("Polar");

        Button btn3 = new Button(getContext());
        btn3.setText("Polar (Direction cosines)");

        Paris.styleBuilder(tv1).add(R.style.custom_textView).apply();
        Paris.styleBuilder(et1).add(R.style.custom_edittext).apply();
        Paris.styleBuilder(btn1).add(R.style.custom_button_enabled).apply();
        Paris.styleBuilder(btn2).add(R.style.custom_button_enabled).apply();
        Paris.styleBuilder(btn3).add(R.style.custom_button_enabled).apply();

        ll.addView(tv1, 0);
        ll.addView(et1, 1);
        ll.addView(btn1, 2);
        ll.addView(btn2, 3);
        ll.addView(btn3, 4);

        setupEditTextChangeListener(null, btn1, customKeyboard, et1);
        setupEditTextChangeListener(null, btn2, customKeyboard, et1);
        setupEditTextChangeListener(null, btn3, customKeyboard, et1);

        btn1.setOnClickListener(v -> {
            Cartesian cart = new Cartesian();

            Bundle args = new Bundle();
            args.putString("num", String.valueOf(evalf(et1.getText().toString().trim())));
            cart.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.cl_content_main, cart) // Use your host container ID
                    .addToBackStack(null)
                    .commit();
        });

        btn2.setOnClickListener(v -> {
            Polar polar = new Polar();

            Bundle args = new Bundle();
            args.putInt("num", Integer.parseInt(String.valueOf(evalf(et1.getText().toString().trim()))));
            polar.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.cl_content_main, polar) // Use your host container ID
                    .addToBackStack(null)
                    .commit();

        });

        btn3.setOnClickListener(v -> {
            PolarDirectionCosines polDirCos = new PolarDirectionCosines();

            Bundle args = new Bundle();
            args.putString("num", String.valueOf(evalf(et1.getText().toString().trim())));
            polDirCos.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.cl_content_main, polDirCos) // Use your host container ID
                    .addToBackStack(null)
                    .commit();

        });

        return view;
    }

    public static class Cartesian extends Fragment {

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
                    customKeyboard.hideKeyboard();
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

            linearLayout.addView(img, 2);
            linearLayout.addView(btnCalc, 3);
            linearLayout.addView(tvRes, 4);
            linearLayout.addView(btnCopy, 5);


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

    public static class Polar extends Fragment {

    }

    public static class PolarDirectionCosines extends Fragment {

    }

    //a method, which will surrounded a given text with brackets, if it is not already surrounded by brackets
    public static String addBrackets(String s) {
        if (s.charAt(0) != '(') {
            s = "(" + s;
        }
        if (s.charAt(s.length() - 1) != ')') {
            s = s + ")";
        }
        return s;
    }

    //a method to check if the given string is a scalar (no comma)
    public static boolean isScalar(String s) {
        return !s.contains(",");
    }

}
