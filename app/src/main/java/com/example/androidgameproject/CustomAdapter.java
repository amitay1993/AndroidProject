package com.example.androidgameproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private List<User> userlist;
    private Context context;

    public CustomAdapter(List<User> userlist, Context context) {
        this.userlist = userlist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userlist.size();
    }

    @Override
    public Object getItem(int position) {
        return userlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.customlistview,parent,false);
        }

        User user = userlist.get(position);
        TextView nameTv = convertView.findViewById(R.id.name1_tv);
        TextView scoreTv = convertView.findViewById(R.id.score2_tv);
        TextView distanceTv = convertView.findViewById(R.id.distance_tv3);

        nameTv.setText(user.getName());
        scoreTv.setText(String.valueOf(user.getScore()));
        distanceTv.setText(String.valueOf(user.getDistance()));

        return convertView;
    }
}
