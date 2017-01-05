package com.example.sbuddy.feedback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
 * Created by sbuddy on 12/1/2016.
 */
public class Chat_room_main_activity extends AppCompatActivity {

    ListView l;
    EditText e;
    Button b;
    String JSON_URL_1= "http://rater1105.esy.es/insert_messg.php";
    ArrayList<HashMap<String,String>> values;
    String grp_id;
    ArrayList<HashMap<String,String>> x;
    String enroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        values = new ArrayList<>();
        x = new ArrayList<>();
        b = new Button(this);
        e = new EditText(this);

        b = (Button) findViewById(R.id.button8);
        e = (EditText)findViewById(R.id.editText3);


        final Bundle extras = getIntent().getExtras();
        enroll = extras.getString("enroll");

        Fetch1 fetch= new Fetch1();
        fetch.execute("http://rater1105.esy.es/grp_msg.php");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = e.getText().toString();
                InsertMessage insertMessage = new InsertMessage();
                insertMessage.execute(JSON_URL_1,enroll,message);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

    }

    private void showData(String s)
    {

        try {
            JSONObject jsonRootObject = new JSONObject(s);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("result");
            //Iterate the jsonArray and print the info of JSONObjects

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String Data=jsonObject.getString("Sender");
                String ide=jsonObject.getString("Message");
                String time=jsonObject.getString("time");
                HashMap<String, String> contact = new HashMap<>();
                contact.put("enroll", Data);
                contact.put("post", ide);
                contact.put("time", time);
                //  temp++;
                values.add(contact);
            }

            l = (ListView)findViewById(R.id.listView3);
            ListAdapter adapter = new SimpleAdapter(getApplicationContext(), values, R.layout.chat_room_custom_row,new String[]{"enroll","post","time"}, new int[]{R.id.textView12,R.id.textView13,R.id.textView14} );
            //ArrayAdapter<String> akchamp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Data);
            l.setAdapter(adapter);

        } catch (JSONException e) {e.printStackTrace();}
    }
    //*******************************************************************************************************************************
    public class Fetch1 extends AsyncTask<String, Void, String> {
        ProgressDialog loading;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(Chat_room_main_activity.this, "Loading...",null,true,true);
        }

        @Override
        protected String doInBackground(String... params) {

            String uri = params[0];
            StringBuilder sb = new StringBuilder();

            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();


                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String json;
                while((json = bufferedReader.readLine())!= null){
                    sb.append(json+"\n");
                }



            }catch(Exception e){

            }
            return sb.toString().trim();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            showData(s);
            //loading.dismiss();
        }
    }










    private class InsertMessage extends AsyncTask<String, Void, String> {
        // ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//                loading = ProgressDialog.show(getApplicationContext(), "Please Wait...",null,true,true);
        }

        @Override
        protected String doInBackground(String... params) {

            String final_result = null;

            String uri = params[0];

            BufferedReader bufferedReader = null;
            try {
                URL login_url = new URL(uri);
                HttpURLConnection http = (HttpURLConnection) login_url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream OS = http.getOutputStream();
                BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String post_data = URLEncoder.encode("enroll", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8")  + "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //loading.dismiss();
            final Bundle extras = getIntent().getExtras();

            enroll = extras.getString("enroll");

            Fetch1 fetch= new Fetch1();
            fetch.execute("http://rater1105.esy.es/grp_msg.php");
            e.setText("");
            // Fetch fetch= new Fetch();
            // fetch.execute(JSON_URL,grp_id);
        }
    }

}