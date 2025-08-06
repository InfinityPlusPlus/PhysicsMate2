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
import com.example.physicsmate.R;
import com.google.android.flexbox.*;


public class CustomKeyboard extends LinearLayout {

    public static final double pi = Math.PI;
    public static final double e = Math.E;
    public static final double h = 6.626070040e-34;
    public static final double c = 299792458;
    public static final double G = 6.674010551359e-11;
    public static final double mu_0 = 4 * pi * 1e-7;
    public static final double eps_0 = 8.854187817e-12;
    public static final double m_e = 9.10938356e-31;
    public static final double m_p = 1.67262158e-27;
    public static final double m_n = 1.674927e-27;
    public static final double k_B = 1.380649e-23;
    public static final double Na = 6.02214076e23;
    public static final double sigma = 5.670367e-8;
    public static final double gas_constant = 8.31446261815324;
    private EditText targetEditText;
    private View scrollView;
    LinearLayout keyboard_layout, keyboard_tab_bar;
    ConstraintLayout keyboard_main_layout;
    FlexboxLayout keyboard_content;
    ScrollView keyboard_scroll_view;

    public CustomKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.keyboard, this, true);

        setVisibility(GONE);

        keyboard_layout = findViewById(R.id.keyboard_layout);
        keyboard_main_layout = findViewById(R.id.keyboard_main_layout);
        keyboard_scroll_view = findViewById(R.id.keyboard_scroll_view);
        setupOperatorBar();

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
        Button num = findViewById(R.id.num);
        Button alphabets = findViewById(R.id.alphabets);
        Button greek = findViewById(R.id.greek);
        Button constants = findViewById(R.id.constants);

        tab123.setOnClickListener(v -> {
            //smooth scroll to num button
            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, num.getTop()));
        });

        tabABC.setOnClickListener(v -> {
            //smooth scroll to alphabets button
            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, alphabets.getTop()));
        });

        tabGreek.setOnClickListener(v -> {
            //smooth scroll to greek button
            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, greek.getTop()));
        });

        tabConst.setOnClickListener(v -> {
            //smooth scroll to constants button
            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, constants.getTop()));
        });


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

        keyboard_tab_bar = findViewById(R.id.keyboard_tab_bar);
        keyboard_content = findViewById(R.id.keyboard_content);
        //get the number of buttons in keyboard_content
        int numberOfButtons = keyboard_content.getChildCount();

        int i;

        for (i = 0; i < numberOfButtons; i++) {
            Button button = (Button) keyboard_content.getChildAt(i);
            if (button.getId() != R.id.constants) {
                button.setOnClickListener(v -> insertText(button.getText().toString()));
            } else {
                break;
            }
        }

        for (; i < numberOfButtons; i++) {
            Button button = (Button) keyboard_content.getChildAt(i);
            button.setOnClickListener(v -> insertTextConst(button.getText().toString()));
        }

    }

    private void insertTextConst(String string) {
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

    private void setupOperatorBar() {
        int[] operatorIds = new int[]{R.id.buttonDecimal, R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMultiply, R.id.buttonDivide, R.id.button_power, R.id.button_root, R.id.button_equal, R.id.button_modulus, R.id.button_factorial, R.id.button_pi, R.id.button_e};

        for (int id : operatorIds) {
            Button btn = findViewById(id);

            if (btn == null) continue;
            if (btn.getId() == R.id.button_modulus) {
                btn.setOnClickListener(v -> {
                    insertText("||");
                    //go back one character
                    targetEditText.setSelection(targetEditText.getSelectionStart() - 1);
                });
            } else if (btn.getId() == R.id.button_factorial) {
                btn.setOnClickListener(v -> {
                    insertText("()!");
                    //go back two character
                    targetEditText.setSelection(targetEditText.getSelectionStart() - 2);
                });
            } else if (btn.getId() == R.id.button_root) {
                btn.setOnClickListener(v -> {
                    insertText("sqrt()");
                    //go back one character
                    targetEditText.setSelection(targetEditText.getSelectionStart() - 1);
                });

            } else {
                String text = btn.getText().toString();
                btn.setOnClickListener(v -> insertText(text));
            }
        }

        int[] specOperatorIds = new int[]{R.id.buttonLn, R.id.buttonLog10, R.id.button_sin, R.id.button_cos, R.id.button_tan, R.id.button_arcsin, R.id.button_arccos, R.id.button_arctan};

        for (int id : specOperatorIds) {
            Button btn = findViewById(id);
            if (btn == null) continue;
            CharSequence text = btn.getTag().toString();
            btn.setOnClickListener(v -> {
                insertText(text + "()");
                //go back one character
                targetEditText.setSelection(targetEditText.getSelectionStart() - 1);
            });

        }


        Button decimalBtn = findViewById(R.id.buttonDecimal);
        decimalBtn.setText(Html.fromHtml("<sup>.</sup> ,", Html.FROM_HTML_MODE_LEGACY));
        decimalBtn.setOnLongClickListener(v -> {
            insertText(", ");
            return true;
        });

        Button addition = findViewById(R.id.buttonPlus);
        addition.setText(Html.fromHtml("+ <sub>(</sub>", Html.FROM_HTML_MODE_LEGACY));
        addition.setOnLongClickListener(v -> {
            insertText(" ( ");
            return true;
        });

        Button subtraction = findViewById(R.id.buttonMinus);
        subtraction.setText(Html.fromHtml("- <sub>)</sub>", Html.FROM_HTML_MODE_LEGACY));
        subtraction.setOnLongClickListener(v -> {
            insertText(" ) ");
            return true;
        });
    }


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

    private void insertText(CharSequence text) {
        if (targetEditText != null) {
            int cursorPosition = targetEditText.getSelectionStart();
            Editable editable = targetEditText.getText();
            editable.insert(cursorPosition, text);
        }
    }

}


