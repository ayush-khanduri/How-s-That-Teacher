package com.example.sbuddy.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by sbuddy on 12/1/2016.
 */
public class Chat_groups extends AppCompatActivity {

    ListView l;
    String G[]={"Android"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_groups);

        l = (ListView)findViewById(R.id.Groups);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, G);
        l.setAdapter(adapter);
        l.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        final Bundle bundle = getIntent().getExtras();
                        //final String stud = "1";
                        Intent i = new Intent(getApplicationContext(),Chat_room_main_activity.class);
                        i.putExtra("GroupId",pos);
                        i.putExtra("enroll",bundle.getString("enroll"));
                        startActivity(i);
                    }
                });
    }
}