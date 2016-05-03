package com.example.nathaniel.trojan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class emailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        ArrayList<String> emailList = (ArrayList<String>) getIntent().getSerializableExtra("emailList");
        TextView emailtextview = (TextView)findViewById(R.id.emailtextview);
        for(String email: emailList){
            emailtextview.append(email + "\n");
        }
    }
}
