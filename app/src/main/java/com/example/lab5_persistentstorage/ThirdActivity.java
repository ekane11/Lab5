package com.example.lab5_persistentstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ThirdActivity extends AppCompatActivity {

    int noteid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        //Get EditText view
        TextView editText = findViewById(R.id.textView2);

        //Get intent
        Intent intent = getIntent();

        //Get the value of integer "noteid" from intent
        int noteInt = intent.getIntExtra("noteid", -1);

        //initialize class variable "noteid" with the value from intent
        noteid = noteInt;

        Log.d("" +noteid, "");
        if (noteid != -1) {
            Note note = ScreenTwo.notes.get(noteid);
            String noteContent = note.getContent();
            Log.d(noteContent, "message goes here");
            //Use editText.setText() to display the contents of this note on screen.
            editText.setText(noteContent);
        }
    }

    public void saveMethod(View view) {
        //Get editText view and the content that user entered
        TextView editText = findViewById(R.id.textView2);
        String content = editText.getText().toString();

        //initialize SQLiteDatabase instance
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE, null);

        //initialize DBHelper class
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        //set username in the following variable by fetching it from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.lab5_persistentstorage", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        //Save information to database
        String title;
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());

        if (noteid == -1) { //add note
            Log.d("Third Activity", "if");
            title = "NOTE_" + (ScreenTwo.notes.size() + 1);
            dbHelper.saveNotes(username, title, content, date);
        } else {
            Log.d("Third Activity", "else");
            //update note
            Log.d("update Note", content );
            title = "NOTE_" + (noteid + 1);
            dbHelper.updateNote(title, date, content, username);
        }
        Log.d("Third Activity", "save method");
        //go to second activity using intents
        Intent intent = new Intent(this, ScreenTwo.class);
        startActivity(intent);
    }

    public void onButtonClick(View view) {
        saveMethod(view);

    }
}