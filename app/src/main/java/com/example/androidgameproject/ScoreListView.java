package com.example.androidgameproject;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ScoreListView extends ListActivity {

    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_list_view);

        ListView listView=findViewById(android.R.id.list);


        try {
            FileInputStream fileInputStream=openFileInput("LeaderBoard");
            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
            users= (ArrayList<User>) objectInputStream.readObject();
            objectInputStream.close();

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        Collections.sort(users);
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(this,android.R.layout.simple_list_item_1,users);

        setListAdapter(adapter);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Toast.makeText(this, users.get(position).toString(), Toast.LENGTH_SHORT).show();
    }



}
