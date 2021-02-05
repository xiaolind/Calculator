package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView textViewMain;
    TextView textViewSub;
    String strMainNumber = "";
    boolean isOperatorKeyPushed = false;
    double result = 0;
    int recentOperator = R.id.b_eq;
    static int count = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewMain = findViewById(R.id.tv_main_number);
        textViewSub = findViewById(R.id.tv_sub_number);

        findViewById(R.id.b_0).setOnClickListener(buttonNumberListener);
        findViewById(R.id.b_1).setOnClickListener(buttonNumberListener);
        findViewById(R.id.b_2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.b_3).setOnClickListener(buttonNumberListener);
        findViewById(R.id.b_4).setOnClickListener(buttonNumberListener);
        findViewById(R.id.b_5).setOnClickListener(buttonNumberListener);
        findViewById(R.id.b_6).setOnClickListener(buttonNumberListener);
        findViewById(R.id.b_7).setOnClickListener(buttonNumberListener);
        findViewById(R.id.b_8).setOnClickListener(buttonNumberListener);
        findViewById(R.id.b_9).setOnClickListener(buttonNumberListener);

        findViewById(R.id.b_dot).setOnClickListener(buttonDotListener);

        findViewById(R.id.b_add).setOnClickListener(buttonOperatorListener);
        findViewById(R.id.b_sub).setOnClickListener(buttonOperatorListener);
        findViewById(R.id.b_mult).setOnClickListener(buttonOperatorListener);
        findViewById(R.id.b_dev).setOnClickListener(buttonOperatorListener);
        findViewById(R.id.b_eq).setOnClickListener(buttonOperatorListener);

        findViewById(R.id.b_clear).setOnClickListener(buttonClearListener);
        findViewById(R.id.b_delete).setOnClickListener(buttonClearListener);

        findViewById(R.id.b_change).setOnClickListener(buttonChangeListener);
    }

    View.OnClickListener buttonNumberListener = new View.OnClickListener(){

        public void onClick(View view) {
            Button button = (Button)view;

            if(isOperatorKeyPushed == true){
                strMainNumber = button.getText().toString();
            }else{
                strMainNumber = strMainNumber + button.getText().toString();
            }

            textViewMain.setText(editNumber(strMainNumber,false));
            isOperatorKeyPushed = false;
        }
    };

    View.OnClickListener buttonDotListener = new View.OnClickListener(){

        public void onClick(View view){
            Button button = (Button)view;

            if(isOperatorKeyPushed == false && strMainNumber != ""){

                if(strMainNumber.indexOf(".")<0) {
                    strMainNumber = strMainNumber + ".";
                }

            }else{
                strMainNumber = "0.";
            }

            textViewMain.setText(editNumber(strMainNumber,false));
            isOperatorKeyPushed = false;
        }
    };

    View.OnClickListener buttonOperatorListener = new View.OnClickListener(){

        public void onClick(View view) {
            Button operatorButton = (Button)view;

            if(strMainNumber==""){
                return;
            }

            double value = Double.parseDouble(strMainNumber);
            if (recentOperator == R.id.b_eq){
                result = value;
                textViewSub.setText("");
            } else {
                result = calc(recentOperator, result, value);
                strMainNumber = String.valueOf(result);
                textViewMain.setText(editNumber(strMainNumber,true));
            }

            recentOperator = operatorButton.getId();
//            textView.setText(operatorButton.getText());
            isOperatorKeyPushed = true;
        }
    };

    View.OnClickListener buttonClearListener = new View.OnClickListener(){
        public void onClick(View view) {

            Button clearButton = (Button) view;

            switch (clearButton.getId()) {
                case R.id.b_clear:
                    strMainNumber = "";
                    textViewMain.setText("0");
                    recentOperator = R.id.b_eq;
                    isOperatorKeyPushed = false;
                    break;
                case R.id.b_delete:
                    if(strMainNumber.length() >= 1) {
                        strMainNumber = strMainNumber.substring(0, strMainNumber.length() - 1);
                    }
                    if(strMainNumber.equals("") || strMainNumber.equals("-")){
                        strMainNumber = "";
                        textViewMain.setText("0");
                    }else {
                        textViewMain.setText(editNumber(strMainNumber, false));
                    }
                    break;
            }
        }
    };

    View.OnClickListener buttonChangeListener = new View.OnClickListener(){
        public void onClick(View view){

            if(strMainNumber.indexOf("-") >= 0) {
                strMainNumber = strMainNumber.substring(1, strMainNumber.length());
            }else if(strMainNumber.indexOf("-") < 0 && !strMainNumber.equals("0") && !strMainNumber.equals("")){
                strMainNumber = "-" + strMainNumber;
            }

            textViewMain.setText(editNumber(strMainNumber,false));
            isOperatorKeyPushed = false;
        }
    };

    double calc(int operator, double value1, double value2){
        switch (operator) {
            case R.id.b_add:
                return value1 + value2;
            case R.id.b_sub:
                return value1 - value2;
            case R.id.b_mult:
                return value1 * value2;
            case R.id.b_dev:
                return value1 / value2;
            default:
                return value1;
        }
    }

    String editNumber(String inputNumber,boolean isOperatorKeyPushed){

        String strInt;
        String strDecimal;
        String[] splitResult;

        if(inputNumber.equals("")){
            return "0";
        }

        if(inputNumber.indexOf(".")>-1) {
            splitResult = inputNumber.split(Pattern.quote("."), -1);
            if (isOperatorKeyPushed == true && splitResult[1].equals("0")) {
                return formatInt(splitResult[0]);
            } else{
                return formatInt(splitResult[0]) + "." + formatDecimal(splitResult[1]);
            }
        }else{
            return formatInt(inputNumber);
        }
    }

    String formatInt(String inputInt){

        String result;
        String prefix;
        String splitResult[];
        StringBuffer resultReverse;

        if(inputInt.indexOf("-") > -1){
            prefix = "-";
            splitResult = inputInt.substring(1,inputInt.length()) .split("(?!^)");
        }else {
            prefix = "";
            splitResult = inputInt.split("(?!^)");
        }
        result = splitResult[splitResult.length -1];

        for(int i= 1; i <= splitResult.length -1 ; i +=1){
            if(i % count == 0) {
                result += "," + splitResult[splitResult.length - 1 - i];
            }else{
                result += splitResult[splitResult.length - 1 - i];
            }
        }
        resultReverse = new StringBuffer(result);

        return prefix + resultReverse.reverse().toString();
    }

    String formatDecimal(String inputDecimal){

        String result;
        String splitResult[] = inputDecimal.split("(?!^)");
        result = splitResult[0];

        for(int i= 1; i <= splitResult.length -1 ; i +=1){
            if(i % count == 0) {
                result += "'" + splitResult[i];
            }else{
                result += splitResult[i];
            }
        }
        return result;
    }
}