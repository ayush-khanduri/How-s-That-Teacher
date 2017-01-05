package com.example.sbuddy.feedback;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by sbuddy on 11/29/2016.
 */
public class Posts extends AppCompatActivity {

    EditText p;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);

        p = new EditText(this);
        b = new Button(this);
        p = (EditText)findViewById(R.id.editText2);
        b = (Button) findViewById(R.id.button7);



        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle extras = getIntent().getExtras();
                String post = p.getText().toString();
                String id = extras.getString("enroll");
                FetchData fetchData = new FetchData();
                fetchData.execute(id,post);
            }
        });

    }
    private class FetchData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String aVoid) {

            Toast.makeText(getApplicationContext(),"inserted",Toast.LENGTH_LONG).show();

        }


        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }



        @Override
        protected String doInBackground(String... params) {
            String final_result = null;
            String Enroll = params[0];
            String url = "http://rater1105.esy.es/wall_teacher.php";
            try {
                URL login_url = new URL(url);
                HttpURLConnection http = (HttpURLConnection) login_url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream OS = http.getOutputStream();
                BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String post_data = URLEncoder.encode("enroll", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&" + URLEncoder.encode("post", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
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
    }

}
