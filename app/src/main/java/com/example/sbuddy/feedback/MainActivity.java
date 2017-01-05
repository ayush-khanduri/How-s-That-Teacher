package com.example.sbuddy.feedback;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity{


    public static final String MyPREFERENCES = "MyPref";
    public static final String Name = "nameKey";
    public static final String Password = "passKey";
    public static final String Type = "typeKey";
    SharedPreferences sharedPreferences;


    String enroll;
    String t;
    String row;
    final String URL = "http://rater1105.esy.es/login.php";
    EditText userId, password;
    Button login, signup;
    RadioGroup radioGroup;
    SQLiteDatabase db;

    final String[] ty = {null};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        userId = new EditText(this);
        password = new EditText(this);
        login = new Button(this);
        signup = new Button(this);
        userId = (EditText) findViewById(R.id.user_id);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
//        signup = (Button) findViewById(R.id.signup);
        radioGroup = new RadioGroup(this);
        radioGroup = (RadioGroup) findViewById(R.id.login_rg);

        TextView forgotPassword = (TextView) findViewById(R.id.forgot_password);
        TextView createAccount = (TextView) findViewById(R.id.createAccount);

        TextInputLayout inputLayoutUserID = (TextInputLayout) findViewById(R.id.inputLayout_user_id);
        inputLayoutUserID.setError("Enrollment No. is required"); // show error
        inputLayoutUserID.setError(null); // hide error

        TextInputLayout inputLayoutPassword = (TextInputLayout) findViewById(R.id.inputLayout_password);
        inputLayoutPassword.setError("Password is required"); // show error
        inputLayoutPassword.setError(null); // hide error

        sharedPreferences = getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.Teacher){
                    ty[0] ="Teacher";
                }
                if(checkedId==R.id.Student){
                    ty[0] ="Student";
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });

        assert forgotPassword != null;
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });

        assert createAccount != null;
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
    }

    public void login(View view) {

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        String user_id = userId.getText().toString();
        String user_password = password.getText().toString();
        Background background = new Background(this);
        editor.putString(Name,user_id);
        editor.putString(Password,user_password);
        editor.putString(Type,ty[0]);
        editor.commit();
        String type = "login";

        background.execute(type, user_id, user_password,ty[0]);
//
    }



    public class Background extends AsyncTask<String,Void,String> {
        String final_result;
        Context C;

        ProgressDialog loading;

        Background(Context ctx) {
            this.C = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this, "Please Wait...", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            String rowcount = "";
            String type = params[0];
            String user_id = params[1];
            String user_password = params[2];
            String url = "";
            if(ty!=null){
            if (type == "login") {
                url = "http://rater1105.esy.es/login.php";
                try {
                    URL login_url = new URL(url);
                    HttpURLConnection http = (HttpURLConnection) login_url.openConnection();
                    http.setRequestMethod("POST");
                    http.setDoOutput(true);
                    http.setDoInput(true);
                    OutputStream OS = http.getOutputStream();
                    BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&" + URLEncoder.encode("user_password", "UTF-8") + "=" + URLEncoder.encode(user_password, "UTF-8") + "&" + URLEncoder.encode("user_type","UTF-8") + "=" + URLEncoder.encode(ty[0],"UTF-8");
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
                    JSONObject root = new JSONObject(final_result);
                    JSONArray response = root.getJSONArray("result");
                    JSONObject finaljsonobject = response.getJSONObject(0);
                    rowcount = finaljsonobject.getString("row_count");

                    IS.close();
                    http.disconnect();

                } catch (Exception e) {
                }
            }}
            //row=rowcount;

            enroll = params[1];
            t = params[0];
            return rowcount;
        }





        @Override
        protected void onPostExecute(String aVoid) {
            loading.dismiss();

            if(aVoid.equals("1") && ty[0]=="Student"){

                Intent i;
                i = new Intent(getApplicationContext(), sp.class);
                i.putExtra("type",ty[0]);
//                Toast.makeText(getApplicationContext(),ty[0],Toast.LENGTH_SHORT).show();

                i.putExtra("enroll", enroll);
                startActivity(i);

            }
            if(aVoid.equals("1") && ty[0]=="Teacher"){

                Intent i;
                i = new Intent(getApplicationContext(), Nillepropage.class);
                i.putExtra("type",ty[0]);
//                Toast.makeText(getApplicationContext(),ty[0],Toast.LENGTH_SHORT).show();

                i.putExtra("enroll", enroll);
                startActivity(i);

            }
            if(aVoid.equals("0")) {
//                Toast.makeText(getApplicationContext(),"Wrong id or password",Toast.LENGTH_SHORT).show();
                password.setText("");
                userId.setText("");
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
