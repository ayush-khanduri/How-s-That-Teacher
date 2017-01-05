package com.example.sbuddy.feedback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by sbuddy on 11/30/2016.
 */
public class CustomList2 extends ArrayAdapter<String> {


        CustomList2(Context context, String[] names) {
                super(context,R.layout.previous_post_custom_row, names);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater akchampsinflater = LayoutInflater.from(getContext());
                View customView = akchampsinflater.inflate(R.layout.previous_post_custom_row,parent,false);

                String singlename = getItem(position);

                TextView txt = (TextView)customView.findViewById(R.id.post);
                txt.setText(singlename);

                return customView;
        }
}
