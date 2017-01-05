package com.example.sbuddy.feedback;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class TeacherProfile extends AppCompatActivity {

    private TextView textView;
    private TextView textView2;
    private  TextView textView3;
    ImageView imageView;
    EditText editText;
    public Button button2 = null;
    String em="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_profile);
        Bundle i = getIntent().getExtras();
        final String sid = i.getString("Student_id");
        final String tid = i.getString("Teacher_id");

        Button button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        imageView = (ImageView) findViewById(R.id.imageView);
        JSONTask jk = new JSONTask(getApplicationContext());
        jk.execute("http://rater1105.esy.es/ratingSys.php", tid);

        chfollow ch = new chfollow(getApplicationContext());
        ch.execute(sid, tid);
/*
        Picasso.with(TeacherProfile.this)
                .load("http://www.jiit.ac.in/uploads/AnuradhaGupta.jpg")
                .into(imageView);
*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String id = editText.getText().toString();
                Intent i = new Intent(getApplicationContext(), TeacherReview.class);
                i.putExtra("Teacher_id", tid);
                i.putExtra("Student_id", sid);
                startActivity(i);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow ff = new follow(getApplicationContext());
                ff.execute(sid, tid);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Feedback.class);
                i.putExtra("Teacher_id", tid);
                i.putExtra("Student_id", sid);
                startActivity(i);
            }
        });
    }

    public void pic(){
        if(!em.equals("")) {
            Picasso.with(TeacherProfile.this)
                    .load(em)
                    .into(imageView);
        }
        Log.v("picasso1_main",em);
    }

    public class JSONTask extends AsyncTask<String, String, String[]> {
        ProgressDialog loading;
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        Context c;

        public JSONTask(Context c) {
            this.c = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(TeacherProfile.this, "Please Wait...", null, true, true);
        }

        @Override
        protected String[] doInBackground(String... params) {

            String as = params[1];

            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                connection.setDoOutput(true);
                connection.setDoInput(true);

                connection.connect();


                OutputStream ostream = connection.getOutputStream();
                BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(ostream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(as, "UTF-8");

                BF.write(post_data);
                BF.flush();
                BF.close();

                ostream.close();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";

                StringBuffer buffer = new StringBuffer();

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);

                JSONArray parentArray = parentObject.getJSONArray("result");

                JSONObject finalObject = parentArray.getJSONObject(0);

                String teachername = finalObject.getString("teacher_name");

                String branch = finalObject.getString("branch");

                String r = finalObject.getString("total");



                em = finalObject.getString("em");


                String[] A = {teachername, branch,r};
                Log.v("picasso",em);
                stream.close();

                return A;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            pic();
            textView.setText(result[0]);
            textView2.setText(result[1]);
            textView3.setText(result[2]);
            loading.dismiss();
        }
    }

    //******************************************************************************************************************************
    public class chfollow extends AsyncTask<String, String, String> {

        Context c;
        String final_result;
        ProgressDialog loading;

        public chfollow(Context applicationContext) {
            Log.v("ak","con se pehle");
            this.c = applicationContext;
            Log.v("ak","con k baad");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(getApplicationContext(),"check box pre", Toast.LENGTH_LONG).show();
            Log.v("ak","check box pre");
            loading = ProgressDialog.show(TeacherProfile.this, "Please Wait...", null, true, true);
        }

        @Override
        protected String doInBackground(String... param) {
            Log.v("ak","backg");
            String t_id, s_id;
            String rowcount = "";
            String uri = "http://rater1105.esy.es/chfollow.php";
            s_id = param[0];
            t_id = param[1];
            Log.v("champ",s_id);
            try {
                Log.v("ak","try beg");
                URL login_url = new URL(uri);
                HttpURLConnection http = (HttpURLConnection) login_url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                Log.v("ak","conn done");
                OutputStream OS = http.getOutputStream();
                BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String post_data = URLEncoder.encode("s_id", "UTF-8") + "=" + URLEncoder.encode(s_id, "UTF-8") + "&" + URLEncoder.encode("t_id", "UTF-8") + "=" + URLEncoder.encode(t_id, "UTF-8");
                Log.v("champ1",s_id);
                BF.write(post_data);
                BF.flush();
                BF.close();
                OS.close();
                Log.v("ak","posted");
                InputStream IS = http.getInputStream();
                Log.v("ak","b4iso");
                BufferedReader BR = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                Log.v("ak","afteriso");
                StringBuilder result = new StringBuilder();
                String line = null;
                while ((line = BR.readLine()) != null) {
                    result.append(line + "\n");
                }
                BR.close();
                final_result = result.toString();
                JSONObject root = new JSONObject(final_result);
                JSONArray response = root.getJSONArray("result");
                JSONObject finaljsonobject = response.getJSONObject(0);
                rowcount = finaljsonobject.getString("row_count");
                IS.close();
                http.disconnect();
                Toast.makeText(getApplicationContext(),"try end", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Log.v("ak","exception");
            }
            return rowcount;
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(result);
            Log.v("ak","in_post");
            if(result.equals("1")) {
                button2.setText("FOLLOWING");
            }
            super.onPostExecute(result);
            loading.dismiss();
            //Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    //********************************************************************************************************************************
    public class follow extends AsyncTask<String,String,String> {

        Context c;
        public follow(Context applicationContext) {
            this.c=applicationContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {
            String  t_id, s_id;
            String uri = "http://rater1105.esy.es/follow.php";
            s_id = param[0];
            t_id = param[1];
            Log.v("champ",s_id);
            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream outputStream = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("s_id", "UTF-8") + "=" + URLEncoder.encode(s_id, "UTF-8") + "&"+ URLEncoder.encode("t_id", "UTF-8") + "=" + URLEncoder.encode(t_id, "UTF-8");
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
            return "following...";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            button2.setText("FOLLOWING");
            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}

