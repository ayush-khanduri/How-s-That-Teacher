package com.example.sbuddy.feedback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.net.URL;
import java.net.URLEncoder;

import static android.widget.Toast.makeText;

/**
 * Created by nileshg on 6/12/16.
 */
public class Nillepropage extends AppCompatActivity {

    String t;
    //ImageView imageView;
    String em="",check;
    ProgressDialog pg;
    Button b,b1;
    ImageView imageView2;
    TextView enroll,batch,branch,sem,name;
    TextView tchrating, tname, tbranch, tuid;
    String total;
    final String[] ty = {null};


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                makeText(this, "selected Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Student_view_post.class));
                return true;
            case R.id.action_logout:
                makeText(this, "selected Logout", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.nille_propage);


        //imageView = (ImageView) findViewById(R.id.imageView2);


        final Bundle extras = getIntent().getExtras();


        imageView2 = (ImageView) findViewById(R.id.imageView2);
        tname = (TextView) findViewById(R.id.teacher_profile_name);
        tbranch = (TextView) findViewById(R.id.teacher_profile_branch);
        tuid = (TextView) findViewById(R.id.teacher_profile_enrollment);
        tchrating = (TextView) findViewById(R.id.teacher_profile_rating);
        b = new Button(this);
        b = (Button)findViewById(R.id.teacher_profile_enter);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(getApplicationContext(), tp.class);
                //i.putExtra("type",ty[0]);
                Toast.makeText(getApplicationContext(),ty[0],Toast.LENGTH_SHORT).show();

                i.putExtra("enroll", extras.getString("enroll"));
                startActivity(i);

            }
        });
        b1 = new Button(this);
        b1 = (Button) findViewById(R.id.teacher_logout);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AfterSplash.class));
                finish();
            }
        });


        check=extras.getString("type");
        Log.v("pic",check+check);
        FetchData fetchData = new FetchData();
        fetchData.execute(extras.getString("enroll"),extras.getString("type"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void pic(){
        Log.v("picasso1_main",check);
        if(check.equals("Teacher") && !em.equals("")) {
            Log.v("picasso1_main",check);
            Picasso.with(Nillepropage.this)
                    .load(em)
                    .into(imageView2);
            Log.v("picasso1_mainif",em);
        }

        Log.v("picasso1_main",check);
    }

    public class FetchData extends AsyncTask<String,Void,String[]> {

        String res[] = new String[5] ;
        ProgressDialog loading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(Nillepropage.this, "Please Wait...", null, true, true);
        }

        @Override
        protected void onPostExecute(String s[]) {

            super.onPostExecute(s);

            pic();
            tuid.setText(s[0]);
            tname.setText(s[1]);
            tbranch.setText(s[2]);
            tchrating.setText(total);

            loading.dismiss();
            Toast.makeText(getApplicationContext(),ty[0],Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String[] doInBackground(String... params) {

            String Enroll = params[0];
            String final_result = null;
            try{
                URL login_url = new URL("http://rater1105.esy.es/student_profile.php");
                HttpURLConnection http = (HttpURLConnection) login_url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                OutputStream OS = http.getOutputStream();
                BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String post_data = URLEncoder.encode("enroll", "UTF-8") + "=" + URLEncoder.encode(Enroll, "UTF-8") + "& "+ URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
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
                final_result = result.toString();
                JSONObject root = new JSONObject(final_result);
                JSONArray response = root.getJSONArray("result");
                JSONObject finaljsonobject = response.getJSONObject(0);
                res[0] = finaljsonobject.getString("enroll");
                res[1] = finaljsonobject.getString("name");
                res[2] = finaljsonobject.getString("branch");
                res[3] = finaljsonobject.getString("batch");
                res[4] = finaljsonobject.getString("sem");
                //res[5] = finaljsonobject.getString("em");
                em=res[4];

                total = finaljsonobject.getString("total");

                BR.close();

            }catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            t = params[1];
            return res;
        }
    }
}