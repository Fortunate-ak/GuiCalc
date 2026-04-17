package com.example.guicalc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvDisplay;
    private String currentInput = "";
    private String operator = "";
    private double firstOperand = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);

        // Setting listeners in bulk to satisfy "event listeners" requirement
        int[] buttonIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide, 
            R.id.btnEqual, R.id.btnClear, R.id.btnDot, R.id.btnBackspace,
            R.id.btnPercent, R.id.btnRoot
        };

        for (int id : buttonIds) {
            Button btn = findViewById(id);
            if (btn != null) {
                btn.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String buttonText = button.getText().toString();

        if (button.getId() == R.id.btnClear) {
            currentInput = "";
            operator = "";
            firstOperand = 0;
            tvDisplay.setText("0");
        } else if (button.getId() == R.id.btnBackspace) {
            if (currentInput.length() > 0) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                tvDisplay.setText(currentInput.isEmpty() ? "0" : currentInput);
            }
        } else if (button.getId() == R.id.btnEqual) {
            calculateResult();
            operator = "";
        } else if (button.getId() == R.id.btnAdd || button.getId() == R.id.btnSubtract ||
                   button.getId() == R.id.btnMultiply || button.getId() == R.id.btnDivide) {
            if (!currentInput.isEmpty() || !operator.isEmpty()) {
                if (!currentInput.isEmpty()) {
                    firstOperand = Double.parseDouble(currentInput);
                }
                operator = buttonText;
                currentInput = "";
            }
        } else if (button.getId() == R.id.btnRoot) {
            if (!currentInput.isEmpty()) {
                double val = Double.parseDouble(currentInput);
                if (val >= 0) {
                    currentInput = String.valueOf(Math.sqrt(val));
                    tvDisplay.setText(currentInput);
                } else {
                    tvDisplay.setText("Error");
                    currentInput = "";
                }
            }
        } else if (button.getId() == R.id.btnPercent) {
            if (!currentInput.isEmpty()) {
                double val = Double.parseDouble(currentInput);
                currentInput = String.valueOf(val / 100);
                tvDisplay.setText(currentInput);
            }
        } else {
            // Numbers and Dot
            if (buttonText.equals(".") && currentInput.contains(".")) {
                return;
            }
            // Prevent multiple zeros at start
            if (currentInput.equals("0") && !buttonText.equals(".")) {
                currentInput = buttonText;
            } else {
                currentInput += buttonText;
            }
            tvDisplay.setText(currentInput);
        }
    }

    private void calculateResult() {
        if (!currentInput.isEmpty() && !operator.isEmpty()) {
            double secondOperand = Double.parseDouble(currentInput);
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstOperand + secondOperand;
                    break;
                case "-":
                    result = firstOperand - secondOperand;
                    break;
                case "×":
                    result = firstOperand * secondOperand;
                    break;
                case "÷":
                    if (secondOperand != 0) {
                        result = firstOperand / secondOperand;
                    } else {
                        tvDisplay.setText("Error");
                        currentInput = "";
                        return;
                    }
                    break;
            }

            // Clean formatting to remove decimal if it's an integer
            currentInput = String.valueOf(result);
            if (currentInput.endsWith(".0")) {
                currentInput = currentInput.substring(0, currentInput.length() - 2);
            }
            tvDisplay.setText(currentInput);
        }
    }
}
