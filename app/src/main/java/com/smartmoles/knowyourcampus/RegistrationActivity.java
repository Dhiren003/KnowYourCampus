package com.smartmoles.knowyourcampus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        EditText en_no = (EditText) findViewById(R.id.en_no);
        if(getIntent().getBooleanExtra(Config.isFac,false)) {
            en_no.setHint("Faculty ID");
        }
        EditText name = (EditText) findViewById(R.id.name);
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        EditText contact = (EditText) findViewById(R.id.contact);
        Spinner department = (Spinner) findViewById(R.id.depart);
        RadioGroup gender = (RadioGroup) findViewById(R.id.radio_g);

        Button regsit = (Button) findViewById(R.id.btnreg);
        regsit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegistrationActivity.this,"Registration Data Sent", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }
}
