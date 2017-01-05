package com.example.sbuddy.feedback;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class FilterEditText extends AppCompatActivity {

    public static final String TAG = "nileshG";
    String userText = "checking the words";                   // <--------- change EditText here


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text_filter);

        final Button checkProfanity = (Button) findViewById(R.id.checkProfanity);
        final EditText userInput = (EditText) findViewById(R.id.userInput);

//        userText = userInput.getText().toString();
        checkProfanity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CheckTextField().execute();                     //  to execute CheckTextField's AsyncTask
                Log.v(TAG, "userInput: " + userInput.getText().toString());
                userText = userInput.getText().toString();
                Log.v(TAG, "userText: " + userText);
            }
        });
    }

    class CheckTextField extends AsyncTask<Void, Void, String> {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView responseView = (TextView) findViewById(R.id.responseView);

        private static final String API_URL = "http://api1.webpurify.com/services/rest/";
        private static final String API_KEY = "f8975773ffbfa6e01d80458f39b6a6e1";

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
            if(userText.length() == 0){
                Toast.makeText(FilterEditText.this, "ERROR: input field empty!", Toast.LENGTH_SHORT).show();
            }
        }

        protected String doInBackground(Void... urls) {

            try {

                URL url = new URL(API_URL + "?method=webpurify.live.return&api_key=" + API_KEY + "&text=" + userText /*+ "&format=json"*/);
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

            String DATA = NULL;

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