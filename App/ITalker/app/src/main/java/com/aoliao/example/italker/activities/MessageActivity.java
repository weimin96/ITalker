package com.aoliao.example.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.aoliao.example.factory.model.Author;
import com.aoliao.example.italker.R;

public class MessageActivity extends Activity {

    public static void show(Context context, Author author){
        Intent intent = new Intent(context,MessageActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

}
