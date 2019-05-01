package com.yjj.tesorflow.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.speech_test).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SpeechActivity.class);
            startActivity(intent);
        });
    }
}
