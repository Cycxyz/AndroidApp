package com.project.androidlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToInfo(View view)
    {
        startActivity(new Intent(this, InfoActivity.class));
    }

    public void goToFunctionType(View view)
    {
        startActivity(new Intent(this, FunctionTypeActivity.class));
    }
}