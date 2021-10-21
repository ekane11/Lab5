package com.example.lab5_persistentstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScreenTwo extends AppCompatActivity {

    TextView textView;

    public static ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_two);
        Log.d("String Two", "onCreate");
        textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        String str = intent.getStringExtra("username");


        SharedPreferences sharedPreferences = getSharedPreferences("com.example.lab5_persistentstorage", Context.MODE_PRIVATE);
        str = sharedPreferences.getString("username", str);
        textView.setText("Hello " + str +"!");

        //get SQLiteDatabase instance
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE,null);

        //initiate the "notes" class variable using readNotes method implemented in DBHelper class.
        //Use the username you got from SharedPreferences as a parameter to readNotes method.
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        notes = dbHelper.readNotes(str);
        Log.d(str, "print " + notes.size());

        Log.d("Screen Two", "OnCreate- after read notes");
        //Create an ArrayList<String> object by iterating over notes object
        ArrayList<String> displayNotes = new ArrayList<>();
        for(Note note : notes){
            displayNotes.add(String.format("Title:%s\nDate:%s", note.getTitle(), note.getDate()));
        }

        //Use listview view to display notes on screen
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayNotes);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        //add onItemClickListener for Listview item, a note in our case
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);

                intent.putExtra("noteid", i);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater openMenu = getMenuInflater();
        openMenu.inflate(R.menu.testmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.item1){
            Intent intent = new Intent(this, MainActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.lab5_persistentstorage", Context.MODE_PRIVATE);
            sharedPreferences.edit().remove(MainActivity.usernameKey).apply();
            startActivity(intent);
            return true;
        } else if(item.getItemId() == R.id.item2){
            Intent intent = new Intent(this, ThirdActivity.class);
            startActivity(intent);

        }

        return true;
    }
}