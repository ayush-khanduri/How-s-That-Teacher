package com.example.sbuddy.feedback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by sbuddy on 11/30/2016.
 */
public class CustomList3 extends ArrayAdapter<String> {


    CustomList3(Context context, String[] names) {
        super(context,R.layout.previous_post_custom_row, names);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater akchampsinflater = LayoutInflater.from(getContext());
        View customView = akchampsinflater.inflate(R.layout.previous_post_custom_row,parent,false);

        String singlename = getItem(position);

        TextView txt = (TextView)customView.findViewById(R.id.textView9);
        txt.setText(singlename);

        return customView;
    }
}