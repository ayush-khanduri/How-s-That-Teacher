package com.example.sbuddy.feedback;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class TeacherReview extends AppCompatActivity {

    RatingBar rb5;
    RatingBar rb1;
    RatingBar rb2;
    RatingBar rb3;
    RatingBar rb4;
    Button bt;
    //EditText stid;
    Bundle i;
    String s1="0.0";
    String s2="0.0" ;
    String s3="0.0" ;
    String s4="0.0" ;
    String s5="0.0" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_review);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        i = getIntent().getExtras();


        final String tid = i.getString("Teacher_id");
        final String sid = i.getString("Student_id");
        rb1=(RatingBar)findViewById(R.id.ratingBar2);
        rb2=(RatingBar)findViewById(R.id.ratingBar);
        rb3=(RatingBar)findViewById(R.id.ratingBar3);
        rb4=(RatingBar)findViewById(R.id.ratingBar4);
        rb5=(RatingBar)findViewById(R.id.ratingBar5);
        bt=(Button)findViewById(R.id.button4);


        //final String tid = "1";


        ///stid = (EditText) findViewById(R.id.stid);

        rb1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                s1 = String.valueOf(v);
            }
        });



        rb2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                s2 = String.valueOf(v);

            }
        });



        rb3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                s3 = String.valueOf(v);

            }
        });
        rb4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                s4 = String.valueOf(v);

            }
        });
        rb5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                s5 = String.valueOf(v);

            }
        });

        bt.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                insertData insert = new insertData();
                //insert.execute(stid.getText().toString(),tid , s1,s2,s3,s4,s5);
                insert.execute(sid,tid , s1,s2,s3,s4,s5);

            }
        });

    }

    private class insertData extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String t_id, s_id;
            //String rb1 = null,rb2=null,rb3=null,rb4=null,rb5=null;
            String uri = "http://rater1105.esy.es/ratingstarteacher.php";
            s_id = param[0];
            t_id = param[1];

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");

                con.setDoOutput(true);
                con.setDoInput(true);
                OutputStream outputStream = con.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("stid", "UTF-8") + "=" + URLEncoder.encode(s_id, "UTF-8") + "&"
                        + URLEncoder.encode("tid", "UTF-8") + "=" + URLEncoder.encode(t_id, "UTF-8") + "&"
                        + URLEncoder.encode("rb1", "UTF-8") + "=" + URLEncoder.encode(param[2], "UTF-8")+ "&"
                        + URLEncoder.encode("rb2", "UTF-8") + "=" + URLEncoder.encode(param[3], "UTF-8") + "&"
                        + URLEncoder.encode("rb3", "UTF-8") + "=" + URLEncoder.encode(param[4], "UTF-8") + "&"
                        + URLEncoder.encode("rb4", "UTF-8") + "=" + URLEncoder.encode(param[5], "UTF-8") + "&"
                        + URLEncoder.encode("rb5", "UTF-8") + "=" + URLEncoder.encode(param[6], "UTF-8") ;
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
            return "Rating Successfully Submitted.";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
            /*
            Intent x = new Intent(getApplicationContext(), TeacherProfile.class);
            x.putExtra("Teacher_id", i.getString("Teacher_id"));
            x.putExtra("Student_id", i.getString("Student_id"));
            startActivity(x);
            */
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
