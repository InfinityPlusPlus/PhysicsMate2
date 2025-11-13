package com.example.physicsmate.misc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.agog.mathdisplay.MTMathView;
import com.airbnb.paris.Paris;
import com.example.physicsmate.R;
import com.example.physicsmate.ui.CustomKeyboard;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.S;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.lab4inf.math.util.Accuracy.round;
import static org.matheclipse.core.expression.F.eval;

public class Custom_methods {

    static String StringError = "Error";
    static String eString = "2.71828182845904523536";
    static String phiString = "1.61803398874989484820";

    /*public static int getNumberOfFractionDigits(@org.jetbrains.annotations.Nullable Number number) {
        if (number == null) {
            return 0; // If the number is null, there are no fraction digits.
        }

        if (number.doubleValue() == 0.0d) {
            return 0; // If the number is zero, there are no fraction digits.
        }

        BigDecimal bd = new BigDecimal(number.toString());
        bd = bd.stripTrailingZeros(); // Remove trailing zeros to get the correct scale.
        return bd.scale();
    }*/


    public static List[] et_tv_dynamic_adder(LinearLayout linearLayout, int N, Context context) {
        List<EditText> editTextList = null;
        List[] viewList = null;
        List<TextView> textViewList = null;
        for (int i = 1; i <= N; i++) {
            TextView textView = new TextView(context);

            textViewList = new ArrayList<>();
            editTextList = new ArrayList<>();

            viewList = new List[2];

            textView.setText("Enter value " + i);
            textView.setId(View.generateViewId());
            Paris.styleBuilder(textView).add(R.style.custom_textView).apply();

            EditText editText = new EditText(context);
            editText.setId(View.generateViewId());
            Paris.styleBuilder(editText).add(R.style.custom_edittext).apply();

            linearLayout.addView(textView);
            textViewList.add(textView);

            linearLayout.addView(editText);
            editTextList.add(editText);
        }

        assert viewList != null;
        viewList[0] = textViewList;
        viewList[1] = editTextList;

        return viewList;
    }


    //method to round a number to the least number of decimal places from all the edit texts

    /**
     * Returns a rounded string representation of the given number, rounded to the least number
     * of decimal places from all the EditTexts.
     *
     * @param t         the number to round
     * @param editTexts the EditTexts containing the numbers
     * @return a rounded string representation of the number
     */
    public static String rounded_String(double t, EditText... editTexts) {
        //from the edittexts, get the least number of decimal places
        int minDecimalPlaces = Integer.MAX_VALUE;

        for (EditText editText : editTexts) {
            String text = editText.getText().toString();
            int decimalPlaces = findLeastSigDigits(text);

            if (decimalPlaces < minDecimalPlaces) {
                minDecimalPlaces = decimalPlaces;
            }
        }
        // Use the minDecimalPlaces value for rounding the number
        // ...
        return String.valueOf(round(t, minDecimalPlaces));
    }

    public static int countSignificantDigits(String numberStr) {
        if (numberStr.contains("e") || numberStr.contains("E")) {
            String[] parts = numberStr.split("[eE]");
            return countSignificantDigits(parts[0]);
        }

        numberStr = numberStr.replaceFirst("^0+", ""); // remove leading zeros
        if (numberStr.contains(".")) {
            // Decimal number
            numberStr = numberStr.replaceFirst("^\\.", ""); // remove lone leading dot
            numberStr = numberStr.replaceFirst("0+(?!\\d)", ""); // remove trailing non-digit zeros
            numberStr = numberStr.replaceFirst("^0+", ""); // remove again leading zeros
            return numberStr.replace(".", "").length();
        } else {
            // Integer (no decimal point)
            return numberStr.replaceAll("^0+", "").length();
        }
    }

    public static int findLeastSigDigits(CharSequence expr) {
        Pattern pattern = Pattern.compile("(?<![a-zA-Z])(?:\\d*\\.\\d+|\\d+\\.?\\d*)(?:[eE][+-]?\\d+)?");
        Matcher matcher = pattern.matcher(expr);

        int minSig = Integer.MAX_VALUE;

        while (matcher.find()) {
            String num = matcher.group();
            int sig = countSignificantDigits(num);
            minSig = Math.min(minSig, sig);
        }

        return minSig == Integer.MAX_VALUE ? 0 : minSig;
    }

    //method to hide keyboard whenever the given button is clicked
    public static void hideKeyboardOnClick(Button button, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(button.getWindowToken(), 0);
    }


    /**
     * Evaluates a mathematical expression and returns the result as a double.
     *
     * @param str the mathematical expression to be evaluated
     * @return the result of the evaluation as a double
     */
    public static double evalf(@NotNull String str) {
        ExprEvaluator util = new ExprEvaluator();
        str = str.replaceAll("\\b(\\d+(?:\\.\\d+)?)[eE]([+-]?\\d+)\\b", "$1 * 10^$2");
        str = str.replaceAll("log10\\((.*?)\\)", "ln($1)/ln(10)");
        str = str.replaceAll("phi", phiString);
        str = str.replaceAll("π", "Pi");
        str = str.replaceAll(",", "");
        str = str.replace("e", eString);
        try {
            String temp = String.valueOf(util.eval(IExprConverter(str)));
            return util.evalf(temp);

        } catch (MathException e) {
            return 12.342;
        }
    }

    //round to nearest integer
    public static int evalfInt(@NotNull String str) {
        ExprEvaluator util = new ExprEvaluator();
        str = str.replaceAll("\\b(\\d+(?:\\.\\d+)?)[eE]([+-]?\\d+)\\b", "$1 * 10^$2");
        str = str.replaceAll("log10\\((.*?)\\)", "ln($1)/ln(10)");
        str = str.replaceAll("phi", phiString);
        str = str.replaceAll("π", "Pi");
        str = str.replaceAll(",", "");
        str = str.replace("e", eString);
        try {
            String temp = String.valueOf(util.eval(IExprConverter(str)));
            return Integer.parseInt(temp);

        } catch (MathException e) {
            return 0;
        }
    }

    public static double evalf(@NotNull String str, boolean isComma) {

        ExprEvaluator util = new ExprEvaluator();
        str = str.replaceAll("\\b(\\d+(?:\\.\\d+)?)[eE]([+-]?\\d+)\\b", "$1 * 10^$2");
        str = str.replaceAll("log10\\((.*?)\\)", "ln($1)/ln(10)");
        str = str.replaceAll("phi", phiString);
        str = str.replaceAll("π", "Pi");
        str = str.replace("e", eString);

        if (!isComma) {
            str = str.replace(",", "");
        }

        try {
            String temp = String.valueOf(util.eval(IExprConverter(str)));
            return util.evalf(temp);

        } catch (MathException e) {
            return 12.342;
        }
    }


    public static ArrayList<String> IntersectionExtractor(CharSequence str) {

        // Define the regex pattern to match the x and y values in "{{y->0.279660012911754,x->1.4843853512085262}}"
        String regex = "\\{\\{y->(.*?),x->(.*?)\\}\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        // Extract the x and y values if the pattern matches
        if (matcher.find()) {
            String yValue = matcher.group(1);
            String xValue = matcher.group(2);
            ArrayList<String> list = new ArrayList<>();
            list.add(xValue);
            list.add(yValue);

            return list;

        } else {
            System.out.println("No intersection found.");
        }

        return null;
    }

