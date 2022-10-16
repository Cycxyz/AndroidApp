package com.project.androidlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class CoefficientsActivity extends AppCompatActivity {

    FunctionType type = FunctionType.POWER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coefficients);
        Bundle extras = getIntent().getExtras();
        type = (FunctionType)extras.get(Globals.FUNCTION_TYPE_NAME);
    }
}