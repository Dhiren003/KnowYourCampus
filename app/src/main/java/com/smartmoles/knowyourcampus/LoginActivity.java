package com.smartmoles.knowyourcampus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    SecurePrefs pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pref = new SecurePrefs(this);
        String ename = Config.eKey;
        final String pass = Config.pKey;
        if(pref.getString(ename) != null && pref.getString(pass) != null){
            Intent go = new Intent(LoginActivity.this, MainActivity.class);
            go.putExtra(ename,pref.getString(ename));
            go.putExtra(pass,pref.getString(pass));
            startActivity(go);
            finish();
        } else {
            setContentView(R.layout.activity_selection);
            Button student = (Button) findViewById(R.id.stu);
            student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passLogin(false);
                }
            });
            Button faculty = (Button) findViewById(R.id.fac);
            faculty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passLogin(true);
                }
            });
        }
    }

    void passLogin(final boolean isFact){
        setContentView(R.layout.activity_login);
        EditText er_no = (EditText) findViewById(R.id.en_no);
        if(isFact) {
            er_no.setHint("Faculty ID");
        }
        EditText password = (EditText) findViewById(R.id.pass);
        TextView forgot = (TextView) findViewById(R.id.forgotpass);
        forgot.setClickable(true);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPass.class));
            }
        });

        Button login = (Button) findViewById(R.id.btnlog);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Login Data Sent", Toast.LENGTH_SHORT).show();
            }
        });

        Button reg = (Button) findViewById(R.id.btnreg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regist = new Intent(LoginActivity.this, RegistrationActivity.class);
                regist.putExtra(Config.isFact,isFact);
                startActivity(regist);
            }
        });
    }
}
