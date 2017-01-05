package com.example.sbuddy.feedback;

/**
 * Created by sbuddy on 10/6/2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FeedbackHome extends AppCompatActivity {

    String ide[] = {};
    private static final String JSON_URL = "http://rater1105.esy.es/tchr.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_home);
        getJSON(JSON_URL);

    }

    private void showData(String s)
    {
        String Data[] = {};

        try {
            JSONObject jsonRootObject = new JSONObject(s);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("result");
            //Iterate the jsonArray and print the info of JSONObjects

            Data = new String[jsonArray.length()];
            ide = new String[jsonArray.length()];

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Data[i]=jsonObject.getString("teacher_name");
                ide[i]=jsonObject.getString("user_id");
            }

            ListView list = (ListView) findViewById(R.id.list);
            ArrayAdapter<String> akchamp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Data);
            //list.setAdapter(new CustomList(getApplicationContext(), Data));
            list.setAdapter(akchamp);
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
        } catch (JSONException e) {e.printStackTrace();}
    }

    public void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.v("feed","pre");
                loading = ProgressDialog.show(FeedbackHome.this, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                Log.v("feed","post");
                super.onPostExecute(s);
                loading.dismiss();
                showData(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}


