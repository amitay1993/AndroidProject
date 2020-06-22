package com.example.androidgameproject;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ScoreListView extends AppCompatActivity {

   ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_list_view);

        ListView listView=findViewById(R.id.custom_list_view);


        try {
            FileInputStream fileInputStream=openFileInput("LeaderBoard");
            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
            users= (ArrayList<User>) objectInputStream.readObject();
            objectInputStream.close();

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        Collections.sort(users);

        CustomAdapter customAdapter = new CustomAdapter(users,this);
        listView.setAdapter(customAdapter);


    }


}
