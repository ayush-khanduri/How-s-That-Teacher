package com.example.sbuddy.feedback;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by sbuddy on 8/30/2016.
 */
public class SignUp extends AppCompatActivity {

    ProgressDialog pg;

    EditText user_id,password,confirm_password;
    TextView step1;
    Button signupstep1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup);
        user_id = new EditText(this);
        password = new EditText(this);
        confirm_password = new EditText(this);
        step1 = new TextView(this);
        signupstep1 = new Button(this);
        user_id = (EditText) findViewById(R.id.signup_enrollment_number);
        password = (EditText) findViewById(R.id.singup_password);
        confirm_password = (EditText) findViewById(R.id.signup_confirm);
        step1 = (TextView) findViewById(R.id.signup_step1);
        signupstep1 = (Button) findViewById(R.id.signup_step1_button);
        signupstep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_id.getText().toString().length()==10 && user_id.getText().toString().matches("[0-9]+")) {
                    if (password.getText().toString().equals(confirm_password.getText().toString())) {
                        InsertIntoTable insert = new InsertIntoTable(getApplicationContext());
                        insert.execute(user_id.getText().toString(), password.getText().toString(), confirm_password.getText().toString());
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Password not match",Toast.LENGTH_LONG).show();
                        password.setText("");
                        confirm_password.setText("");
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enrollment number should have only numbers of length 10",Toast.LENGTH_LONG).show();
                    user_id.setText("");
                }
            }
        });
      //  step1.setTextColor(Color.parseColor("#fff"));
      //  step1.setBackgroundColor(Color.parseColor("#00f"));

    }


    private class InsertIntoTable extends AsyncTask<String,Void,String>{
        Context C;
        AlertDialog.Builder signUpAlert;


        public InsertIntoTable(Context applicationContext) {
            C=applicationContext;
        }


        @Override
        protected String doInBackground(String... params) {
            String final_result = null;
            String url = "http://rater1105.esy.es/signUpStep1.php";
            String user_id = params[0];
            String user_password = params[1];
            String confirm_password = params[2];
            // boolean validation = user_id.matches("[0-9]+");

            try {
                URL login_url = new URL(url);
                HttpURLConnection http = (HttpURLConnection) login_url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream OS = http.getOutputStream();
                BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&" + URLEncoder.encode("user_password", "UTF-8") + "=" + URLEncoder.encode(user_password, "UTF-8");
                BF.write(post_data);
                BF.flush();
                BF.close();
                OS.close();


                InputStream IS = http.getInputStream();
                BufferedReader BR = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                StringBuilder result = new StringBuilder();
                String line = null;
                while ((line = BR.readLine()) != null) {
                    result.append(line + "\n");
                }
                BR.close();
                final_result = result.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }



            return final_result;
        }


        @Override
        protected void onPreExecute() {

            Toast.makeText(C,"insering",Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String aVoid) {

            Intent i = new Intent(getApplicationContext(),SignUpStep2.class);
            i.putExtra("EnrollNumber",user_id.getText().toString());
            startActivity(i);
            //  Toast.makeText(C,aVoid,Toast.LENGTH_LONG).show();

        }


        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}


