package com.example.sbuddy.feedback;

/**
 * Created by sbuddy on 10/6/2016.
 */

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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TeacherRanking extends AppCompatActivity {


    String ide[] = {};
    private static final String JSON_URL = "http://rater1105.esy.es/ratingstarteacher.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_ranking);
        Log.v("pp","xml");
        getJSON(JSON_URL);

    }

    private void showData(String s)
    {
        String Data[] = {};

        try {
            Log.v("pp","try");
            JSONObject jsonRootObject = new JSONObject(s);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("result1");
            //Iterate the jsonArray and print the info of JSONObjects

            Data = new String[jsonArray.length()];
            ide = new String[jsonArray.length()];

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Data[i]=jsonObject.getString("tname");
                Log.v("pp",Data[i]);
                ide[i]=jsonObject.getString("user_id");
            }
            Log.v("pp","listsepehle");
            ListView rlistview = (ListView) findViewById(R.id.rlistview);
            ArrayAdapter<String> akchamp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Data);
            //list.setAdapter(new CustomList(getApplicationContext(), Data));
            rlistview.setAdapter(akchamp);
            /*
            rlistview.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                            String s = String.valueOf(adapterView.getItemAtPosition(pos));
                            //Bundle bundle = getIntent().getExtras();
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
                super.onPreExecute();
                Log.v("ak","pre");
                loading = ProgressDialog.show(TeacherRanking.this, "Please Wait...",null,true,true);
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
                    Log.v("ak","json");
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
}


