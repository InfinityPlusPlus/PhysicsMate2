package com.example.physicsmate.ui;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.physicsmate.R;
import com.example.physicsmate.model.KeyboardBtn;
import com.example.physicsmate.model.KeyboardBtnAdapter;
import com.google.android.flexbox.FlexboxLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CustomKeyboard extends ConstraintLayout {

    final double pi = Math.PI;
    final double e = Math.E;
    final double h = 6.626070040e-34;
    final double c = 299792458;
    final double G = 6.674010551359e-11;
    final double mu_0 = 4 * pi * 1e-7;
    final double eps_0 = 8.854187817e-12;
    final double m_e = 9.10938356e-31;
    final double m_p = 1.67262158e-27;
    final double m_n = 1.674927e-27;
    final double k_B = 1.380649e-23;
    final double Na = 6.02214076e23;
    final double sigma = 5.670367e-8;
    final double gas_constant = 8.31446261815324;
    public static EditText targetEditText;
    public View scrollView;
    ConstraintLayout keyboard_layout;
    RecyclerView rv_operator_bar;
    RecyclerView rv_keyboard_content;
    LinearLayout ll_tab_bar;

    public CustomKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setOrientation(VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.keyboard, this, true);

        setVisibility(GONE);

        keyboard_layout = findViewById(R.id.keyboard_layout);
        rv_keyboard_content = findViewById(R.id.rv_keyboard_content);
        ll_tab_bar = findViewById(R.id.ll_tab_bar);
        //setupOperatorBar();

        //get the current orientation of the device
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == 2) {
            keyboard_layout.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels / 2 + 150;
            System.out.println("H/2: " + keyboard_layout.getLayoutParams().height);
        }

        Button tab123 = findViewById(R.id.tab_numbers);
        Button tabABC = findViewById(R.id.tab_alphabets);
        Button tabGreek = findViewById(R.id.tab_greek);
        Button tabConst = findViewById(R.id.tab_constants);
        Button backspace = findViewById(R.id.backspace);
//        Button num = findViewById(R.id.num);
//        Button alphabets = findViewById(R.id.alphabets);
//        Button greek = findViewById(R.id.greek);
//        Button constants = findViewById(R.id.constants);
//
//        tab123.setOnClickListener(v -> {
//            //smooth scroll to num button
//            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, num.getTop()));
//        });
//
//        tabABC.setOnClickListener(v -> {
//            //smooth scroll to alphabets button
//            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, alphabets.getTop()));
//        });
//
//        tabGreek.setOnClickListener(v -> {
//            //smooth scroll to greek button
//            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, greek.getTop()));
//        });
//
//        tabConst.setOnClickListener(v -> {
//            //smooth scroll to constants button
//            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, constants.getTop()));
//        });


        backspace.setOnClickListener(v -> {
            int cursorPosition = targetEditText.getSelectionStart();
            if (targetEditText.getText() != null && cursorPosition > 0) {
                targetEditText.getText().delete(cursorPosition - 1, cursorPosition);
            }
        });

        backspace.setOnLongClickListener(v -> {
            int cursorPosition = targetEditText.getSelectionStart();
            if (targetEditText.getText() != null && cursorPosition > 0) {
                while (backspace.isPressed()) {
                    targetEditText.getText().delete(cursorPosition - 1, cursorPosition);
                }
            }
            return true;
        });

        rv_operator_bar = findViewById(R.id.rv_operator_bar);
        rv_keyboard_content = findViewById(R.id.rv_keyboard_content);
        //get the number of buttons in keyboard_content
        int numberOfButtons = rv_keyboard_content.getChildCount();

        int i;

//        for (i = 0; i < numberOfButtons; i++) {
//            Button button = (Button) rv_keyboard_content.getChildAt(i);
//            if (button.getId() != R.id.constants) {
//                button.setOnClickListener(v -> insertText(button.getText().toString()));
//            } else {
//                break;
//            }
//        }
//
//        for (; i < numberOfButtons; i++) {
//            Button button = (Button) rv_keyboard_content.getChildAt(i);
//            button.setOnClickListener(v -> insertTextConst(button.getText().toString()));
//        }

    }

    public void insertTextConst(String string) {
        int cursorPosition = targetEditText.getSelectionStart();
        Editable editable = targetEditText.getText();

        switch (string) {
            case "π":
                editable.insert(cursorPosition, String.valueOf(pi));
                break;
            case "e":
                editable.insert(cursorPosition, String.valueOf(e));
                break;
            case "ħ":
                editable.insert(cursorPosition, String.valueOf(h));
                break;
            case "c":
                editable.insert(cursorPosition, String.valueOf(c));
                break;
            case "G":
                editable.insert(cursorPosition, String.valueOf(G));
                break;
            case "μ₀":
                editable.insert(cursorPosition, String.valueOf(mu_0));
                break;
            case "ε₀":
                editable.insert(cursorPosition, String.valueOf(eps_0));
                break;
            case "mₑ":
                editable.insert(cursorPosition, String.valueOf(m_e));
                break;
            case "mₚ":
                editable.insert(cursorPosition, String.valueOf(m_p));
                break;
            case "mₙ":
                editable.insert(cursorPosition, String.valueOf(m_n));
                break;
            case "k":
                editable.insert(cursorPosition, String.valueOf(k_B));
                break;
            case "N":
                editable.insert(cursorPosition, String.valueOf(Na));
                break;
            case "σ":
                editable.insert(cursorPosition, String.valueOf(sigma));
                break;
            case "R":
                editable.insert(cursorPosition, String.valueOf(gas_constant));
                break;
        }
    }

    public void setupOperatorBar() {
        List<KeyboardBtn> keyboardBtns = Arrays.asList(
                new KeyboardBtn(".", "buttonDecimal"),
                new KeyboardBtn("+", "buttonPlus"),
                new KeyboardBtn("-", "buttonMinus"),
                new KeyboardBtn("*", "buttonMultiply"),
                new KeyboardBtn("/", "buttonDivide"),
                new KeyboardBtn("^", "button_power"),
                new KeyboardBtn("sqrt()", "button_root"),
                new KeyboardBtn("=", "button_equal"),
                new KeyboardBtn("||", "button_modulus"),
                new KeyboardBtn("!", "button_factorial"),
                new KeyboardBtn("π", "button_pi"),
                new KeyboardBtn("e", "button_e")
        );

        KeyboardBtnAdapter keyboardBtnAdapter = new KeyboardBtnAdapter(keyboardBtns, false);
        rv_operator_bar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_operator_bar.setAdapter(keyboardBtnAdapter);

        for (int i = 0; i < keyboardBtnAdapter.getItemCount(); i++) {
            Button button = (Button) rv_operator_bar.getChildAt(i);
            button.setOnClickListener(v -> insertText(button.getTag().toString()));
        }
    }

