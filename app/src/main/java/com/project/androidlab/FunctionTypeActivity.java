package com.project.androidlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FunctionTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_type);
    }

    public void goBack(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
//        Spinner spinner = findViewById(R.id.spinner);
//        intent.putExtra("Chosen function", spinner.getSelectedItemId());
        startActivity(intent);
    }
}