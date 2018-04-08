package com.smartmoles.knowyourcampus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
    boolean isFaculty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pref = new SecurePrefs(this);
        String ename = Config.eKey;
        final String pass = Config.pKey;
        if(pref.getString(ename) != null && pref.getString(pass) != null && Prefs.with(this).readBoolean(Config.isLogged,false)){
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
                    isFaculty = true;
                }
            });
        }
    }

    void passLogin(final boolean isFact){
        setContentView(R.layout.activity_login);
        final EditText er_no = (EditText) findViewById(R.id.en_no);
        if(isFact) {
            er_no.setHint("Faculty ID");
        }
        final EditText password = (EditText) findViewById(R.id.pass);
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
                if(!er_no.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                    pref.putString(Config.eKey, er_no.getText().toString());
                    pref.putString(Config.pKey, password.getText().toString());
                    new Login(LoginActivity.this).execute(er_no.getText().toString(),password.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "Fill Both Fields", Toast.LENGTH_SHORT).show();
                }
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

    private class Login extends AsyncTask<String,Void,String> {
        private Context ctx;
        private ProgressDialog progress;

        Login(Context context){
            ctx = context;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(ctx);
            progress.setMessage("Connecting...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (!isInternetAvailable(ctx)) {
                return "?";
            }
            HttpRequest request;
            try {
                progress.setMessage("Logging...");
                request = HttpRequest.get(Config.reghost, true, "is_fact",isFaculty ? "1" : "0",(isFaculty) ? "fac_id" : "en_no",strings[0],"password",strings[1])
                        .followRedirects(true).connectTimeout(6000);
            } catch (HttpRequest.HttpRequestException e) {
                return "?";
            }
            if (request.ok()) {
                return request.body();
            }
            return "!";
        }

        @Override
        protected void onPostExecute(String s) {
            progress.dismiss();
            if(s.equals("?") || s.equals("!")){
                Toast.makeText(LoginActivity.this,"Error To Write Data", Toast.LENGTH_SHORT).show();
            } else {
                if(s.equals("Done")) {
                    Intent go = new Intent(LoginActivity.this, MainActivity.class);
                    go.putExtra(Config.eKey, pref.getString(Config.eKey));
                    go.putExtra(Config.pKey, pref.getString(Config.pKey));
                    Prefs.with(LoginActivity.this).writeBoolean(Config.isLogged,true);
                    startActivity(go);
                    finish();
                } else {
                    pref.remove(Config.eKey);
                    pref.remove(Config.pKey);
                    Toast.makeText(LoginActivity.this,"Wrong Data Entered", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private boolean isInternetAvailable(Context ctx) {
            ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }
}
