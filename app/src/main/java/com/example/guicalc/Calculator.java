package com.example.guicalc;

/**
 * Calculator.java
 * ----------------
 * Pure calculator logic - completely separated from the UI.
 * Demonstrates good architecture: business logic != presentation.
 *
 * Lab 1 - MAD (IIT2201/ICS2201/ISE2201)
 * Harare Institute of Technology
 */
public class Calculator {

    private double firstOperand = 0;
    private double secondOperand = 0;
    private String currentOperator = "";
    private boolean isNewInput = true;
    private String displayValue = "0";

    // -------------------------------------------------------
    // Input handling
    // -------------------------------------------------------

    /**
     * Called when the user presses a digit (0-9) or decimal point.
     */
    public String inputDigit(String digit) {
        if (isNewInput) {
            displayValue = digit.equals(".") ? "0." : digit;
            isNewInput = false;
        } else {
            // Prevent multiple decimal points
            if (digit.equals(".") && displayValue.contains(".")) {
                return displayValue;
            }
            // Prevent leading zeros like "007"
            if (displayValue.equals("0") && !digit.equals(".")) {
                displayValue = digit;
            } else {
                displayValue += digit;
            }
        }
        return displayValue;
    }

    /**
     * Called when the user presses an operator (+, -, ×, ÷).
     */
    public String inputOperator(String operator) {
        firstOperand = parseDisplay();
        currentOperator = operator;
        isNewInput = true;
        return displayValue;
    }

    /**
     * Called when the user presses = to compute the result.
     */
    public String computeResult() {
        secondOperand = parseDisplay();
        double result = 0;
        boolean error = false;

        switch (currentOperator) {
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
                if (secondOperand == 0) {
                    displayValue = "Error";
                    reset();
                    return displayValue;
                }
                result = firstOperand / secondOperand;
                break;
            default:
                // No operator was set — just show what's there
                return displayValue;
        }

        displayValue = formatResult(result);
        firstOperand = result;
        currentOperator = "";
        isNewInput = true;
        return displayValue;
    }

    /**
     * Called when the user presses the +/- toggle.
     */
    public String toggleSign() {
        double value = parseDisplay();
        value = -value;
        displayValue = formatResult(value);
        return displayValue;
    }

    /**
     * Called when the user presses the % button.
     */
    public String percentage() {
        double value = parseDisplay();
        value = value / 100.0;
        displayValue = formatResult(value);
        return displayValue;
    }

    /**
     * Deletes the last character entered (backspace).
     */
    public String deleteLast() {
        if (isNewInput || displayValue.equals("0") || displayValue.length() <= 1) {
            displayValue = "0";
        } else {
            displayValue = displayValue.substring(0, displayValue.length() - 1);
            if (displayValue.equals("-")) displayValue = "0";
        }
        return displayValue;
    }

    /**
     * Clears everything — back to the initial state.
     */
    public String clear() {
        reset();
        return displayValue;
    }

    // -------------------------------------------------------
    // Getters
    // -------------------------------------------------------

    public String getDisplayValue() {
        return displayValue;
    }

    public String getCurrentOperator() {
        return currentOperator;
    }

    // -------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------

    private void reset() {
        firstOperand = 0;
        secondOperand = 0;
        currentOperator = "";
        isNewInput = true;
        displayValue = "0";
    }

    private double parseDisplay() {
        try {
            return Double.parseDouble(displayValue);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Formats the result cleanly:
     * - Whole numbers show without decimal (e.g. 6.0 → "6")
     * - Decimals show up to 10 significant digits
     */
    private String formatResult(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return "Error";
        }
        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            return String.valueOf((long) value);
        }
        // Limit to 10 decimal places and strip trailing zeros
        String formatted = String.format("%.10f", value);
        formatted = formatted.replaceAll("0*$", "").replaceAll("\\.$", "");
        return formatted;
    }
}
