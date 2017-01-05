package com.example.sbuddy.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button BACK,EMAIL;
    public static final String TAG = "nileshG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        BACK = new Button(this);
        EMAIL = new Button(this);
        BACK = (Button) findViewById(R.id.backToLoginBtn);
        EMAIL = (Button) findViewById(R.id.emailAdmin);

        BACK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        EMAIL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "admin@jiit.ac.in" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Reset password request for How's That Teacher?");
                intent.putExtra(Intent.EXTRA_TEXT, "I forgot my password for How's That Teacher?. I request you to kindly reset the password for \nEnrollment No.: \nName: ");
                startActivity(Intent.createChooser(intent, ""));

//                startActivity(new Intent(getApplicationContext(), SignUp.class));
//                finish();
            }
        });

    }
}
