package com.smartmoles.knowyourcampus;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    boolean isFaculty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);
        final EditText en_no = (EditText) findViewById(R.id.en_no);
        isFaculty = getIntent().getBooleanExtra(Config.isFact,false);
        if(isFaculty) {
            en_no.setHint("Faculty ID");
        }
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText contact = (EditText) findViewById(R.id.contact);
        final Spinner department = (Spinner) findViewById(R.id.depart);
        final RadioGroup gender = (RadioGroup) findViewById(R.id.radio_g);

        Button regsit = (Button) findViewById(R.id.btnreg);
        regsit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(en_no.getText().toString().isEmpty() || name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty()
                        || contact.getText().toString().isEmpty() || gender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegistrationActivity.this,"Please Fill All Fields", Toast.LENGTH_SHORT).show();
                } else {
                    new Register(RegistrationActivity.this).execute(en_no.getText().toString(),name.getText().toString(),email.getText().toString(),password.getText().toString(),department.getSelectedItem().toString(),contact.getText().toString(),(gender.getCheckedRadioButtonId() == R.id.male) ? "Male" : "Female");
                }
            }
        });
    }

    private class Register extends AsyncTask<String,Void,String>{
        private Context ctx;
        private ProgressDialog progress;

        Register(Context context){
            ctx = context;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(ctx);
            progress.setMessage("Registering...");
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
                request = HttpRequest.get(Config.reghost, true, "is_fact",isFaculty ? "1" : "0",(isFaculty) ? "fac_id" : "en_no",strings[0],"name",strings[1],"email",strings[2],"password",strings[3],(isFaculty) ? "dept_id" : "dept",strings[4],"contact_no",strings[5],"gender",strings[6])
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
                Toast.makeText(RegistrationActivity.this,"Error To Write Data", Toast.LENGTH_SHORT).show();
            } else {
                switch (s){
                    case "Already":
                        Toast.makeText(RegistrationActivity.this,"You Already Registered!", Toast.LENGTH_SHORT).show();
                        break;
                    case "Done":
                        Toast.makeText(RegistrationActivity.this,"Registration Successfully", Toast.LENGTH_SHORT).show();
                        break;
                    case "Failed":
                        Toast.makeText(RegistrationActivity.this,"Wrong Data Entered", Toast.LENGTH_SHORT).show();
                        break;
                }
                onBackPressed();
            }
        }

        private boolean isInternetAvailable(Context ctx) {
            ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }
}