//__________________________________________________________________________________________________________
//
//
//public class CustomKeyboard extends ConstraintLayout {
//
//    final double pi = Math.PI;
//    final double e = Math.E;
//    final double h = 6.626070040e-34;
//    final double c = 299792458;
//    final double G = 6.674010551359e-11;
//    final double mu_0 = 4 * pi * 1e-7;
//    final double eps_0 = 8.854187817e-12;
//    final double m_e = 9.10938356e-31;
//    final double m_p = 1.67262158e-27;
//    final double m_n = 1.674927e-27;
//    final double k_B = 1.380649e-23;
//    final double Na = 6.02214076e23;
//    final double sigma = 5.670367e-8;
//    final double gas_constant = 8.31446261815324;
//    public static EditText targetEditText;
//    public View scrollView;
//    ConstraintLayout keyboard_layout;
//    RecyclerView rv_operator_bar;
//    RecyclerView rv_keyboard_content;
//    LinearLayout ll_tab_bar;
//
//    Button tab123;
//    Button tabABC;
//    Button tabGreek;
//    Button tabConst;
//
//
//    public CustomKeyboard(Context context, AttributeSet attrs)
//    {
//        super(context, attrs);
//        //setOrientation(VERTICAL);
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        inflater.inflate(R.layout.keyboard, this, true);
//
//        setVisibility(GONE);
//
//        keyboard_layout = findViewById(R.id.keyboard_layout);
//        rv_keyboard_content = findViewById(R.id.rv_keyboard_content);
//        ll_tab_bar = findViewById(R.id.ll_tab_bar);
//
//        //get the current orientation of the device
//        int orientation = getResources().getConfiguration().orientation;
//        if (orientation == 2) {
//            keyboard_layout.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels / 2 + 150;
//            System.out.println("H/2: " + keyboard_layout.getLayoutParams().height);
//        }
//
//        tab123 = findViewById(R.id.tab_numbers);
//        tabABC = findViewById(R.id.tab_alphabets);
//        tabGreek = findViewById(R.id.tab_greek);
//        tabConst = findViewById(R.id.tab_constants);
//        Button backspace = findViewById(R.id.backspace);
/// /        Button num = findViewById(R.id.num);
/// /        Button alphabets = findViewById(R.id.alphabets);
/// /        Button greek = findViewById(R.id.greek);
/// /        Button constants = findViewById(R.id.constants);
/// /
/// /        tab123.setOnClickListener(v -> {
/// /            //smooth scroll to num button
/// /            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, num.getTop()));
/// /        });
/// /
/// /        tabABC.setOnClickListener(v -> {
/// /            //smooth scroll to alphabets button
/// /            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, alphabets.getTop()));
/// /        });
/// /
/// /        tabGreek.setOnClickListener(v -> {
/// /            //smooth scroll to greek button
/// /            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, greek.getTop()));
/// /        });
/// /
/// /        tabConst.setOnClickListener(v -> {
/// /            //smooth scroll to constants button
/// /            keyboard_scroll_view.post(() -> keyboard_scroll_view.smoothScrollTo(0, constants.getTop()));
/// /        });
//
//
//        backspace.setOnClickListener(v -> {
//            int cursorPosition = targetEditText.getSelectionStart();
//            if (targetEditText.getText() != null && cursorPosition > 0) {
//                targetEditText.getText().delete(cursorPosition - 1, cursorPosition);
//            }
//        });
//
//        backspace.setOnLongClickListener(v -> {
//            int cursorPosition = targetEditText.getSelectionStart();
//            if (targetEditText.getText() != null && cursorPosition > 0) {
//                while (backspace.isPressed()) {
//                    targetEditText.getText().delete(cursorPosition - 1, cursorPosition);
//                }
//            }
//            return true;
//        });
//
//        rv_operator_bar = findViewById(R.id.rv_operator_bar);
//        rv_keyboard_content = findViewById(R.id.rv_keyboard_content);
//
//        System.out.println("\n\n\nrv_keyboard_content null? " + (rv_keyboard_content == null));
//
//        setupOperatorBar();
//        setupKeyboardContent();
//
//    }
//
//    public void setupKeyboardContent() {
//        List<KeyboardBtn> keyboardBtns = Arrays.asList(
//                new KeyboardBtn("1", "button1", "1"),
//                new KeyboardBtn("2", "button2", "2"),
//                new KeyboardBtn("3", "button3", "3"),
//                new KeyboardBtn("4", "button4", "4"),
//                new KeyboardBtn("5", "button5", "5"),
//                new KeyboardBtn("6", "button6", "6"),
//                new KeyboardBtn("7", "button7", "7"),
//                new KeyboardBtn("8", "button8", "8"),
//                new KeyboardBtn("9", "button9", "9"),
//                new KeyboardBtn("0", "button0", "0"),
//                new KeyboardBtn("a", "buttonA", "a"),
//                new KeyboardBtn("b", "buttonB", "b"),
//                new KeyboardBtn("c", "buttonC", "c"),
//                new KeyboardBtn("d", "buttonD", "d"),
//                new KeyboardBtn("e", "buttonE", "e"),
//                new KeyboardBtn("f", "buttonF", "f"),
//                new KeyboardBtn("g", "buttonG", "g"),
//                new KeyboardBtn("h", "buttonH", "h"),
//                new KeyboardBtn("i", "buttonI", "i"),
//                new KeyboardBtn("j", "buttonJ", "j"),
//                new KeyboardBtn("k", "buttonK", "k"),
//                new KeyboardBtn("l", "buttonL", "l"),
//                new KeyboardBtn("m", "buttonM", "m"),
//                new KeyboardBtn("n", "buttonN", "n"),
//                new KeyboardBtn("o", "buttonO", "o"),
//                new KeyboardBtn("p", "buttonP", "p"),
//                new KeyboardBtn("q", "buttonQ", "q"),
//                new KeyboardBtn("r", "buttonR", "r"),
//                new KeyboardBtn("s", "buttonS", "s"),
//                new KeyboardBtn("t", "buttonT", "t"),
//                new KeyboardBtn("u", "buttonU", "u"),
//                new KeyboardBtn("v", "buttonV", "v"),
//                new KeyboardBtn("w", "buttonW", "w"),
//                new KeyboardBtn("x", "buttonX", "x"),
//                new KeyboardBtn("y", "buttonY", "y"),
//                new KeyboardBtn("z", "buttonZ", "z"),
//                new KeyboardBtn("α", "buttonAlpha", "α"),
//                new KeyboardBtn("β", "buttonBeta", "β"),
//                new KeyboardBtn("γ", "buttonGamma", "γ"),
//                new KeyboardBtn("δ", "buttonDelta", "δ"),
//                new KeyboardBtn("ε", "buttonEpsilon", "ε"),
//                new KeyboardBtn("ζ", "buttonZeta", "ζ"),
//                new KeyboardBtn("η", "buttonEta", "η"),
//                new KeyboardBtn("θ", "buttonTheta", "θ"),
//                new KeyboardBtn("ι", "buttonIota", "ι"),
//                new KeyboardBtn("κ", "buttonKappa", "κ"),
//                new KeyboardBtn("λ", "buttonLambda", "λ"),
//                new KeyboardBtn("μ", "buttonMu", "μ"),
//                new KeyboardBtn("ν", "buttonNu", "ν"),
//                new KeyboardBtn("ξ", "buttonXi", "ξ"),
//                new KeyboardBtn("ο", "buttonOmicron", "ο"),
//                new KeyboardBtn("π", "buttonPi", "π"),
//                new KeyboardBtn("ρ", "buttonRho", "ρ"),
//                new KeyboardBtn("σ", "buttonSigma", "σ"),
//                new KeyboardBtn("τ", "buttonTau", "τ"),
//                new KeyboardBtn("υ", "buttonUpsilon", "υ"),
//                new KeyboardBtn("φ", "buttonPhi", "φ"),
//                new KeyboardBtn("χ", "buttonChi", "χ"),
//                new KeyboardBtn("ψ", "buttonPsi", "ψ"),
//                new KeyboardBtn("ω", "buttonOmega", "ω"),
//                new KeyboardBtn("△", "buttonTriangle", "△"),
//                new KeyboardBtn("□", "buttonSquare", "□"),
//                new KeyboardBtn("▽", "buttonTriangleDown", "▽"),
//                new KeyboardBtn("▷", "buttonTriangleRight", "▷"),
//                new KeyboardBtn("◁", "buttonTriangleLeft", "◁"),
//                new KeyboardBtn("⊥", "buttonPerpendicular", "⊥"),
//                new KeyboardBtn("⊙", "buttonCircleDot", "⊙"),
//                new KeyboardBtn("⊗", "buttonCircleCross", "⊗"),
//                new KeyboardBtn("•", "buttonDot", "•"),
//                new KeyboardBtn(":", "buttonColon", ":"),
//                new KeyboardBtn(";", "buttonSemicolon", ";"),
//                new KeyboardBtn("{", "buttonBraceLeft", "{"),
//                new KeyboardBtn("}", "buttonBraceRight", "}"),
//                new KeyboardBtn("[", "buttonBracketLeft", "["),
//                new KeyboardBtn("]", "buttonBracketRight", "]"),
//                new KeyboardBtn("×", "buttonCross", "×"),
//                new KeyboardBtn("÷", "buttonDivide", "÷"),
//                new KeyboardBtn("ʃ", "buttonIntegral", "ʃ"),
//                new KeyboardBtn("∮", "buttonLineIntegral", "∮"),
//                new KeyboardBtn("∯", "buttonSurfaceIntegral", "∯"),
//                new KeyboardBtn("∰", "buttonVolumeIntegral", "∰"),
//                new KeyboardBtn("∑", "buttonSum", "∑"),
//                new KeyboardBtn("∏", "buttonProduct", "∏"),
//
//                new KeyboardBtn("π", "buttonPi", "π"),
//                new KeyboardBtn("e", "buttonE", "e"),
//                new KeyboardBtn("h", "buttonH", "h"),
//                new KeyboardBtn("c", "buttonC", "c"),
//                new KeyboardBtn("G", "buttonG", "G"),
//                new KeyboardBtn("μ0", "buttonMu0", "μ0"),
//                new KeyboardBtn("ε0", "buttonEpsilon0", "ε0"),
//                new KeyboardBtn("me", "buttonMe", "me"),
//                new KeyboardBtn("mp", "buttonMp", "mp"),
//                new KeyboardBtn("mn", "buttonMn", "mn"),
//                new KeyboardBtn("kB", "buttonKb", "kB"),
//                new KeyboardBtn("Na", "buttonNa", "Na"),
//                new KeyboardBtn("σ", "buttonSigma", "σ"),
//                new KeyboardBtn("R", "buttonR", "R"),
//                new KeyboardBtn("k", "buttonK", "k")
//        );
//
//        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
//        layoutManager.setFlexDirection(FlexDirection.ROW);
//        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
//        layoutManager.setFlexWrap(FlexWrap.WRAP);
//        rv_keyboard_content.setLayoutManager(layoutManager);
//
//        KeyboardBtnAdapter keyboardBtnAdapter = new KeyboardBtnAdapter(keyboardBtns, false);
//        rv_keyboard_content.setAdapter(keyboardBtnAdapter);
//
//        for (int i = 0; i < keyboardBtnAdapter.getItemCount(); i++) {
//            Button button = (Button) rv_operator_bar.getChildAt(i);
//            KeyboardBtn keyboardBtn = keyboardBtnAdapter.getKeyboardBtn(i);
//            button.setOnClickListener(v -> insertText(keyboardBtn.stringToInsert));
//            String tag = keyboardBtn.tag;
//            switch (tag) {
//                case "buttonPi":
//                    button.setOnClickListener(v -> insertTextConst(pi));
//                    break;
//                case "buttonE":
//                    button.setOnClickListener(v -> insertTextConst(e));
//                    break;
//                case "buttonH":
//                    button.setOnClickListener(v -> insertTextConst(h));
//                    break;
//                case "buttonC":
//                    button.setOnClickListener(v -> insertTextConst(c));
//                    break;
//                case "buttonG":
//                    button.setOnClickListener(v -> insertTextConst(G));
//                    break;
//                case "buttonMu0":
//                    button.setOnClickListener(v -> insertTextConst(mu_0));
//                    break;
//                case "buttonEpsilon0":
//                    button.setOnClickListener(v -> insertTextConst(eps_0));
//                    break;
//                case "buttonMe":
//                    button.setOnClickListener(v -> insertTextConst(m_e));
//                    break;
//                case "buttonMp":
//                    button.setOnClickListener(v -> insertTextConst(m_p));
//                    break;
//                case "buttonMn":
//                    button.setOnClickListener(v -> insertTextConst(m_n));
//                    break;
//                case "buttonKb":
//                    button.setOnClickListener(v -> insertTextConst(k_B));
//                    break;
//                case "buttonNa":
//                    button.setOnClickListener(v -> insertTextConst(Na));
//                    break;
//                case "buttonSigma":
//                    button.setOnClickListener(v -> insertTextConst(sigma));
//                    break;
//                case "buttonR":
//                    button.setOnClickListener(v -> insertTextConst(gas_constant));
//                    break;
//            }
//
//        }
//
//        final int numStart   = findPositionByTag(keyboardBtns, "button1");
//        final int abcStart   = findPositionByTag(keyboardBtns, "buttonA");
//        final int greekStart = findPositionByTag(keyboardBtns, "buttonAlpha");
//        final int constStart = findPositionByTag(keyboardBtns, "buttonPi");
//
//        // 4) wire up your tab buttons to scroll to those positions
//        tab123.setOnClickListener(v ->
//                rv_keyboard_content.post(() ->
//                        rv_keyboard_content.smoothScrollToPosition(numStart)
//                )
//        );
//        tabABC.setOnClickListener(v ->
//                rv_keyboard_content.post(() ->
//                        rv_keyboard_content.smoothScrollToPosition(abcStart)
//                )
//        );
//        tabGreek.setOnClickListener(v ->
//                rv_keyboard_content.post(() ->
//                        rv_keyboard_content.smoothScrollToPosition(greekStart)
//                )
//        );
//        tabConst.setOnClickListener(v ->
//                rv_keyboard_content.post(() ->
//                        rv_keyboard_content.smoothScrollToPosition(constStart)
//                )
//        );
//
//    }
//
//    public void setupOperatorBar()
//    {
//        List<KeyboardBtn> keyboardBtns = Arrays.asList(
//                new KeyboardBtn(".", "buttonDecimal", "."),
//                new KeyboardBtn("+", "buttonPlus", "+"),
//                new KeyboardBtn("-", "buttonMinus", "-"),
//                new KeyboardBtn("*", "buttonMultiply", "×"),
//                new KeyboardBtn("/", "buttonDivide", "÷"),
//                new KeyboardBtn("=", "button_equal", "="),
//                new KeyboardBtn("^", "button_power", "^"),
//                new KeyboardBtn("sqrt()", "button_root", "√"),
//                new KeyboardBtn("||", "button_modulus", "||"),
//                new KeyboardBtn("fact()", "button_factorial", "n!"),
//                new KeyboardBtn("sin()", "button_sin", "sin"),
//                new KeyboardBtn("cos()", "button_cos", "cos"),
//                new KeyboardBtn("tan()", "button_tan", "tan"),
//                new KeyboardBtn("log10()", "button_log", "log10"),
//                new KeyboardBtn("ln()", "button_ln", "ln"),
//                new KeyboardBtn("arcsin()", "button_arcsin", "sin⁻¹"),
//                new KeyboardBtn("arccos()", "button_arccos", "cos⁻¹"),
//                new KeyboardBtn("arctan()", "button_arctan", "tan⁻¹"),
//                new KeyboardBtn("π", "button_pi", "π"),
//                new KeyboardBtn("e", "button_e", "e")
//        );
//
//        KeyboardBtnAdapter keyboardBtnAdapter = new KeyboardBtnAdapter(keyboardBtns, false);
//        System.out.println("keyboardBtns.size(): " + keyboardBtns.size());
//        rv_operator_bar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        rv_operator_bar.setAdapter(keyboardBtnAdapter);
//
//        for (int i = 0; i < keyboardBtnAdapter.getItemCount(); i++) {
//            Button button = (Button) rv_operator_bar.getChildAt(i);
//            KeyboardBtn keyboardBtn = keyboardBtnAdapter.getKeyboardBtn(i);
//            button.setOnClickListener(v -> insertText(keyboardBtn.stringToInsert));
//
//            if (keyboardBtn.tag.equals("sqrt()") || keyboardBtn.tag.equals("fact()") ||
//                    keyboardBtn.tag.equals("||") || keyboardBtn.tag.equals("sin()") ||
//                    keyboardBtn.tag.equals("cos()") || keyboardBtn.tag.equals("tan()") ||
//                    keyboardBtn.tag.equals("log10()") || keyboardBtn.tag.equals("ln()") ||
//                    keyboardBtn.tag.equals("arcsin()") || keyboardBtn.tag.equals("arccos()") ||
//                    keyboardBtn.tag.equals("arctan()"))
//            {
//                button.setOnClickListener(v -> targetEditText.setSelection(targetEditText.getSelectionStart() - 1));
//            }
//        }
//    }
//
//    // helper method to find the first index whose tag matches
//    private int findPositionByTag(List<KeyboardBtn> list, String tag) {
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).tag.equals(tag)) {
//                return i;
//            }
//        }
//        // fallback to 0 if not found
//        return 0;
//    }
//
//
//    public void insertTextConst(double val) {
//        int cursorPosition = targetEditText.getSelectionStart();
//        Editable editable = targetEditText.getText();
//        String string = String.valueOf(val);
//        switch (string) {
//            case "π":
//                editable.insert(cursorPosition, String.valueOf(pi));
//                break;
//            case "e":
//                editable.insert(cursorPosition, String.valueOf(e));
//                break;
//            case "ħ":
//                editable.insert(cursorPosition, String.valueOf(h));
//                break;
//            case "c":
//                editable.insert(cursorPosition, String.valueOf(c));
//                break;
//            case "G":
//                editable.insert(cursorPosition, String.valueOf(G));
//                break;
//            case "μ₀":
//                editable.insert(cursorPosition, String.valueOf(mu_0));
//                break;
//            case "ε₀":
//                editable.insert(cursorPosition, String.valueOf(eps_0));
//                break;
//            case "mₑ":
//                editable.insert(cursorPosition, String.valueOf(m_e));
//                break;
//            case "mₚ":
//                editable.insert(cursorPosition, String.valueOf(m_p));
//                break;
//            case "mₙ":
//                editable.insert(cursorPosition, String.valueOf(m_n));
//                break;
//            case "k":
//                editable.insert(cursorPosition, String.valueOf(k_B));
//                break;
//            case "N":
//                editable.insert(cursorPosition, String.valueOf(Na));
//                break;
//            case "σ":
//                editable.insert(cursorPosition, String.valueOf(sigma));
//                break;
//            case "R":
//                editable.insert(cursorPosition, String.valueOf(gas_constant));
//                break;
//        }
//    }
/// /    public void setupOperatorBar() {
/// /        int[] operatorIds = new int[]{R.id.buttonDecimal, R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMultiply,
/// /                R.id.buttonDivide, R.id.button_power, R.id.button_root, R.id.button_equal, R.id.button_modulus,
/// /                R.id.button_factorial, R.id.button_pi, R.id.button_e};
/// /
/// /        for (int id : operatorIds) {
/// /            Button btn = findViewById(id);
/// /
/// /            if (btn == null) continue;
/// /            if (btn.getId() == R.id.button_modulus) {
/// /                btn.setOnClickListener(v -> {
/// /                    insertText("||");
/// /                    //go back one character
/// /                    targetEditText.setSelection(targetEditText.getSelectionStart() - 1);
/// /                });
/// /            } else if (btn.getId() == R.id.button_factorial) {
/// /                btn.setOnClickListener(v -> {
/// /                    insertText("()!");
/// /                    //go back two character
/// /                    targetEditText.setSelection(targetEditText.getSelectionStart() - 2);
/// /                });
/// /            } else if (btn.getId() == R.id.button_root) {
/// /                btn.setOnClickListener(v -> {
/// /                    insertText("sqrt()");
/// /                    //go back one character
/// /                    targetEditText.setSelection(targetEditText.getSelectionStart() - 1);
/// /                });
/// /
/// /            } else {
/// /                String text = btn.getText().toString();
/// /                btn.setOnClickListener(v -> insertText(text));
/// /            }
/// /        }
/// /
/// /        int[] specOperatorIds = new int[]{R.id.buttonLn, R.id.buttonLog10, R.id.button_sin, R.id.button_cos, R.id.button_tan,
/// /                R.id.button_arcsin, R.id.button_arccos, R.id.button_arctan};
/// /
/// /        for (int id : specOperatorIds) {
/// /            Button btn = findViewById(id);
/// /            if (btn == null) continue;
/// /            CharSequence text = btn.getTag().toString();
/// /            btn.setOnClickListener(v -> {
/// /                insertText(text + "()");
/// /                //go back one character
/// /                targetEditText.setSelection(targetEditText.getSelectionStart() - 1);
/// /            });
/// /
/// /        }
/// /
/// /
/// /        Button decimalBtn = findViewById(R.id.buttonDecimal);
/// /        decimalBtn.setText(Html.fromHtml("<sup>.</sup> ,", Html.FROM_HTML_MODE_LEGACY));
/// /        decimalBtn.setOnLongClickListener(v -> {
/// /            insertText(", ");
/// /            return true;
/// /        });
/// /
/// /        Button addition = findViewById(R.id.buttonPlus);
/// /        addition.setText(Html.fromHtml("+ <sub>(</sub>", Html.FROM_HTML_MODE_LEGACY));
/// /        addition.setOnLongClickListener(v -> {
/// /            insertText(" ( ");
/// /            return true;
/// /        });
/// /
/// /        Button subtraction = findViewById(R.id.buttonMinus);
/// /        subtraction.setText(Html.fromHtml("- <sub>)</sub>", Html.FROM_HTML_MODE_LEGACY));
/// /        subtraction.setOnLongClickListener(v -> {
/// /            insertText(" ) ");
/// /            return true;
/// /        });
/// /    }
//
//
//    public static void insertText(CharSequence text) {
//        if (targetEditText != null) {
//            int cursorPosition = targetEditText.getSelectionStart();
//            Editable editable = targetEditText.getText();
//            editable.insert(cursorPosition, text);
//        }
//    }
//
//    public void setTargetEditText(EditText editText) {
//        targetEditText = editText;
//    }
//
//    public void setScrollView(View view) {
//        scrollView = view;
//    }
//
//    public void showKeyboard() {
//        setVisibility(View.VISIBLE);
//        if (scrollView != null) {
//            post(() -> {
//                scrollView.setPadding(0, 0, 0, getHeight());
//                System.out.println("\n\n\ndone: " + getHeight() + "\n\n\n");
//            });
//        }
//    }
//
//    public void hideKeyboard() {
//        if (scrollView != null) {
//            scrollView.setPadding(0, 0, 0, 0);
//        }
//        setVisibility(View.GONE);
//    }
//
//
//}
