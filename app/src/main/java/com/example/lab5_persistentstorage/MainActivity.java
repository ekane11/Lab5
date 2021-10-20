package com.example.lab5_persistentstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static String usernameKey;

    //String usernameKey = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usernameKey = "username";
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.lab5_persistentstorage", Context.MODE_PRIVATE);
        if(!sharedPreferences.getString(usernameKey, "").equals("")){
            //code here for usernameKey to be shared preference
            sharedPreferences.getString(usernameKey, "");
            goToActivity2(usernameKey);
        } else {

            setContentView(R.layout.activity_main);
        }

    }

    public void onButtonClick(View view){
        EditText username = (EditText) findViewById(R.id.editTextTextPersonName2);
        String str = username.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.lab5_persistentstorage", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("username", str).apply();
        goToActivity2(str);
    }

    public void goToActivity2(String s){
        Intent intent = new Intent(this, ScreenTwo.class);
        intent.putExtra("username", s);
        startActivity(intent);
    }
}