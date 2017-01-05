package com.example.sbuddy.feedback;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TeacherFeed extends AppCompatActivity {


    String ide[] = {};
    String t_id;
    private static final String JSON_URL = "http://rater1105.esy.es/tchr_feed.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_home);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("FEEDBACK");

        Bundle bundle = getIntent().getExtras();
        t_id = bundle.getString("enroll");
        Log.v("tfeed",t_id);
        getJSON(JSON_URL);

    }

    private void showData(String s)
    {
        String Data[] = {};

        try {
            Log.v("tfeed","tryshow");
            JSONObject jsonRootObject = new JSONObject(s);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("result");
            //Iterate the jsonArray and print the info of JSONObjects

            Data = new String[jsonArray.length()];
            ide = new String[jsonArray.length()];
            Log.v("tfeed","bfor");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Data[i]=jsonObject.getString("feed");
                Log.v("tfeed",Data[i]);
                ide[i]=jsonObject.getString("user_id");
            }
            Log.v("tfeed","afor");
            ListView list = (ListView) findViewById(R.id.list);
            ArrayAdapter<String> akchamp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Data);
            //list.setAdapter(new CustomList(getApplicationContext(), Data));
            list.setAdapter(akchamp);
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
        class GetJSON extends AsyncTask<String, Void, String> {
            /*Context c;
            public GetJSON(Context c) {
                this.c=c;
            }
*/
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.v("tfeed","pre");
                loading = ProgressDialog.show(TeacherFeed.this, "Please Wait...",null,true,true);
                Log.v("tfeed","loadingdbd");
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                //String t_id = params[1];
                Log.v("tfeeddo",t_id);
                BufferedReader bufferedReader = null;
                try {
                    Log.v("tfeed","try");
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data =  URLEncoder.encode("t_id", "UTF-8") + "=" + URLEncoder.encode(t_id, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    Log.v("tfeed","except");
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                Log.v("tfeed","post");
                super.onPostExecute(s);
                loading.dismiss();
                showData(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}
