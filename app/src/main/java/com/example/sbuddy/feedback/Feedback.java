package com.example.sbuddy.feedback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by hp-pc on 04-10-2016.
 */
public class Feedback extends AppCompatActivity {

    EditText editText ;String TAG= "nileshG";
    TextView responseView;
    Button submit;
    String tid;
    //ListView listFeed;
    String ide[]= {};



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back);
        Bundle i = getIntent().getExtras();
        tid = i.getString("Teacher_id");
        final String sid = i.getString("Student_id");
        //sid = (EditText) findViewById(R.id.sid);
        editText = (EditText) findViewById(R.id.editText);
        responseView = (TextView) findViewById(R.id.webpurifyResponse);
        submit = (Button) findViewById(R.id.submit);
        //listFeed = (ListView) findViewById(listFeed);
        getJSON("http://rater1105.esy.es/getfeed.php");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData insert = new insertData();
                //new CheckTextField().execute();                     //  to execute CheckTextField's AsyncTask
                insert.execute(sid, tid, editText.getText().toString());
            }
        });
    }

    private void showData(String s)
    {
        String Data[] = {};

        try {
            JSONObject jsonRootObject = new JSONObject(s);
            Log.v("ak","showdata");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("result");
            //Iterate the jsonArray and print the info of JSONObjects

            Data = new String[jsonArray.length()];
            ide = new String[jsonArray.length()];

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Data[i]=jsonObject.getString("posts");
                Log.v("json",Data[i]);
                ide[i]=jsonObject.getString("user_id");
            }

            ListView listFeed = (ListView) findViewById(R.id.listFeed);
            ArrayAdapter<String> akchamp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Data);
            //list.setAdapter(new CustomList(getApplicationContext(), Data));
            listFeed.setAdapter(akchamp);
            /*
            list.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                            String s = String.valueOf(adapterView.getItemAtPosition(pos));
                            Bundle bundle = getIntent().getExtras();
                            String stud = bundle.getString("enroll");
                            //final String stud = "1";
                            Intent i = new Intent(getApplicationContext(),TeacherProfile.class);
                            i.putExtra("Teacher_id",ide[pos]);
                            i.putExtra("Student_id",stud);
                            startActivity(i);
                        }
                    });
                    */
        } catch (JSONException e) {e.printStackTrace();}
    }

    public void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                Log.v("ak","onprefeedbackhome");
                super.onPreExecute();
                Log.v("ak","pre");
                loading = ProgressDialog.show(Feedback.this, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    Log.v("ak","try");
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream OS = con.getOutputStream();
                    BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String post_data = URLEncoder.encode("tid", "UTF-8") + "=" + URLEncoder.encode(tid, "UTF-8");
                    Log.v("tid",tid);
                    BF.write(post_data);
                    BF.flush();
                    BF.close();
                    OS.close();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    Log.v("ak","exception");
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                Log.v("ak","post");
                super.onPostExecute(s);
                loading.dismiss();
                showData(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }

    //*****************************************************************************************************************************************
    private class insertData extends AsyncTask<String,String,String>{

        ProgressDialog loading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(Feedback.this, "Please Wait...",null,true,true);

        }

        @Override
        protected String doInBackground(String... param) {

            String fb, t_id, s_id;
            String uri = "http://rater1105.esy.es/feedback.php";
            s_id = param[0];
            t_id = param[1];
            fb = param[2];
            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream outputStream = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("s_id", "UTF-8") + "=" + URLEncoder.encode(s_id, "UTF-8") + "&"
                        + URLEncoder.encode("t_id", "UTF-8") + "=" + URLEncoder.encode(t_id, "UTF-8") + "&"
                        + URLEncoder.encode("fb", "UTF-8") + "=" + URLEncoder.encode(fb, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = con.getInputStream();
                inputStream.close();
                con.disconnect();

            }catch(MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Feedback Successfully Submitted";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();
            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    //*****************************************************************************************************************************************

    class CheckTextField extends AsyncTask<Void, Void, String> {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView responseView = (TextView) findViewById(R.id.webpurifyResponse);

        private static final String API_URL = "http://api1.webpurify.com/services/rest/";
        private static final String API_KEY = "f8e1ddc6a8e6623f50de6df7a7cd8951";

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
            if(editText.length() == 0){
                Toast.makeText(Feedback.this, "ERROR: input field empty!", Toast.LENGTH_SHORT).show();
            }
        }

        protected String doInBackground(Void... urls) {

            try {

                URL url = new URL(API_URL + "?method=webpurify.live.return&api_key=" + API_KEY + "&text=" + editText + "&format=json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {

            if (response == null) {
                response = "response is null!";
            }
            progressBar.setVisibility(View.GONE);
            Log.i(TAG, response);
            responseView.setText(response);

            String DATA = null;

            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONObject rsp = jsonObject.getJSONObject("rsp");

                JSONObject attributes = rsp.getJSONObject("@attributes");
                String stat = attributes.getString("stat");

                int found = rsp.getInt("found");

                JSONArray expletiveArray = rsp.getJSONArray("expletive");
                String[] expletive = new String[expletiveArray.length()];
                for (int i = 0; i < expletiveArray.length(); i++) {
                    expletive[i] = expletiveArray.getString(i);
                }

                DATA = "\nstatus: "+stat+"\nfound "+found+" expletive(s):\n";
                for (String anExpletive : expletive) {
                    DATA = DATA+anExpletive+"\n";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            responseView.setText(DATA);

        }

    }
}
