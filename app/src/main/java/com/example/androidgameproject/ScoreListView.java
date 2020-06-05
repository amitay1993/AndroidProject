package com.example.androidgameproject;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ScoreListView extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_list_view);

        ListView listView=findViewById(android.R.id.list);
        ArrayList<Player> players = new ArrayList<>();
        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this,android.R.layout.simple_list_item_1);

        SharedPreferences sp=getSharedPreferences("liran_file",MODE_PRIVATE);
        String player_name= sp.getString("nickname","");
        int player_score= sp.getInt("score", Integer.parseInt("0"));
        players.add(player1);

        setListAdapter(adapter);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


    }



}