    //solver for multi variable equations
    public static String solveEquationSymbolic(Iterable<String> equation, String variables) {
        try {
            //store the eqns in string form: "{eqn1, eqn2, eqn3, ...}"
            StringBuilder eqns = new StringBuilder("{");
            for (String s : equation) {
                eqns.append(s).append(",");
            }
            eqns = new StringBuilder(eqns.substring(0, eqns.length() - 1) + "}");
            eqns = new StringBuilder(eqns.toString().replace("=", "=="));

            //solve the eqns
            ExprEvaluator evaluator = new ExprEvaluator();
            IExpr result = evaluator.eval("Solve(" + eqns + ", {" + variables + "})");
            return result.toString();

        } catch (SyntaxError e) {
            // catch Symja parser errors here
            return "Syntax error";
        } catch (MathException me) {
            // catch Symja math errors here
            return "Math error";
        } catch (Exception ignored) {

        }
        return StringError;
    }

    //a function to extract numerical values from a string
    public static String extractNumberFromString(CharSequence input) {

        Matcher matcher = Pattern.compile("<font color='#CDDC39'>([\\d.]+)</font>").matcher(input);
        if (matcher.find()) {
            return (Objects.requireNonNull(matcher.group(1)));
        }
        return "0.0";
    }

    public static Double extractValue(CharSequence input, String variable) {
        Matcher matcher = Pattern.compile(variable + "= </font><font color='#CDDC39'>([\\d.]+)</font>").matcher(input);
        if (matcher.find()) {
            return evalf(Objects.requireNonNull(matcher.group(1)));
        }
        return null;
    }

    /**
     * A function to solve a system of numeric equations.
     *
     * @param equation  the iterable of equations to be solved
     * @param variables the string representation of the variables in the equations
     * @return the result of solving the equations as a string
     */
    public static String solveEquationNumeric(Iterable<String> equation, String variables) {
        try {
            //store the eqns in string form: "{eqn1, eqn2, eqn3, ...}"
            StringBuilder eqns = new StringBuilder("{");
            for (String s : equation) {
                eqns.append(s).append(",");
            }
            eqns = new StringBuilder(eqns.substring(0, eqns.length() - 1) + "}");
            eqns = new StringBuilder(eqns.toString().replace("=", "=="));

            //solve the eqns
            ExprEvaluator evaluator = new ExprEvaluator(true, (short) 0);
            IExpr result = evaluator.eval("NSolve(" + eqns + ", {" + variables + "})");

            //clear cache
            evaluator.clearVariables();

            return String.valueOf(result);

        } catch (SyntaxError e) {
            // catch Symja parser errors here
            return "Syntax error";
        } catch (MathException me) {
            // catch Symja math errors here
            return "Math error";
        } catch (Exception ignored) {
        }
        return StringError;

    }

    public static String addVectors(String[] vectors) {
        if (vectors == null || vectors.length == 0) {
            return "Empty input";
        }

        // Split the first vector to determine its dimension
        String[] firstVector = vectors[0].substring(1, vectors[0].length() - 1).split(",");
        int dimension = firstVector.length;

        // Initialize an array to hold the sum of components
        double[] sum = new double[dimension];

        // Process each vector
        for (String vector : vectors) {
            String[] components = vector.substring(1, vector.length() - 1).split(",");

            // Ensure all vectors have the same dimension
            if (components.length != dimension) {
                throw new IllegalArgumentException("All vectors must have the same dimension.");
            }

            // Add components to the sum array
            for (int i = 0; i < dimension; i++) {
                sum[i] += evalf(components[i].trim());
            }
        }

        // Convert the sum array back to a string in Cartesian form
        StringBuilder result = new StringBuilder("(");
        for (int i = 0; i < dimension; i++) {
            result.append(sum[i]);
            if (i < dimension - 1) {
                result.append(", ");
            }
        }
        result.append(")");

        return result.toString();
    }

    public static String addPolarVectors(String[] polarVectors) {
        if (polarVectors == null || polarVectors.length == 0) {
            return "Empty input";
        }

        // Determine the dimension from the first vector
        String[] firstVector = polarVectors[0].replaceAll("[()]", "").split(",");
        int dimension = firstVector.length;

        // Initialize an array to hold the sum in Cartesian form
        double[] cartesianSum = new double[dimension];

        // Process each polar vector
        for (String polarVector : polarVectors) {

            //remove only the first and last bracket, not the in between brackets
            String vector = polarVector.substring(1, polarVector.length() - 1);
            System.out.println(vector);
            String[] components = vector.split(",");

            // Ensure all vectors have the same dimension
            if (components.length != dimension) {
                throw new IllegalArgumentException("All vectors must have the same dimension.");
            }

            // Convert polar coordinates to Cartesian and add
            double[] cartesian = polarToCartesian(components);
            for (int i = 0; i < dimension; i++) {
                cartesianSum[i] += cartesian[i];
            }
        }


        // Convert the resultant Cartesian sum back to polar coordinates
        double[] resultantPolar = cartesianToPolar(cartesianSum);

        // Format the result as a polar coordinate string
        StringBuilder result = new StringBuilder("(");
        for (int i = 0; i < resultantPolar.length; i++) {
            result.append(resultantPolar[i]);
            if (i < resultantPolar.length - 1) {
                result.append(", ");
            }
        }
        result.append(")");

        return result.toString();
    }

    // Convert polar coordinates to Cartesian coordinates
    public static double[] polarToCartesian(String[] polarComponents) {
        int n = polarComponents.length;
        double[] cartesian = new double[n];

        System.out.println("\nr: " + polarComponents[0].trim() + "\n");
        double r = evalf(polarComponents[0].trim()); // Magnitude
        double[] angles = new double[n - 1];
        for (int i = 1; i < n; i++) {
            angles[i - 1] = Math.toRadians(evalf(polarComponents[i].trim())); // Angle is given in degrees
        }

        // Compute Cartesian coordinates
        cartesian[0] = r * Math.cos(angles[0]); // First axis (x-axis)
        for (int i = 1; i < n - 1; i++) {
            cartesian[i] = r * Math.cos(angles[i]) * Arrays.stream(angles, 0, i).map(Math::sin).reduce(1, (a, b) -> a * b); // Multiply by sines of previous angles
        }
        cartesian[n - 1] = r * Arrays.stream(angles).map(Math::sin).reduce(1, (a, b) -> a * b); // Last component

        return cartesian;
    }

    // Convert Cartesian coordinates to polar coordinates
    public static double[] cartesianToPolar(double[] cartesian) {
        int n = cartesian.length;
        double[] polar = new double[n];

        // Compute radius
        polar[0] = Math.sqrt(Arrays.stream(cartesian).map(x -> x * x).sum());
        System.out.println("\n\n" + polar[0] + "\n\n");

        // Compute angles
        for (int i = 1; i < n; i++) {
            polar[i] = Math.toDegrees(Math.acos(cartesian[i - 1] / polar[0]));
            System.out.println("\n\n" + polar[i] + "\n\n");
        }

        return polar;
    }

    public static String addPolarVectorsDirnCosines(String[] vectors) throws IllegalArgumentException {
        // Parse the input strings into magnitudes and direction cosines
        List<Double> magnitudes = new ArrayList<>();
        List<double[]> directionCosines = new ArrayList<>();

        for (String vector : vectors) {
            // Remove parentheses and split the string
            vector = vector.substring(1, vector.length() - 1);
            String[] components = vector.split(",");

            // Parse the magnitude and direction cosines
            double magnitude = evalf(components[0]);
            magnitudes.add(magnitude);

            double[] cosines = new double[components.length - 1];
            double sumOfSquares = 0.0;

            for (int i = 1; i < components.length; i++) {
                cosines[i - 1] = evalf(components[i]);
                sumOfSquares += cosines[i - 1] * cosines[i - 1];
            }

            // Check if the sum of squares of direction cosines equals 1 (within tolerance)
            if (Math.abs(sumOfSquares - 1.0) > 1e-6) {
                throw new IllegalArgumentException("Invalid direction cosines for vector: (" + vector + "). Sum of squares = " + sumOfSquares + " (must be 1).");
            }

            directionCosines.add(cosines);
        }

        // Check the number of dimensions
        int dimensions = directionCosines.get(0).length;
        double[] sumCartesian = new double[dimensions];

        // Convert each vector to Cartesian form and add them
        for (int i = 0; i < magnitudes.size(); i++) {
            double magnitude = magnitudes.get(i);
            double[] cosines = directionCosines.get(i);

            for (int j = 0; j < dimensions; j++) {
                sumCartesian[j] += magnitude * cosines[j]; // Add Cartesian components
            }
        }

        // Calculate the resulting magnitude
        double rSum = 0;
        for (double component : sumCartesian) {
            rSum += component * component;
        }
        rSum = Math.sqrt(rSum);

        // Calculate the resulting direction cosines
        double[] resultCosines = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            resultCosines[i] = sumCartesian[i] / rSum;
        }

