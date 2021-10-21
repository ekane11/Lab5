package com.example.lab5_persistentstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

        //Get intent

        //Get the value of integer "noteid" from intent

        //initialize class variable "noteid" with the value from intent

        if(noteid != -1){
            Note note = ScreenTwo.notes.get(noteid);
            String noteContent = note.getContent();
            //Use editText.setText() to display the contents of this note on screen.
        }
    }

    public void saveMethod(View view){
        //Get editText view and the content that user entered
        TextView editText = findViewById(R.id.textView);
        String content = editText.toString();

        //initialize SQLiteDatabase instance
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE,null);

        //initialize DBHelper class
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        //set username in the following variable by fetching it from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.lab5_persistentstorage", Context.MODE_PRIVATE);
        String username = sharedPreferences.toString();

        //Save information to database
        String title;
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date = dateFormat.format(new Date());

        if(noteid == -1){ //add note
            title = "NOTE_" + (ScreenTwo.notes.size() + 1);
            dbHelper.saveNotes(username, title, content, date);
        } else {
            //update note
            title = "NOTE" + (noteid + 1);
            dbHelper.updateNote(title, date, content, username);
        }

        //go to second activity using intents
        Intent intent = new Intent(this, ScreenTwo.class);
        startActivity(intent);
    }

    //change to make save button go back to second screen
    public void onButtonClick(View view){
        EditText note = (EditText) findViewById(R.id.textView2);
        Intent intent = new Intent(this, ScreenTwo.class);
        startActivity(intent);
    }


    public class Note {
        private String date;
        private String username;
        private String title;
        private String content;

        public Note(String date, String username, String title, String content){
            this.date = date;
            this.username = username;
            this.title = title;
            this.content = content;
        }

        public String getDate(){ return date; }
        public String getUsername(){ return username; }
        public String getTitle(){ return title; }
        public String getContent() { return content; }
    }

    public class DBHelper {
        SQLiteDatabase sqLiteDatabase;

        public DBHelper(SQLiteDatabase sqLiteDatabase) {
            this.sqLiteDatabase = sqLiteDatabase;
        }

        public void createTable() {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS notes"
            +"(id INTEGER PRIMARY KEY, username TEXT, date TEXT, title TEXT, content TEXT, src TEXT)");

        }

        public ArrayList<Note> readNotes (String username){
            createTable();
            Cursor c = sqLiteDatabase.rawQuery(String.format("SELECT * from notes where username like '%s", username), null);
            int dateIndex = c.getColumnIndex("date");
            int titleIndex = c.getColumnIndex("title");
            int contentIndex = c.getColumnIndex("content");
            c.moveToFirst();

            ArrayList<Note> notesList = new ArrayList<>();

            while(!c.isAfterLast()){
                String title = c.getString(titleIndex);
                String date = c.getString(dateIndex);
                String content = c.getString(contentIndex);

                Note note = new Note(date, username, title, content);
                notesList.add(note);
                c.moveToNext();
            }
            c.close();
            sqLiteDatabase.close();

            return notesList;
        }

        public void saveNotes(String username, String title, String content, String date){
            createTable();
            sqLiteDatabase.execSQL(String.format("INSERT INTO notes (username, date, title, content) VALUES('%s', '%s', '%s', '%s')",
                    username, date, title, content));
        }

        public void updateNote(String title, String date, String content, String username){
            createTable();
            sqLiteDatabase.execSQL(String.format("UPDATE notes set content = '%s', date = '%s' where title = '%s' and username = '%s'"
            , content, date, title, username));
        }
    }
}