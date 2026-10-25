package com.example.unitconverter;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText inputValue;
    Spinner spinnerFrom, spinnerTo;
    Button btnConvert;
    TextView txtResult;
    RadioGroup categoryGroup;

    // Base unit conversions
    Map<String, Double> lengthMap = new HashMap<>();
    Map<String, Double> weightMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        btnConvert = findViewById(R.id.btnConvert);
        txtResult = findViewById(R.id.txtResult);
        categoryGroup = findViewById(R.id.categoryGroup);

        // LENGTH: base = meter
        lengthMap.put("Centimeters", 0.01);
        lengthMap.put("Meters", 1.0);
        lengthMap.put("Kilometers", 1000.0);
        lengthMap.put("Inches", 0.0254);
        lengthMap.put("Feet", 0.3048);

        // WEIGHT: base = gram
        weightMap.put("Grams", 1.0);
        weightMap.put("Kilograms", 1000.0);
        weightMap.put("Pounds", 453.592);
        weightMap.put("Ounces", 28.3495);

        // Initial load
        updateSpinners("length");

        categoryGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbLength) updateSpinners("length");
            else if (checkedId == R.id.rbWeight) updateSpinners("weight");
            else if (checkedId == R.id.rbTemp) updateSpinners("temp");
            txtResult.setText("Result will appear here");
            inputValue.setText("");
        });

        btnConvert.setOnClickListener(v -> convert());
    }

    void updateSpinners(String category) {
        String[] units;
        if (category.equals("length")) {
            units = new String[]{"Centimeters", "Meters", "Kilometers", "Inches", "Feet"};
        } else if (category.equals("weight")) {
            units = new String[]{"Grams", "Kilograms", "Pounds", "Ounces"};
        } else {
            units = new String[]{"Celsius", "Fahrenheit", "Kelvin"};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
        // set different default for To
        if (units.length > 1) spinnerTo.setSelection(1);
    }

    void convert() {
        String inputStr = inputValue.getText().toString().trim();

        // [ ] Input validation - Toast
        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }
        double input;
        try {
            input = Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input: Enter a numeric value only", Toast.LENGTH_SHORT).show();
            return;
        }

        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();
        int catId = categoryGroup.getCheckedRadioButtonId();
        double result;

        if (catId == R.id.rbLength) {
            double fromFactor = lengthMap.get(from);
            double toFactor = lengthMap.get(to);
            result = input * fromFactor / toFactor;
        } else if (catId == R.id.rbWeight) {
            double fromFactor = weightMap.get(from);
            double toFactor = weightMap.get(to);
            result = input * fromFactor / toFactor;
        } else {
            // Temperature - special formulas
            double celsius = toCelsius(input, from);
            result = fromCelsius(celsius, to);

            // Absolute zero check
            if (celsius < -273.15) {
                Toast.makeText(this, "Below absolute zero!", Toast.LENGTH_LONG).show();
                txtResult.setText("Error: Below absolute zero (-273.15°C)");
                return;
            }
        }

        // [ ] Result TextView with unit label
        txtResult.setText(String.format("%.4f %s = %.4f %s", input, from, result, to));
    }

    double toCelsius(double val, String unit) {
        if (unit.equals("Celsius")) return val;
        if (unit.equals("Fahrenheit")) return (val - 32) * 5/9;
        return val - 273.15; // Kelvin
    }

    double fromCelsius(double c, String unit) {
        if (unit.equals("Celsius")) return c;
        if (unit.equals("Fahrenheit")) return (c * 9/5) + 32;
        return c + 273.15;
    }
}
