package com.example.nathaniel.trojan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickbutton1(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append("1");
    }

    public void clickbutton2(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append("2");
    }

    public void clickbutton3(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append("3");
    }

    public void clickbutton4(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append("4");
    }

    public void clickbutton5(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append("5");
    }

    public void clickbutton6(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append("6");
    }

    public void clickbutton7(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append("7");
    }

    public void clickbutton8(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append("8");
    }

    public void clickbutton9(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append("9");
    }

    public void clickbutton0(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append("0");
    }

    public void clickbuttonperiod(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append(".");
    }

    public void clickbuttondivide(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append(" / ");
    }

    public void clickbuttonmultiply(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append(" * ");
    }

    public void clickbuttonsubtract(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append(" - ");
    }

    public void clickbuttonadd(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.append(" + ");
    }

    public void clickbuttonenter(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        String calctext = calcfield.getText().toString();
        String evalcalctext = String.valueOf(eval(calctext));
        calcfield.setText(evalcalctext);
    }

    public void clickbuttonclear(View view){
        TextView calcfield = (TextView)findViewById(R.id.calcfield);
        calcfield.setText("");
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