//    public void setupOperatorBar() {
//        int[] operatorIds = new int[]{R.id.buttonDecimal, R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMultiply,
//                R.id.buttonDivide, R.id.button_power, R.id.button_root, R.id.button_equal, R.id.button_modulus,
//                R.id.button_factorial, R.id.button_pi, R.id.button_e};
//
//        for (int id : operatorIds) {
//            Button btn = findViewById(id);
//
//            if (btn == null) continue;
//            if (btn.getId() == R.id.button_modulus) {
//                btn.setOnClickListener(v -> {
//                    insertText("||");
//                    //go back one character
//                    targetEditText.setSelection(targetEditText.getSelectionStart() - 1);
//                });
//            } else if (btn.getId() == R.id.button_factorial) {
//                btn.setOnClickListener(v -> {
//                    insertText("()!");
//                    //go back two character
//                    targetEditText.setSelection(targetEditText.getSelectionStart() - 2);
//                });
//            } else if (btn.getId() == R.id.button_root) {
//                btn.setOnClickListener(v -> {
//                    insertText("sqrt()");
//                    //go back one character
//                    targetEditText.setSelection(targetEditText.getSelectionStart() - 1);
//                });
//
//            } else {
//                String text = btn.getText().toString();
//                btn.setOnClickListener(v -> insertText(text));
//            }
//        }
//
//        int[] specOperatorIds = new int[]{R.id.buttonLn, R.id.buttonLog10, R.id.button_sin, R.id.button_cos, R.id.button_tan,
//                R.id.button_arcsin, R.id.button_arccos, R.id.button_arctan};
//
//        for (int id : specOperatorIds) {
//            Button btn = findViewById(id);
//            if (btn == null) continue;
//            CharSequence text = btn.getTag().toString();
//            btn.setOnClickListener(v -> {
//                insertText(text + "()");
//                //go back one character
//                targetEditText.setSelection(targetEditText.getSelectionStart() - 1);
//            });
//
//        }
//
//
//        Button decimalBtn = findViewById(R.id.buttonDecimal);
//        decimalBtn.setText(Html.fromHtml("<sup>.</sup> ,", Html.FROM_HTML_MODE_LEGACY));
//        decimalBtn.setOnLongClickListener(v -> {
//            insertText(", ");
//            return true;
//        });
//
//        Button addition = findViewById(R.id.buttonPlus);
//        addition.setText(Html.fromHtml("+ <sub>(</sub>", Html.FROM_HTML_MODE_LEGACY));
//        addition.setOnLongClickListener(v -> {
//            insertText(" ( ");
//            return true;
//        });
//
//        Button subtraction = findViewById(R.id.buttonMinus);
//        subtraction.setText(Html.fromHtml("- <sub>)</sub>", Html.FROM_HTML_MODE_LEGACY));
//        subtraction.setOnLongClickListener(v -> {
//            insertText(" ) ");
//            return true;
//        });
//    }


    public void setTargetEditText(EditText editText) {
        targetEditText = editText;
    }

    public void setScrollView(View view) {
        scrollView = view;
    }

    public void showKeyboard() {
        setVisibility(View.VISIBLE);
        if (scrollView != null) {
            post(() -> {
                scrollView.setPadding(0, 0, 0, getHeight());
                System.out.println("\n\n\ndone: " + getHeight() + "\n\n\n");
            });
        }
    }

    public void hideKeyboard() {
        if (scrollView != null) {
            scrollView.setPadding(0, 0, 0, 0);
        }
        setVisibility(View.GONE);
    }

    public static void insertText(CharSequence text) {
        if (targetEditText != null) {
            int cursorPosition = targetEditText.getSelectionStart();
            Editable editable = targetEditText.getText();
            editable.insert(cursorPosition, text);
        }
    }

}
