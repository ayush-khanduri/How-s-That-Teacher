package com.example.sbuddy.feedback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

class CustomList extends ArrayAdapter<String> {


    CustomList(Context context, String[] names) {
        super(context,R.layout.custom_row, names);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         LayoutInflater akchampsinflater = LayoutInflater.from(getContext());
         View customView = akchampsinflater.inflate(R.layout.custom_row,parent,false);

        String singlename = getItem(position);

        TextView txt = (TextView)customView.findViewById(R.id.txtw);
        txt.setText(singlename);
        final Button button = (Button)customView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setText("Followed");
            }
        });
        return customView;
    }
}
