package com.smartmoles.knowyourcampus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        final EditText en_no = (EditText) findViewById(R.id.en_no);
        final EditText contact = (EditText) findViewById(R.id.contact);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText feedback = (EditText) findViewById(R.id.feedback);

        Button submit = (Button) findViewById(R.id.btnfeed);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!en_no.getText().toString().isEmpty() && !contact.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !feedback.getText().toString().isEmpty()){
                    new FeedGo(Feedback.this).execute(en_no.getText().toString(),contact.getText().toString(),email.getText().toString(),feedback.getText().toString());
                } else {
                    Toast.makeText(Feedback.this,"Fill All Fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class FeedGo extends AsyncTask<String,Void,String> {
        private Context ctx;
        private ProgressDialog progress;

        FeedGo(Context context){
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
                request = HttpRequest.get(Config.feedbhost, true, "en_no",strings[0],"contact_no",strings[1],"email",strings[2],"feedback",strings[3])
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
            Toast.makeText(Feedback.this,s, Toast.LENGTH_SHORT).show();
            if(s.equals("?") || s.equals("!")){
                Toast.makeText(Feedback.this,"Error To Write Data", Toast.LENGTH_SHORT).show();
            } else {
                if(s.contains("Failed")) {
                    Toast.makeText(ctx,"Error to Send FeedBack", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ctx,"Your FeedBack Saved", Toast.LENGTH_SHORT).show();
                    onBackPressed();
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
