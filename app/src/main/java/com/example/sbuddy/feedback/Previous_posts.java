package com.example.sbuddy.feedback;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.HashMap;

/**
 * Created by sbuddy on 11/30/2016.
 */
public class Previous_posts  extends AppCompatActivity {


    ArrayList<HashMap<String,String>> values;

    private static final String JSON_URL = "http://rater1105.esy.es/previous_posts.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previous_post);

        final Bundle extras = getIntent().getExtras();
        String enroll = extras.getString("enroll");

        values = new ArrayList<>();
        Log.v("sar","fetchpehle");
        Fetch fetch= new Fetch();
        fetch.execute(JSON_URL,enroll);
        Log.v("sar","fetchbaad");
    }

    private void showData(String s)
    {

        String Data[];


        try {
            JSONObject jsonRootObject = new JSONObject(s);
            Log.v("sar","showtry");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("result");
            //Iterate the jsonArray and print the info of JSONObjects

            Data = new String[jsonArray.length()];


            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Data[i]=jsonObject.getString("post");
            }



            Log.v("sar","forbaad");
            ListView list = (ListView) findViewById(R.id.listView);
            ArrayAdapter<String> akchamp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Data);
            //list.setAdapter(new CustomList(getApplicationContext(), Data));
            list.setAdapter(akchamp);
            Log.v("sar","custadap");
        } catch (JSONException e) {e.printStackTrace();Log.v("sar","exceptcust");}
    }

    private class Fetch extends AsyncTask<String, Void, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(Previous_posts.this, "Please Wait...", null, true, true);
            Log.v("sar","pre");
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            showData(aVoid);
            loading.dismiss();
            Log.v("sar","post");
        }


        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }



        @Override
        protected String doInBackground(String... params) {
            String final_result = null;
            String url = params[0];
            try {
                URL login_url = new URL(url);
                HttpURLConnection http = (HttpURLConnection) login_url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream OS = http.getOutputStream();
                BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String post_data = URLEncoder.encode("enroll", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                BF.write(post_data);
                BF.flush();
                BF.close();
                OS.close();
                Log.v("sar","closeconn");

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
                Log.v("sar","except");
                e.printStackTrace();
            }

            return final_result;
        }
    }
}
