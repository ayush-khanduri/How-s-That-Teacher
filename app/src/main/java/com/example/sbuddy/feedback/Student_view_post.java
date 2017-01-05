package com.example.sbuddy.feedback;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sbuddy on 11/30/2016.
 */
public class Student_view_post  extends AppCompatActivity {


    private static final String JSON_URL = "http://rater1105.esy.es/student_view_post.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_view_post);
        getJSON(JSON_URL);
    }

    private void showData(String s)
    {
        String Data[];

        try {
            JSONObject jsonRootObject = new JSONObject(s);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("result");
            //Iterate the jsonArray and print the info of JSONObjects
            Data=new String[jsonArray.length()];

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Data[i]=jsonObject.getString("post");

            }



            ListView list = (ListView) findViewById(R.id.listView2);
            ArrayAdapter<String> data = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Data);
            //list.setAdapter(new CustomList(getApplicationContext(), Data));
            list.setAdapter(data);

        } catch (JSONException e) {e.printStackTrace();}
    }

    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Student_view_post.this, "Please Wait...",null,true,true);
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
                super.onPostExecute(s);
                loading.dismiss();
                showData(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}