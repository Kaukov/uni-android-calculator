package k4uk0v.unicalculatorapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText result;
    private EditText newInput;
    private TextView selectedOperator;
    private Double operand = null;
    private String pendingOperation = "=";

    private static final String STATE_PENDING_OPERATION = "PendingOperation";
    private static final String STATE_OPERAND = "Operand";

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button b = (Button) v;

            newInput.append(b.getText().toString());
        }
    };

    View.OnClickListener operationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button b = (Button) v;

            String operator = b.getText().toString();
            String value = newInput.getText().toString();

            try {
                Double val = Double.valueOf(value);
                performOperation(val, operator);
            } catch (NumberFormatException e) {
                newInput.setText("");
            }

            pendingOperation = operator;
            selectedOperator.setText(pendingOperation);
        }
    };

    View.OnClickListener negativeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String value = newInput.getText().toString();

            if (value.length() == 0) {
                newInput.setText("-");
            } else {
                try {
                    Double doubleValue = Double.valueOf(value);

                    doubleValue *= -1;

                    newInput.setText(doubleValue.toString());
                } catch (NumberFormatException e) {
                    newInput.setText("");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        newInput = findViewById(R.id.newInput);
        selectedOperator = findViewById(R.id.selectedOperator);

        selectedOperator.setText("");

        // Calculator number buttons (0-9)
        Button button0 = findViewById(R.id.button0);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);

        // Calculator decimal point button
        Button buttonDecimalPoint = findViewById(R.id.buttonDecimalPoint);

        // Calculator operation buttons (+, -, *, /)
        Button buttonEquals = findViewById(R.id.buttonEquals);
        Button buttonAddition = findViewById(R.id.buttonAddition);
        Button buttonSubtraction = findViewById(R.id.buttonSubtraction);
        Button buttonMultiplication = findViewById(R.id.buttonMultiplication);
        Button buttonDivision = findViewById(R.id.buttonDivision);

        // Calculator negative/positive conversion button
        Button buttonNegative = findViewById(R.id.buttonNegative);

        Button[] numberButtons = new Button[]{
                button0, button1, button2, button3, button4,
                button5, button6, button7, button8, button9
        };

        Button[] operationButtons = new Button[]{
                buttonEquals, buttonAddition, buttonSubtraction,
                buttonMultiplication, buttonDivision
        };

        for (Button button : numberButtons) {
            button.setOnClickListener(listener);
        }

        for (Button button : operationButtons) {
            button.setOnClickListener(operationListener);
        }

        buttonDecimalPoint.setOnClickListener(listener);

        buttonNegative.setOnClickListener(negativeListener);
    }

    private void performOperation(Double value, String operation) {
        if (null == operand) {
            operand = value;
        } else {
            if (pendingOperation.equals("=")) {
                pendingOperation = operation;
            }

            switch (pendingOperation) {
                case "+":
                    operand += value;
                    break;

                case "-":
                    operand -= value;
                    break;

                case "*":
                    operand *= value;
                    break;

                case "/":
                    if (value == 0) {
                        operand = 0.0;
                    } else {
                        operand /= value;
                    }
                    break;

                case "=":
                    operand = value;
                    break;
            }
        }

        result.setText(operand.toString());

        newInput.setText("");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION);
        operand = savedInstanceState.getDouble(STATE_OPERAND);

        selectedOperator.setText(pendingOperation);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_PENDING_OPERATION, selectedOperator.getText().toString());

        if (operand != null) {
            outState.putDouble(STATE_OPERAND, operand);
        }

        super.onSaveInstanceState(outState);
    }
}
