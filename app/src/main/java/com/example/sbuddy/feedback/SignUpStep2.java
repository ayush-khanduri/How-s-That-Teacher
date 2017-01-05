package com.example.sbuddy.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.app.ProgressDialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbuddy on 9/22/2016.
 */
public class SignUpStep2 extends AppCompatActivity{

    ProgressDialog pg;

    EditText name,batch;
    Button button;
    Spinner branch,year;
    TextView enrollment_number;
    String b;
    String s;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup_step2);

        enrollment_number = new TextView(this);
        name = new EditText(this);
        batch = new EditText(this);
        branch = new Spinner(this);
        year = new Spinner(this);
        enrollment_number = (TextView)findViewById(R.id.signupstep2_enrollment_number);
        final Bundle extras = getIntent().getExtras();
        enrollment_number.setText(extras.getString("EnrollNumber"));
        name = (EditText)findViewById(R.id.singupstep2_name);
        batch = (EditText)findViewById(R.id.signupstep_batch);
        branch = (Spinner)findViewById(R.id.signupstep_branch);
        year = (Spinner)findViewById(R.id.signupstep_sem);
        List<String> categories = new ArrayList<String>();
        categories.add("CSE");
        categories.add("ECE");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(dataAdapter);
        List<String> sem = new ArrayList<String>();
        sem.add("1");
        sem.add("2");
        sem.add("3");
        sem.add("4");
        sem.add("5");
        sem.add("6");
        sem.add("7");
        sem.add("8");
        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sem);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(data);
        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 b = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button = (Button)findViewById(R.id.signup_step2_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().matches("[a-z A-Z]+") && name.getText().toString().trim().length()>3) {
                    if (batch.getText().toString().matches("[FfEe][1-8]")) {
                        Update update = new Update(getApplicationContext());
                        update.execute(extras.getString("EnrollNumber"), name.getText().toString(), batch.getText().toString(), branch.getSelectedItem().toString(), year.getSelectedItem().toString());
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Enter a valid Batch",Toast.LENGTH_LONG).show();
                        batch.setText("");
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Enter a valid Name",Toast.LENGTH_LONG).show();
                    name.setText("");
                }
            }
        });


    }

    private class Update extends AsyncTask<String,Void,String>{
        Context C;
        public Update(Context applicationContext) {
            C=applicationContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        @Override
        protected String doInBackground(String... params) {

            String Enroll = params[0];
            String Name = params[1];
            String Batch = params[2];
            String Branch = params[3];
            String Year = params[4];
            String final_result = null;
            try{
                URL login_url = new URL("http://rater1105.esy.es/signUpStep2.php");
                HttpURLConnection http = (HttpURLConnection) login_url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream OS = http.getOutputStream();
                BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String post_data = URLEncoder.encode("enroll", "UTF-8") + "=" + URLEncoder.encode(Enroll, "UTF-8") + "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8") + "&" + URLEncoder.encode("batch", "UTF-8") + "=" + URLEncoder.encode(Batch, "UTF-8") + "&" + URLEncoder.encode("branch", "UTF-8") + "=" + URLEncoder.encode(Branch, "UTF-8") + "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(Year, "UTF-8");
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
                final_result = result.toString();
                BR.close();

            }catch (IOException e){
                e.printStackTrace();
            }

            return final_result;
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
