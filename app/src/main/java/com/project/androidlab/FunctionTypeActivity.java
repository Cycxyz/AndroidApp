package com.project.androidlab;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
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
        startActivity(new Intent(this, MainActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void goChoseCoefficients(View view)
    {
        Intent intent = new Intent(this, CoefficientsActivity.class);
        Spinner spinner = findViewById(R.id.spinner);
        intent.putExtra(Globals.FUNCTION_TYPE_NAME, FunctionType.values()[(int)spinner.getSelectedItemId()]);
        startActivity(intent);
    }
}