        // Format the output
        StringBuilder resultCosineStr = new StringBuilder();
        for (int i = 0; i < dimensions; i++) {
            resultCosineStr.append(resultCosines[i]);
            if (i < dimensions - 1) {
                resultCosineStr.append(", ");
            }
        }

        return "Magnitude: " + rSum + ", Direction Cosines: (" + resultCosineStr + ")";
    }


    public static String parseStringAfterSolvingEqn(Context context, String input) {
        String br = "<br/>";
        // Replace },{ with <br/>OR<br/>
        String result = input.replace("},{", "<br/><br/>OR<br/><br/>");
        // Replace {{ with <br/>
        result = result.replace("{{", br);
        // Replace }} with <br/>
        result = result.replace("}}", br);
        // Replace { with <br/>
        result = result.replace("{", br);
        // Replace } with <br/>
        result = result.replace("}", br);
        // Replace , with <br/>
        result = result.replace(",", br);
        // Replace -> with =
        result = result.replace("->", "=");

        //make the numerical part #CDDC39 and the text part in #6BBBE1
        List<String> resultSplit = Arrays.asList(result.split(br));
        List<String> resultSplitParsed = resultSplit.stream().map(s -> {
            if (s.contains("=")) {
                String[] split = s.split("=");
                return HtmlNumberFormatter(context, split[0] + " = " + split[1]);
            } else {
                return s;
            }
        }).collect(Collectors.toList());

        result = String.join(br, resultSplitParsed);

        return result;
    }

    //a method, when given a string, makes the number, comma and brackets in yellow color, and the text in blue
    public static String HtmlNumberFormatter(Context context, String input) {
        // Retrieve the color from colors.xml
        int color = ContextCompat.getColor(context, R.color.yellow_num_answer);
        // Convert color to hex string
        String hexColor = String.format("#%06X", (0xFFFFFF & color));

        // Replace numbers (including decimals) with formatted color
        input = input.replaceAll("(?<![a-zA-Z_])(-?\\d*\\.?\\d+([eE][-+]?\\d+)?)", "<font color='" + hexColor + "'>$1</font>");

        return input;
    }

    //a method, when given a string, makes it surrounded by yellow color
    public static String HtmlColoriser(Context context, String input) {
        // Retrieve the color from resources
        int color = ContextCompat.getColor(context, R.color.yellow_num_answer);
        // Convert the color integer to a hexadecimal string
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        // Return the input string wrapped in a span with the specified color
        return "<span style=\"color:" + hexColor + "\">" + input + "</span>";
    }

    public static String replaceNumbersInTrigWithRemainder(String expression) {
        String modifiedExpression = expression;

        // Regular expression pattern to match trigonometric functions
        String pattern = "(sin|cos|tan)\\(([^)]+)\\)";

        // Create a pattern object
        Pattern regex = Pattern.compile(pattern);

        // Create a matcher object with the input expression
        Matcher matcher = regex.matcher(expression);

        // Iterate through the matches
        while (matcher.find()) {
            // Get the trigonometric function
            String trigFunction = matcher.group(1); // sin, cos, or tan

            // Get the argument of the trigonometric function
            String argValueString = matcher.group(2);
            assert argValueString != null;
            String argValue = argValueString.replaceAll("[()]", ""); // Remove parentheses before parsing

            // Evaluate the argument of the trigonometric function
            double evaluatedExpression = evalf(argValue);

            // Replace the numbers in the trigonometric expression with the remainder
            modifiedExpression = modifiedExpression.replaceFirst(Pattern.quote(matcher.group()), trigFunction + "(" + evaluatedExpression + ")");
        }

        return modifiedExpression;
    }

    public static double evalDefiniteIntegral(String integral, double lowerBound, double upperBound, String var) {

        double upperValue = evalAtPoint(integral, Collections.singletonList(var), Collections.singletonList(upperBound));
        double lowerValue = evalAtPoint(integral, Collections.singletonList(var), Collections.singletonList(lowerBound));
        return upperValue - lowerValue;

    }

    public static String evalDefiniteIntegralSymbolic(String integral, CharSequence var, double lowerBound, double upperBound) //working
    {
        String upperval = evalAtPointXSymbolic(integral, var, upperBound);
        String lowerval = evalAtPointXSymbolic(integral, var, lowerBound);

        String expr = "(" + upperval + ")-(" + lowerval + ")";

        System.out.println(eval("Simplify(" + expr + ")").toString());
        return eval("Simplify(" + expr + ")").toString();

        //String expanded = util.eval("Expand(" + expr + ")").toString();
        //return util.eval("Simplify(ReplaceAll(" + expanded + ", {log(x_)+log(y_) -> log(x*y), log(x_)-log(y_) -> log(x/y), log(x_^y_) -> y*log(x_)}))").toString();
    }

    public static double[] eigenVals(double[][] matrix) {
        int n = matrix.length;
        double[] eigenvalues = new double[n];
        ExprEvaluator util = new ExprEvaluator();
        StringBuilder matrixStr = new StringBuilder("{{");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrixStr.append(matrix[i][j]);
                if (j < n - 1) {
                    matrixStr.append(", ");
                }
            }
            if (i < n - 1) {
                matrixStr.append("}, {");
            }
        }
        matrixStr.append("}}");

        IExpr EigenVal = util.eval("Eigenvalues(" + matrixStr + ")");
        for (int i = 0; i < n; i++) {
            eigenvalues[i] = util.evalf(EigenVal.getAt(i + 1).toString());
        }

        return eigenvalues;
    }

    public static double[][] eigenVecs(double[][] matrix) {
        int n = matrix.length;
        double[][] eigenvectors = new double[n][n];
        ExprEvaluator util = new ExprEvaluator();
        StringBuilder matrixStr = new StringBuilder("{{");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrixStr.append(matrix[i][j]);
                if (j < n - 1) {
                    matrixStr.append(", ");
                }
            }
            if (i < n - 1) {
                matrixStr.append("}, {");
            }
        }
        matrixStr.append("}}");

        IExpr EigenVec = util.eval("Eigenvectors(" + matrixStr + ")");
        for (int i = 0; i < n; i++) {
            IExpr vec = EigenVec.getAt(i + 1);
            for (int j = 0; j < n; j++) {
                eigenvectors[i][j] = util.evalf(vec.getAt(j + 1).toString());
            }
        }

        return eigenvectors;
    }

    //normalized eigenvectors
    public static double[][] normalizedEigenVecs(double[][] matrix) {
        double[][] eigenvectors = eigenVecs(matrix);
        int n = eigenvectors.length;

        for (int i = 0; i < n; i++) {
            double norm = 0.0;
            for (int j = 0; j < n; j++) {
                norm += eigenvectors[i][j] * eigenvectors[i][j];
            }
            norm = Math.sqrt(norm);

            for (int j = 0; j < n; j++) {
                eigenvectors[i][j] /= norm;
            }
        }

        return eigenvectors;
    }


    public static double mod(double dividend, double divisor) {
        return dividend - divisor * Math.floor(dividend / divisor);
    }

    public static void dynamicEtLength(Editable s, EditText et) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        float minWidthDp = 60f;
        float minWidthPx = minWidthDp * density;

        // Measure the text width using the paint used by the EditText
        float textWidth = et.getPaint().measureText(s.toString());

        // Add some padding for cursor and spacing
        float totalWidth = textWidth + et.getPaddingLeft() + et.getPaddingRight() + 16 * density;

        // Limit it to a reasonable max (optional)
        float maxWidthPx = 300 * density;
        if (totalWidth < minWidthPx) totalWidth = minWidthPx;
        if (totalWidth > maxWidthPx) totalWidth = maxWidthPx;

        // Apply the new width
        ViewGroup.LayoutParams params = et.getLayoutParams();
        params.width = (int) totalWidth;
        et.setLayoutParams(params);
    }

    public static void setupEditTextChangeListener(View[] viewsToDisappear, Button btnCalc, CustomKeyboard custom_keyboard, Iterable<EditText> editTexts) {

        // Check if any of the EditTexts are empty
        boolean anyEditTextEmpty = false;
        for (EditText editText : editTexts) {
            if (editText.getText().toString().isEmpty()) {
                anyEditTextEmpty = true;
                break;
            }
        }

        hideKeyboardOnClick(btnCalc, custom_keyboard.getContext());
        // Enable or disable the btnCalc based on EditTexts' contents
        btnCalc.setEnabled(!anyEditTextEmpty);

        if (anyEditTextEmpty) {
            Paris.styleBuilder(btnCalc).add(R.style.custom_button_disabled).apply();
            btnCalc.setText("Please enter all the fields");
        } else {
            //use paris style builder to set the background
            Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();
            btnCalc.setText("Calculate");
        }

        for (EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    //check if the style of edittext = R.style.custom_edittext_mini
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if(editText.getExplicitStyle()==R.style.custom_edittext_mini){
                            dynamicEtLength(editable, editText);
                        }
                    }

                    for (View view : viewsToDisappear) {
                        view.setVisibility(View.GONE);
                    }

                    // Check if any of the EditTexts are empty
                    boolean anyEditTextEmpty = false;
                    for (EditText editText : editTexts) {
                        if (editText.getText().toString().isEmpty()) {
                            anyEditTextEmpty = true;
                            break;
                        }
                    }

                    //if all the edittexts have some text, activate the button
                    if (!anyEditTextEmpty) {
                        btnCalc.setEnabled(true);
                        Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();
                        btnCalc.setText("Calculate");
                    } else {
                        btnCalc.setEnabled(false);
                        Paris.styleBuilder(btnCalc).add(R.style.custom_button_disabled).apply();
                        btnCalc.setText("Please enter all the fields");
                    }

                    // Enable or disable the btnCalc based on EditTexts' contents
                    btnCalc.setEnabled(!anyEditTextEmpty);

                    if (anyEditTextEmpty) {
                        Paris.styleBuilder(btnCalc).add(R.style.custom_button_disabled).apply();
                        btnCalc.setText("Please enter all the fields");
                    } else {
                        //use paris style builder to set the background
                        Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();
                        btnCalc.setText("Calculate");
                    }

                }
            });
        }
    }

    public static void setupEditTextChangeListener(View[] viewsToDisappear, Button btnCalc, CustomKeyboard custom_keyboard, EditText... editTexts) {

        // Check if any of the EditTexts are empty
        boolean anyEditTextEmpty = false;
        for (EditText editText : editTexts) {
            if (editText.getText().toString().isEmpty()) {
                anyEditTextEmpty = true;
                break;
            }
        }

        hideKeyboardOnClick(btnCalc, custom_keyboard.getContext());
        // Enable or disable the btnCalc based on EditTexts' contents
        btnCalc.setEnabled(!anyEditTextEmpty);

        if (anyEditTextEmpty) {
            Paris.styleBuilder(btnCalc).add(R.style.custom_button_disabled).apply();
        } else {
            //use paris style builder to set the background
            Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();
        }

        for (EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (viewsToDisappear != null) {
                        for (View view : viewsToDisappear) {
                            view.setVisibility(View.GONE);
                        }
                    }

                    // Check if any of the EditTexts are empty
                    boolean anyEditTextEmpty = false;
                    for (EditText editText : editTexts) {
                        if (editText.getText().toString().isEmpty()) {
                            anyEditTextEmpty = true;
                            break;
                        }
                    }

                    //if all the edittexts have some text, activate the button
                    if (!anyEditTextEmpty) {
                        btnCalc.setEnabled(true);
                        Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();
                    } else {
                        btnCalc.setEnabled(false);
                        Paris.styleBuilder(btnCalc).add(R.style.custom_button_disabled).apply();
                    }

                    // Enable or disable the btnCalc based on EditTexts' contents
                    btnCalc.setEnabled(!anyEditTextEmpty);

                    if (anyEditTextEmpty) {
                        Paris.styleBuilder(btnCalc).add(R.style.custom_button_disabled).apply();
                    } else {
                        //use paris style builder to set the background
                        Paris.styleBuilder(btnCalc).add(R.style.custom_button_enabled).apply();
                    }

                }
            });
        }
    }

    //set up the edittext to use the custom keyboard
    public static void setupEditTextForCustomKeyboard(CustomKeyboard custom_keyboard1, ScrollView scrollView, EditText... editText) {

        for (EditText editText1 : editText) {
            editText1.setShowSoftInputOnFocus(false);
            custom_keyboard1.setScrollView(scrollView);
            editText1.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    custom_keyboard1.setTargetEditText(editText1);
                    custom_keyboard1.showKeyboard();
                    //smooth scroll to the editText if editText is not visible

                    custom_keyboard1.post(() -> {
                        if (!isFullyVisible(editText1)) {
                            System.out.println("not fully visible");
                            scrollView.smoothScrollTo(0, editText1.getTop());
                        }
                    });
                }
            });
        }
    }

    public static void setupEditTextForCustomKeyboard(CustomKeyboard custom_keyboard1, ScrollView scrollView, Iterable<EditText> editText) {

        for (EditText editText1 : editText) {
            editText1.setShowSoftInputOnFocus(false);
            custom_keyboard1.setScrollView(scrollView);
            editText1.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    custom_keyboard1.setTargetEditText(editText1);
                    custom_keyboard1.showKeyboard();
                    //smooth scroll to the editText if editText is not visible

                    custom_keyboard1.post(() -> {
                        if (!isFullyVisible(editText1)) {
                            System.out.println("not fully visible");
                            scrollView.smoothScrollTo(0, editText1.getTop());
                        }
                    });
                }
            });
        }
    }

    public static Boolean isFullyVisible(View view) {
        Rect rect = new Rect();
        return view.getGlobalVisibleRect(rect) && view.getHeight() == rect.height() && view.getWidth() == rect.width();
    }

    /**
     * Replaces constants in the input string with their corresponding values.
     *
     * @param input the input string to be processed
     * @return the processed string with constants replaced
     */
    public static String replaceConstants(String input) {
        // Replace "pi" with "Pi"
        input = input.replace("pi", String.valueOf(Math.PI));

        // Replace "e" with "E"
        input = input.replace("e", String.valueOf(Math.E));

        // Replace phi with Phi
        input = input.replace("phi", String.valueOf(1.61803398874989484820458683436563811772030917980576286213544862270526046281890));

        //replace log10 with ln equivalent
        input = input.replaceAll("log10\\((.*?)\\)", "ln($1)/ln(10)");

        return input;

    }

    /**
     * Removes null values from the given array and returns a new array
     * without the null values.
     *
     * @param array the array from which to remove null values
     * @return the new array without null values
     */
    public static String[] removeNullValues(String[] array) {
        List<String> resultList = new ArrayList<>();

        for (String element : array) {
            if (element != null) {
                resultList.add(element);
            }
        }

        String[] resultArray = new String[resultList.size()];
        return resultList.toArray(resultArray);
    }

    //method to find number of extremas of a polynomial

    /**
     * This method finds the number of extrema of a polynomial.
     *
     * @param function the polynomial function
     * @param var      the variable of the polynomial function
     * @return an array of strings representing the extremas as (x, y) pairs
     */
    public static String[] ExtremasFinder(String function, String var) {
        ExprEvaluator evaluator = new ExprEvaluator();
        try {
            IExpr inputExpr = evaluator.parse(function);
            IExpr x = evaluator.parse(var);

            IExpr firstDerivative = F.D(inputExpr, x);

            //create IAST equation for first derivative = 0
            IAST equation1 = F.Equal(firstDerivative, 0);

            IExpr[] Extremas = F.solve(equation1, S.x);

            //check if x is point of inflection
            IExpr secondDerivative = F.D(firstDerivative, x);
            IAST equation2 = F.Equal(secondDerivative, 0);
            IExpr[] InflectionPoints = F.solve(equation2, S.x);

            //remove inflection points from extremas
            for (IExpr inflectionPoint : InflectionPoints) {
                for (int j = 0; j < Extremas.length; j++) {
                    if (inflectionPoint.equals(Extremas[j])) {
                        Extremas[j] = null;
                    }
                }
            }

            //convert IExpr[] to String[]
            String[] ExtremasString = new String[Extremas.length];

            for (int i = 0; i < Extremas.length; i++) {
                assert Extremas[i] != null;
                ExtremasString[i] = Extremas[i].toString();
            }

            //remove null values from ExtremasString
            ExtremasString = removeNullValues(ExtremasString);

            if (ExtremasString.length == 0) {
                return null;
            } else {

                //get the x values of the extremas
                double[] ExtremasX = new double[ExtremasString.length];
                for (int i = 0; i < ExtremasString.length; i++) {
                    ExtremasX[i] = eval(ExtremasString[i]).evalDouble();
                }

                double[] ExtremasY = new double[ExtremasString.length];

                for (int i = 0; i < ExtremasX.length; i++) {
                    double ExtremaX = ExtremasX[i];
                    ExtremasY[i] = evalAtPointX(function, ExtremaX);
                }

                String[] pairs = new String[ExtremasX.length];

                for (int i = 0; i < ExtremasX.length; i++) {
                    pairs[i] = "(" + ExtremasX[i] + ", " + ExtremasY[i] + ")";
                }

                return pairs;
            }

        } catch (SyntaxError e) {
            // catch Symja parser errors here
        } catch (MathException me) {
            // catch Symja math errors here
        } catch (Exception ignored) {
        }
        return null;
    }

    public static int[] findNumberOfExtremas(String function, String var) {  //tested well
        ExprEvaluator evaluator = new ExprEvaluator();
        try {
            IExpr inputExpr = evaluator.parse(function);
            IExpr x = evaluator.parse(var);

            IExpr firstDerivative = F.D(inputExpr, x);
            IExpr secondDerivative = F.D(firstDerivative, x);

            String stringSecondDerivative = secondDerivative.toString();

            //create IAST equation for first derivative = 0
            IAST equation1 = F.Equal(firstDerivative, 0);

            IExpr[] Extremas = F.solve(equation1, S.x);
            //convert IExpr[] to String[]
            String[] ExtremasString = new String[Extremas.length];

            for (int i = 0; i < Extremas.length; i++) {
                if (Extremas[i] != null) {
                    ExtremasString[i] = Extremas[i].toString();
                }
            }

            //get the x values of the extremas
            double[] ExtremasX = new double[ExtremasString.length];
            for (int i = 0; i < ExtremasString.length; i++) {
                ExtremasX[i] = eval(ExtremasString[i]).evalDouble();
            }

            double[] SecDerY = new double[ExtremasString.length];

            for (int i = 0; i < ExtremasX.length; i++) {
                double ExtremaX = ExtremasX[i];
                SecDerY[i] = evalAtPointX(stringSecondDerivative, ExtremaX);
            }

            int[] numOfExtremas = new int[ExtremasString.length];
            int Maximas = 0;
            int Minimas = 0;
            for (int i = 0; i < ExtremasString.length; i++) {
                if (evalf(String.valueOf(eval(String.valueOf(SecDerY[i])))) > 0) {
                    Minimas++;
                } else if (evalf(String.valueOf(eval(String.valueOf(SecDerY[i])))) < 0) {
                    Maximas++;
                }
            }
            numOfExtremas[0] = Maximas;
            numOfExtremas[1] = Minimas;

            return numOfExtremas;


        } catch (SyntaxError e) {
            // catch Symja parser errors here
        } catch (MathException me) {
            // catch Symja math errors here
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Evaluates a mathematical expression and returns the result as a double.
     * Replaces any instances of 'x' in the expression with the given value.
     *
     * @param str the mathematical expression to be evaluated
     * @param val the value to replace 'x' with
     * @return the result of the evaluation as a double
     */

    public static double evalAtPointX(@NotNull final String str, Double val) {
        ExprEvaluator util = new ExprEvaluator(false, (short) 2);
        try {
            IExpr variableSetter = util.eval("x=" + val);
            return util.evalf(str);
        } catch (SyntaxError e) {
            // catch Symja parser errors here
            String limVal = String.valueOf(util.evalf("Limit(" + str + ", x -> " + val + ")"));
            if (limVal.equals("Infinity") || limVal.equals("-Infinity") || limVal.equals("Indeterminate")) {
                return 12.342;
            }
            return evalf(limVal);
        } catch (MathException me) {
            // catch Symja math errors here
            String limVal = String.valueOf(util.evalf("Limit(" + str + ", x -> " + val + ")"));
            if (limVal.equals("Infinity") || limVal.equals("-Infinity") || limVal.equals("Indeterminate")) {
                return 12.342;
            }
            return evalf(limVal);
        } catch (Exception e) {
            // catch other exceptions here
            String limVal = String.valueOf(util.evalf("Limit(" + str + ", x -> " + val + ")"));
            if (limVal.equals("Infinity") || limVal.equals("-Infinity") || limVal.equals("Indeterminate")) {
                return 12.342;
            }
            return evalf(limVal);
        }
    }

    public static double evalAtPoint(@NotNull final String str, List<String> vars, List<Double> vals) {
        ExprEvaluator util = new ExprEvaluator(false, (short) 200000);
        if (vals.size() != vars.size()) {
            throw new RuntimeException();
        }
        try {
            for (int i = 0; i < vals.size(); i++) {
                util.eval(vars.get(i) + "=" + vals.get(i));
            }
            return util.evalf(str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String evalAtPointXSymbolic(@NotNull final String str, CharSequence variable, Double val) {
        //replace x with val. If val is in decimal, convert it to fraction
        //String frac = convertDecimalToFraction(val);
        String str1 = str.replace(variable, val.toString());

        return (ExprSimplifier(str1)).toLowerCase();
    }

    public static String replaceFractionalValues(CharSequence expression) {
        Pattern pattern = Pattern.compile("\\d+/\\d+");
        Matcher matcher = pattern.matcher(expression);

        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String fraction = matcher.group();
            String[] parts = fraction.split("/");
            double numerator = evalf(parts[0]);
            double denominator = evalf(parts[1]);
            double decimalValue = numerator / denominator;
            String replacement = Double.toString(decimalValue);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }


    //method to convert decimal to fraction
    public static String convertDecimalToFraction(double decimal) {
        int precision = getNumberOfFractionDigitsSym(decimal);
        int denominator = (int) Math.pow(10, precision);
        int numerator = (int) (decimal * denominator);
        int gcd = getGCD(numerator, denominator);
        return (numerator / gcd) + "/" + (denominator / gcd);
    }

    public static int getNumberOfFractionDigitsSym(double number) {
        String numberString = Double.toString(Math.abs(number));
        int indexOfDecimal = numberString.indexOf(".");

        if (indexOfDecimal >= 0 && indexOfDecimal < numberString.length() - 1) {
            return numberString.length() - indexOfDecimal - 1;
        } else {
            return 0;
        }
    }

    public static String reduceLogTerms(CharSequence expression) {
        // Regular expression pattern to match log terms
        String pattern = "log\\(([^)]+)\\)";

        // Create a pattern object
        Pattern regex = Pattern.compile(pattern);

        // Create a matcher object with the input expression
        Matcher matcher = regex.matcher(expression);

        StringBuffer output = new StringBuffer();

        // Iterate through the matches
        while (matcher.find()) {
            // Get the log term
            String logTerm = matcher.group(1);

            // Split the log term by multiplication
            assert logTerm != null;
            String[] terms = logTerm.split("\\*");

            // Simplify the log terms
            double result = 0;
            for (String term : terms) {
                result += Math.log(evalf(term));
            }

            // Replace the log term with the simplified version in the output
            String replacement = "log(" + result + ")";
            matcher.appendReplacement(output, replacement);
        }

        // Append the remaining part of the input to the output
        matcher.appendTail(output);

        return output.toString();
    }

    public static String simplifyFractionsInsideLog(CharSequence input) {
        // Regular expression pattern to match the fraction inside log
        String pattern = "log\\((\\d+)/(\\d+)\\)";

        // Create a pattern object
        Pattern regex = Pattern.compile(pattern);

        // Create a matcher object with the input string
        Matcher matcher = regex.matcher(input);

        StringBuffer output = new StringBuffer();

        // Iterate through the matches
        while (matcher.find()) {
            // Get the numerator and denominator
            int numerator = Integer.parseInt(Objects.requireNonNull(matcher.group(1)));
            int denominator = Integer.parseInt(Objects.requireNonNull(matcher.group(2)));

            // Simplify the fraction
            int gcd = getGCD(numerator, denominator);
            int simplifiedNumerator = numerator / gcd;
            int simplifiedDenominator = denominator / gcd;

            // Replace the fraction with the simplified version in the output
            String replacement = "log(" + simplifiedNumerator + "/" + simplifiedDenominator + ")";
            matcher.appendReplacement(output, replacement);
        }

        // Append the remaining part of the input to the output
        matcher.appendTail(output);

        return output.toString();
    }

    public static int getGCD(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return getGCD(b, a % b);
        }
    }

    @SafeVarargs
    public static void addArrayListEntries(String input, String var, float xmin, float xmax, ArrayList<Entry>... inputLineArrays) {

        input = input.replace(var, "x");

        for (ArrayList<Entry> inputLineArray : inputLineArrays) {

            int numThreads = Runtime.getRuntime().availableProcessors();
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            Entry entry1 = new Entry(0f, (float) evalAtPointX(input, 0.0));

            for (float lineChartX = xmin; lineChartX < xmax; lineChartX = lineChartX + ((xmax - xmin) / 100)) {
                float finalLineChartX = lineChartX;
                String finalInput = input;
                executor.submit(() -> {
                    Entry entry = new Entry(finalLineChartX, (float) evalAtPointX(finalInput, (double) finalLineChartX));
                    synchronized (inputLineArray) {
                        //add entry to the array only if y is not equal to 12.342 +- 0.01 or if x != 0
                        if (Math.abs(entry.getY() - 12.342) > 0.01 || Math.abs(entry.getX()) > 0.009) {
                            inputLineArray.add(entry);
                        }

                        //remove all entires which |x| < 0.00001
                        inputLineArray.removeIf(entry2 -> Math.abs(entry2.getX()) < 0.00001);

                        //remove all entries which |y - 12.342| < 0.0001
                        inputLineArray.removeIf(entry2 -> Math.abs(entry2.getY() - 12.342) < 0.0001);

                        inputLineArray.add(entry1);

                        //inputLineArray.add(entry1);
                        inputLineArray.sort((e1, e2) -> Float.compare(e1.getX(), e2.getX()));

                    }
                });
            }

            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException ignored) {
            }
        }

    }

    public static String calculateNthDerivative(String function, String var, int n) {
        ExprEvaluator evaluator = new ExprEvaluator();
        try {
            IExpr inputExpr = evaluator.parse(function);
            IExpr x = evaluator.parse(var);

            for (int i = 0; i < n; i++) {
                inputExpr = F.D(inputExpr, x);
            }

            IExpr result = evaluator.eval(inputExpr);
            return result.toString();
        } catch (SyntaxError e) {
            // catch Symja parser errors here
        } catch (MathException me) {
            // catch Symja math errors here
        } catch (Exception ignored) {
        }
        return StringError;
    }

    public static String calculateNthIntegral(String function, String var, int n) {
        ExprEvaluator evaluator = new ExprEvaluator();
        try {
            IExpr inputExpr = evaluator.parse(function);
            IExpr x = evaluator.parse(var);

            for (int i = 0; i < n; i++) {
                inputExpr = F.Integrate(inputExpr, x);
            }

            IExpr result = evaluator.eval(inputExpr);
            return result.toString();
        } catch (SyntaxError e) {
            // catch Symja parser errors here
        } catch (MathException me) {
            // catch Symja math errors here
        } catch (Exception ignored) {
        }
        return StringError;
    }

    public static String ExprSimplifier(String function) {
        ExprEvaluator evaluator = new ExprEvaluator();
        IExpr result = evaluator.eval("Simplify(" + function + ")");
        return result.toString();
    }

    public static String Orthogonalize(String vector) {
        ExprEvaluator evaluator = new ExprEvaluator();
        IExpr result = evaluator.eval("Orthogonalize(" + vector + ")");
        return result.toString();
    }

    public static IExpr IExprConverter(String function) {
        ExprEvaluator evaluator = new ExprEvaluator();
        return evaluator.parse(function);

    }

    //method to calculate the nth integral of a function


    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
//http://stackoverflow.com/questions/4605527/converting-pixels-to-dp
//The above method results accurate method compared to below methods
//http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android


    public static int convertDpToPx(int dp) {
        return Math.round(dp * (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));

    }

    public static int convertPxToDp(int px) {
        return Math.round(px / (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String getTex(String function, float fontSize, MTMathView mathView, Boolean shouldConvertToTex) {
        try {
            // false -> switch to Mathematica syntax mode:
            EvalEngine engine = new EvalEngine(false);
            //
            TeXUtilities texUtil = new TeXUtilities(engine, false);

            StringWriter stw = new StringWriter();
            String stw_string = function;
            stw_string = stw_string.replace("Epsilon", "\\epsilon");
            stw_string = stw_string.replace("Alpha", "\\alpha");
            stw_string = stw_string.replace("Beta", "\\beta");
            stw_string = stw_string.replace("Gamma", "\\gamma");
            stw_string = stw_string.replace("Delta", "\\delta");
            stw_string = stw_string.replace("Theta", "\\theta");
            stw_string = stw_string.replace("Lambda", "\\lambda");
            stw_string = stw_string.replace("Xi", "\\xi");
            stw_string = stw_string.replace("Pi", "\\pi");
            stw_string = stw_string.replace("Sigma", "\\sigma");
            stw_string = stw_string.replace("Phi", "\\phi");
            stw_string = stw_string.replace("Psi", "\\psi");
            stw_string = stw_string.replace("Omega", "\\omega");
            stw_string = stw_string.replace("kappa", "\\kappa");

            if (shouldConvertToTex) {
                texUtil.toTeX(IExprConverter(function), stw);
                stw_string = stw.toString();
                stw_string = stw_string.replaceAll("([a-zA-Z])(\\d+)", "$1_{$2}");
            }

            mathView.setFontSize(fontSize);
            return "\\color{#D0D0D0}{" + stw_string + "}";

        } catch (SyntaxError e) {
            // catch Symja parser errors here
        } catch (MathException me) {
            // catch Symja math errors here
        } catch (Exception ignored) {
        }

        return StringError;
    }

    /**
     * Retrieves the LaTeX representation of a mathematical function.
     *
     * @param function the mathematical function to convert to LaTeX
     * @return the LaTeX representation of the function
     */
    public static String getTex(String function, MTMathView mathView, Boolean shouldConvertToTex) {
        try {
            // false -> switch to Mathematica syntax mode:
            EvalEngine engine = new EvalEngine(false);
            TeXUtilities texUtil = new TeXUtilities(engine, false);

            StringWriter stw = new StringWriter();
            String stw_string = function;
            stw_string = stw_string.replace("Epsilon", "\\epsilon");
            stw_string = stw_string.replace("Alpha", "\\alpha");
            stw_string = stw_string.replace("Beta", "\\beta");
            stw_string = stw_string.replace("Gamma", "\\gamma");
            stw_string = stw_string.replace("Delta", "\\delta");
            stw_string = stw_string.replace("Theta", "\\theta");
            stw_string = stw_string.replace("Lambda", "\\lambda");
            stw_string = stw_string.replace("Xi", "\\xi");
            stw_string = stw_string.replace("Pi", "\\pi");
            stw_string = stw_string.replace("Sigma", "\\sigma");
            stw_string = stw_string.replace("Phi", "\\phi");
            stw_string = stw_string.replace("Psi", "\\psi");
            stw_string = stw_string.replace("Omega", "\\omega");
            stw_string = stw_string.replace("kappa", "\\kappa");
            if (shouldConvertToTex) {
                texUtil.toTeX(IExprConverter(function), stw);
                stw_string = stw.toString();
                stw_string = stw_string.replaceAll("([a-zA-Z])(\\d+)", "$1_{$2}");
            }

            mathView.setFontSize(60f);
            return "\\color{#D0D0D0}{" + stw_string + "}";

        } catch (SyntaxError e) {
            // catch Symja parser errors here
        } catch (MathException me) {
            // catch Symja math errors here
        } catch (Exception ignored) {
        }

        return StringError;
    }

    public static String getTex(String function, MTMathView mathView) {
        try {
            // false -> switch to Mathematica syntax mode:
            EvalEngine engine = new EvalEngine(false);
            TeXUtilities texUtil = new TeXUtilities(engine, false);

            StringWriter stw = new StringWriter();
            String stw_string = function;
            stw_string = stw_string.replace("Epsilon", "\\epsilon");
            stw_string = stw_string.replace("Alpha", "\\alpha");
            stw_string = stw_string.replace("Beta", "\\beta");
            stw_string = stw_string.replace("Gamma", "\\gamma");
            stw_string = stw_string.replace("Delta", "\\delta");
            stw_string = stw_string.replace("Theta", "\\theta");
            stw_string = stw_string.replace("Lambda", "\\lambda");
            stw_string = stw_string.replace("Xi", "\\xi");
            stw_string = stw_string.replace("Pi", "\\pi");
            stw_string = stw_string.replace("Sigma", "\\sigma");
            stw_string = stw_string.replace("Phi", "\\phi");
            stw_string = stw_string.replace("Psi", "\\psi");
            stw_string = stw_string.replace("Omega", "\\omega");
            stw_string = stw_string.replace("kappa", "\\kappa");
            texUtil.toTeX(IExprConverter(function), stw);
            stw_string = stw.toString();
            stw_string = stw_string.replaceAll("([a-zA-Z])(\\d+)", "$1_{$2}");

            mathView.setFontSize(60f);
            return "\\color{#D0D0D0}{" + stw_string + "}";

        } catch (SyntaxError e) {
            // catch Symja parser errors here
        } catch (MathException me) {
            // catch Symja math errors here
        } catch (Exception ignored) {
        }

        return StringError;
    }

    public static String getTex(String function, Boolean shouldConvertToTex) {
        try {
            // false -> switch to Mathematica syntax mode:
            EvalEngine engine = new EvalEngine(false);
            //
            TeXUtilities texUtil = new TeXUtilities(engine, false);

            StringWriter stw = new StringWriter();
            String stw_string = function;
            stw_string = stw_string.replace("Epsilon", "\\epsilon");
            stw_string = stw_string.replace("Alpha", "\\alpha");
            stw_string = stw_string.replace("Beta", "\\beta");
            stw_string = stw_string.replace("Gamma", "\\gamma");
            stw_string = stw_string.replace("Delta", "\\delta");
            stw_string = stw_string.replace("Theta", "\\theta");
            stw_string = stw_string.replace("Lambda", "\\lambda");
            stw_string = stw_string.replace("Xi", "\\xi");
            stw_string = stw_string.replace("Pi", "\\pi");
            stw_string = stw_string.replace("Sigma", "\\sigma");
            stw_string = stw_string.replace("Phi", "\\phi");
            stw_string = stw_string.replace("Psi", "\\psi");
            stw_string = stw_string.replace("Omega", "\\omega");
            stw_string = stw_string.replace("kappa", "\\kappa");

            if (shouldConvertToTex) {
                texUtil.toTeX(IExprConverter(function), stw);
                stw_string = stw.toString();
                stw_string = stw_string.replaceAll("([a-zA-Z])(\\d+)", "$1_{$2}");
            }

            return "\\color{#D0D0D0}{" + stw_string + "}";

        } catch (SyntaxError e) {
            // catch Symja parser errors here
        } catch (MathException me) {
            // catch Symja math errors here
        } catch (Exception ignored) {
        }

        return StringError;
    }

    /*public static void MathJaxConfig(MathView... mathViews) {

        for (MathView mathView : mathViews) {
            mathView.config("MathJax.Hub.Config({\n" + "  \"HTML-CSS\" : {\n" + "        webFont : \"Latin-Modern\",\n" + "        linebreaks: { automatic: true },\n" + "    }" + "});");
        }
    }*/

    @SuppressLint("SetJavaScriptEnabled")
    public static void WebViewLoader(@NotNull WebView webView, String url) {
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    public static double DoubleNanChecker(double ACangle) {
        if (Double.isNaN(ACangle)) {
            return 0.0;
        }
        return ACangle;
    }

    @SafeVarargs
    public static void mChartDataSetter(@NotNull LineChart mChart, @NotNull Context context, Boolean ShouldBeFilled, @NotNull ArrayList<Entry>... yValues) {

        for (ArrayList<Entry> yVals : yValues) {
            LineDataSet set1 = new LineDataSet(yVals, "");

            set1.setFillAlpha(110);
            set1.setDrawCircles(false);
            set1.setLineWidth(2.f);
            set1.setColor(Color.WHITE);
            set1.setValueTextColor(Color.WHITE);
            set1.setValueTextSize(15f);

            set1.setDrawFilled(ShouldBeFilled);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_blue);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.rgb(30, 30, 30));
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);
            mChart.setData(data);
            mChart.invalidate();
        }

        mChart.setVisibility(View.VISIBLE);
    }

    /*@NotNull
    public static String rounded_StringNLOG(double t) {
        if (getNumberOfFractionDigits(t) <= 4) {
            return String.valueOf(t);
        } else {
            DecimalFormat df = new DecimalFormat("###.#########E0");
            df.setRoundingMode(RoundingMode.CEILING);
            return (t == (int) t ? String.valueOf((int) t) : df.format(t));
        }

    }*/

    public static void TextChangeListeners(@Nullable TextView tvresult, @NotNull EditText... editTexts) {

        for (EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    assert tvresult != null;
                    tvresult.setVisibility(View.GONE);
                    tvresult.setText(null);
                }
            });
        }
    }

    //eval function
        /* public static double eval(final String str)
        {
            return new Object()
            {
                int pos = -1, ch;

                void nextChar()
                {
                    ch = (++pos < str.length()) ? str.charAt(pos) : -1;
                }

                boolean eat(int charToEat)
                {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat)
                    {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                double parse()
                {
                    nextChar();
                    double x = parseExpression();
                    if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                    return x;
                }

                // Grammar:
                // expression = term | expression `+` term | expression `-` term
                // term = factor | term `*` factor | term `/` factor
                // factor = `+` factor | `-` factor | `(` expression `)`
                //        | number | functionName factor | factor `^` factor

                double parseExpression()
                {
                    double x = parseTerm();
                    for (; ; )
                    {
                        if (eat('+')) x += parseTerm(); // addition
                        else if (eat('-')) x -= parseTerm(); // subtraction
                        else return x;
                    }
                }

                double parseTerm()
                {
                    double x = parseFactor();
                    for (; ; )
                    {
                        if (eat('*')) x *= parseFactor(); // multiplication
                        else if (eat('/')) x /= parseFactor(); // division
                        else if (eat('%')) x %= parseFactor(); // remainder
                        else return x;
                    }
                }

                double parseFactor()
                {
                    if (eat('+')) return parseFactor(); // unary plus
                    if (eat('-')) return -parseFactor(); // unary minus

                    double x;
                    int startPos = this.pos;
                    if (eat('('))
                    { // parentheses
                        x = parseExpression();
                        eat(')');
                    } else if ((ch >= '0' && ch <= '9') || ch == '.')
                    { // numbers
                        while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                        x = evalf(str.substring(startPos, this.pos));
                    } else if (ch >= 'a' && ch <= 'z')
                    { // functions
                        while (ch >= 'a' && ch <= 'z') nextChar();
                        String func = str.substring(startPos, this.pos);
                        x = parseFactor();
                        switch (func)
                        {
                            case "sqrt":
                                x = Math.sqrt(x);
                                break;
                            case "sin":
                                x = Math.sin(Math.toRadians(x));
                                break;
                            case "cos":
                                x = Math.cos(Math.toRadians(x));
                                break;
                            case "tan":
                                x = Math.tan(Math.toRadians(x));
                                break;
                            case "log":
                                x = Math.log10(x);
                                break;
                            case "ln":
                                x = Math.log(x);
                                break;
                            default:
                                throw new RuntimeException("Unknown function: " + func);
                        }
                    } else
                    {
                        throw new RuntimeException("Unexpected: " + (char) ch);
                    }
                    return x;
                }
            }.parse();
        } */

    public static void et_Scroller(@NotNull EditText etAi, Context context) {
        etAi.setScroller(new Scroller(context));
        etAi.setMaxLines(1);
        etAi.setHorizontalScrollBarEnabled(true);
        etAi.setHorizontallyScrolling(true);
        etAi.setMovementMethod(new ScrollingMovementMethod());

    }

    /**
     * Increments the given integer array if the checkbox is checked and the first element of the array is less than the specified maximum value.
     *
     * @param CB_acc          the checkbox to check
     * @param a               the integer array to increment
     * @param max_CB_Required the maximum value the first element can reach
     */
    public static void CB_Incrementer_Till_Max(@NotNull Checkable CB_acc, int[] a, int max_CB_Required) {
        if (CB_acc.isChecked() && a[0] < max_CB_Required) {
            a[0]++;
        }
    }

    public static void CB_LessThanMaxThen(@NotNull Button btn_next, @NotNull Button btn_clear, int @NotNull [] a, int max_CB_Required, @NotNull CheckBox... checkBoxes) {
        if (a[0] < max_CB_Required) {
            for (CheckBox CB : checkBoxes) {
                CB.setClickable(true);
            }
            btn_next.setClickable(false);
            btn_next.setEnabled(false);
            Paris.styleBuilder(btn_next).add(R.style.custom_button_disabled).apply();
            btn_clear.setVisibility(View.VISIBLE);
        }
    }

    public static void CB_disable_if3(@NotNull CheckBox CB_acc) {
        if (!CB_acc.isChecked()) {
            CB_acc.setClickable(false);
        }
    }

    /**
     * Generates the function comment for the given function body.
     *
     * @param btnNext         the next button
     * @param btnClear        the clear button
     * @param a               the array of integers
     * @param max_CB_Required the maximum number of checkboxes required
     * @param checkBoxes      the checkboxes
     */
    public static void CB_MainMethod(@NotNull Button btnNext, @NotNull Button btnClear, int @NotNull [] a, int max_CB_Required, Iterable<CheckBox> checkBoxes) {

        for (CheckBox CB : checkBoxes) {
            CB_Incrementer_Till_Max(CB, a, max_CB_Required);
        }

        if (a[0] == max_CB_Required) {
            btnClear.setVisibility(View.VISIBLE);
            btnNext.setClickable(true);
            btnNext.setEnabled(true);
            Paris.styleBuilder(btnNext).add(R.style.custom_button_enabled).apply();

            for (CheckBox CB : checkBoxes) {
                CB_disable_if3(CB);
            }
        }

        if (a[0] == 0) {
            btnClear.setVisibility(View.GONE);
        }

        for (CheckBox CB : checkBoxes) {
            CB_LessThanMaxThen(btnNext, btnClear, a, max_CB_Required, CB);
        }

        a[0] = 0;
    }

    public static void CB_MainMethod(@NotNull Button btnNext, @NotNull Button btnClear, int @NotNull [] a, int max_CB_Required, Iterable<CheckBox> checkBoxes, View... views) {

        for (CheckBox CB : checkBoxes) {
            CB_Incrementer_Till_Max(CB, a, max_CB_Required);
        }

        if (a[0] == max_CB_Required) {
            btnClear.setVisibility(View.VISIBLE);
            btnNext.setClickable(true);
            btnNext.setEnabled(true);
            Paris.styleBuilder(btnNext).add(R.style.custom_button_enabled).apply();

            for (CheckBox CB : checkBoxes) {
                CB_disable_if3(CB);
            }
        }

        if (a[0] == 0) {
            btnClear.setVisibility(View.GONE);
        }

        for (CheckBox CB : checkBoxes) {
            CB_LessThanMaxThen(btnNext, btnClear, a, max_CB_Required, CB);
        }

        a[0] = 0;


        //if any of the checkbox is checked to unchecked, or vice versa, clear the textviews
        for (CheckBox CB : checkBoxes) {
            CB.setOnCheckedChangeListener((buttonView, isChecked) -> {
                for (View view : views) {
                    view.setVisibility(View.GONE);
                }
            });
        }

    }

    @NotNull
    public static ArrayList<Entry> mChartProperties(@NotNull LineChart mChart) {
        mChart.setHardwareAccelerationEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.getAxisRight().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(15f);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTextSize(15f);

        mChart.setHighlightPerDragEnabled(false);
        mChart.setHighlightPerTapEnabled(false);
        mChart.setGridBackgroundColor(Color.WHITE);

        return new ArrayList<>();
    }
}
