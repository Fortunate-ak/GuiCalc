package com.example.guicalc;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity.java
 * ------------------
 * Demonstrates:
 *   [1] GUI Components  — TextView (display), Buttons (digit, operator, action)
 *   [2] Fonts & Colors  — Custom typeface, programmatic color application
 *   [3] Layout Managers — ConstraintLayout + GridLayout (see activity_main.xml)
 *   [4] Event Listeners — View.OnClickListener for every button
 *   [5] Native Calculator — delegates to Calculator.java
 *
 * Lab 1 - MAD (IIT2201/ICS2201/ISE2201)
 * Harare Institute of Technology
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // -------------------------------------------------------
    // UI References
    // -------------------------------------------------------
    private TextView tvExpression;   // shows the running expression (e.g. "12 + 5")
    private TextView tvDisplay;      // main large number display
    private TextView tvOperator;     // small indicator showing the active operator

    // -------------------------------------------------------
    // Business Logic
    // -------------------------------------------------------
    private Calculator calculator;

    // -------------------------------------------------------
    // Colors (defined here to demonstrate programmatic color use)
    // -------------------------------------------------------
    private static final int COLOR_DISPLAY_BG   = Color.parseColor("#1A1A2E");
    private static final int COLOR_DIGIT_BG     = Color.parseColor("#16213E");
    private static final int COLOR_DIGIT_TEXT   = Color.parseColor("#E0E0E0");
    private static final int COLOR_OPERATOR_BG  = Color.parseColor("#0F3460");
    private static final int COLOR_OPERATOR_TEXT= Color.parseColor("#E94560");
    private static final int COLOR_ACTION_BG    = Color.parseColor("#533483");
    private static final int COLOR_ACTION_TEXT  = Color.WHITE;
    private static final int COLOR_EQUALS_BG    = Color.parseColor("#E94560");
    private static final int COLOR_EQUALS_TEXT  = Color.WHITE;
    private static final int COLOR_DISPLAY_TEXT = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise the calculator engine
        calculator = new Calculator();

        // Wire up all views
        bindViews();

        // Apply custom font & colors programmatically
        //   (This demonstrates "Font and Colors" from Lab 1)
        applyFontsAndColors();

        // Register click listeners on every button
        //   (This demonstrates "event listeners" from Lab 1)
        registerListeners();
    }

    // -------------------------------------------------------
    // [GUI Components] — bind view references
    // -------------------------------------------------------
    private void bindViews() {
        tvExpression = findViewById(R.id.tv_expression);
        tvDisplay    = findViewById(R.id.tv_display);
        tvOperator   = findViewById(R.id.tv_operator);
    }

    // -------------------------------------------------------
    // [Fonts & Colors] — apply programmatically
    // -------------------------------------------------------
    private void applyFontsAndColors() {
        // Display area background color
        findViewById(R.id.layout_display).setBackgroundColor(COLOR_DISPLAY_BG);

        // Display text — large, bold, white
        tvDisplay.setTextColor(COLOR_DISPLAY_TEXT);
        tvDisplay.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        tvDisplay.setTextSize(52f);

        // Expression text — smaller, muted
        tvExpression.setTextColor(Color.parseColor("#9E9E9E"));
        tvExpression.setTextSize(16f);

        // Operator indicator
        tvOperator.setTextColor(COLOR_OPERATOR_TEXT);
        tvOperator.setTextSize(20f);

        // Style digit buttons
        int[] digitIds = {
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
            R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
            R.id.btn_8, R.id.btn_9, R.id.btn_dot
        };
        for (int id : digitIds) {
            styleButton(id, COLOR_DIGIT_BG, COLOR_DIGIT_TEXT, 22f, Typeface.NORMAL);
        }

        // Style operator buttons
        int[] operatorIds = {
            R.id.btn_add, R.id.btn_sub, R.id.btn_mul, R.id.btn_div
        };
        for (int id : operatorIds) {
            styleButton(id, COLOR_OPERATOR_BG, COLOR_OPERATOR_TEXT, 26f, Typeface.BOLD);
        }

        // Style action buttons (AC, +/-, %)
        int[] actionIds = {
            R.id.btn_clear, R.id.btn_sign, R.id.btn_percent, R.id.btn_del
        };
        for (int id : actionIds) {
            styleButton(id, COLOR_ACTION_BG, COLOR_ACTION_TEXT, 18f, Typeface.NORMAL);
        }

        // Equals button — accent color
        styleButton(R.id.btn_equals, COLOR_EQUALS_BG, COLOR_EQUALS_TEXT, 28f, Typeface.BOLD);
    }

    /**
     * Helper: applies background color, text color, text size, and typeface to a button.
     * This keeps "font and color" logic DRY and reusable.
     */
    private void styleButton(int viewId, int bgColor, int textColor, float textSizeSp, int typefaceStyle) {
        Button btn = findViewById(viewId);
        if (btn == null) return;
        btn.setBackgroundColor(bgColor);
        btn.setTextColor(textColor);
        btn.setTextSize(textSizeSp);
        btn.setTypeface(Typeface.create("sans-serif", typefaceStyle));
    }

    // -------------------------------------------------------
    // [Event Listeners] — register OnClickListener on every button
    // -------------------------------------------------------
    private void registerListeners() {
        int[] allButtons = {
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
            R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
            R.id.btn_8, R.id.btn_9, R.id.btn_dot,
            R.id.btn_add, R.id.btn_sub, R.id.btn_mul, R.id.btn_div,
            R.id.btn_equals, R.id.btn_clear, R.id.btn_sign,
            R.id.btn_percent, R.id.btn_del
        };
        for (int id : allButtons) {
            View v = findViewById(id);
            if (v != null) v.setOnClickListener(this);
        }
    }

    // -------------------------------------------------------
    // [Event Listeners] — handle all button clicks in one place
    // -------------------------------------------------------
    @Override
    public void onClick(View view) {
        int id = view.getId();
        String result;

        if (id == R.id.btn_0)       { result = calculator.inputDigit("0"); }
        else if (id == R.id.btn_1)  { result = calculator.inputDigit("1"); }
        else if (id == R.id.btn_2)  { result = calculator.inputDigit("2"); }
        else if (id == R.id.btn_3)  { result = calculator.inputDigit("3"); }
        else if (id == R.id.btn_4)  { result = calculator.inputDigit("4"); }
        else if (id == R.id.btn_5)  { result = calculator.inputDigit("5"); }
        else if (id == R.id.btn_6)  { result = calculator.inputDigit("6"); }
        else if (id == R.id.btn_7)  { result = calculator.inputDigit("7"); }
        else if (id == R.id.btn_8)  { result = calculator.inputDigit("8"); }
        else if (id == R.id.btn_9)  { result = calculator.inputDigit("9"); }
        else if (id == R.id.btn_dot){ result = calculator.inputDigit("."); }

        else if (id == R.id.btn_add){ result = calculator.inputOperator("+"); updateOperatorIndicator("+"); }
        else if (id == R.id.btn_sub){ result = calculator.inputOperator("-"); updateOperatorIndicator("−"); }
        else if (id == R.id.btn_mul){ result = calculator.inputOperator("×"); updateOperatorIndicator("×"); }
        else if (id == R.id.btn_div){ result = calculator.inputOperator("÷"); updateOperatorIndicator("÷"); }

        else if (id == R.id.btn_equals)  { result = computeAndClearOperator(); }
        else if (id == R.id.btn_clear)   { result = calculator.clear(); clearOperatorIndicator(); }
        else if (id == R.id.btn_sign)    { result = calculator.toggleSign(); }
        else if (id == R.id.btn_percent) { result = calculator.percentage(); }
        else if (id == R.id.btn_del)     { result = calculator.deleteLast(); }
        else return;

        updateDisplay(result);
    }

    // -------------------------------------------------------
    // Display update helpers
    // -------------------------------------------------------

    private void updateDisplay(String value) {
        tvDisplay.setText(value);
        // Auto-shrink text if the number is long
        if (value.length() > 10)      tvDisplay.setTextSize(28f);
        else if (value.length() > 7)  tvDisplay.setTextSize(38f);
        else                          tvDisplay.setTextSize(52f);
    }

    private void updateOperatorIndicator(String op) {
        tvOperator.setText(op);
        // Build expression hint: e.g. "12 +"
        tvExpression.setText(calculator.getDisplayValue() + "  " + op);
    }

    private void clearOperatorIndicator() {
        tvOperator.setText("");
        tvExpression.setText("");
    }

    private String computeAndClearOperator() {
        String result = calculator.computeResult();
        tvExpression.setText("");
        tvOperator.setText("");
        return result;
    }
}
