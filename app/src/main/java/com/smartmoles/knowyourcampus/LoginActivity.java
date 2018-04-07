package com.smartmoles.knowyourcampus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    SecurePrefs pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = new SecurePrefs(this);
        String ename = Config.eKey;
        String pass = Config.pKey;
        if(!pref.getString(ename).isEmpty() && !pref.getString(pass).isEmpty()){
            Intent go = new Intent(LoginActivity.this, MainActivity.class);
            go.putExtra(ename,pref.getString(ename));
            go.putExtra(pass,pref.getString(pass));
            startActivity(go);
            finish();
        } else {
            EditText en_no = (EditText) findViewById(R.id.en_no);
            EditText name = (EditText) findViewById(R.id.name);
            EditText email = (EditText) findViewById(R.id.email);
            EditText password = (EditText) findViewById(R.id.password);
            EditText contact = (EditText) findViewById(R.id.contact);
        }
    }
}
