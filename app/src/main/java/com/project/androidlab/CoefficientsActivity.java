package com.project.androidlab;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Optional;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CoefficientsActivity extends AppCompatActivity {
    FunctionType cachedType = FunctionType.POWER;

    private String getFunctionName()
    {
        int stringId = R.string.expFunc;
        if (cachedType == FunctionType.POWER)
        {
            stringId = R.string.powerFunc;
        }
        return getString(stringId);
    }

    private void setFunctionLabel(String str)
    {
        TextView textView = findViewById(R.id.functionLabelView);
        textView.setText(str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coefficients);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            cachedType = ((FunctionType) extras.get(Globals.FUNCTION_TYPE_NAME));
        }
        setFunctionLabel(getFunctionName());
    }

    public void goBack(View view)
    {
        startActivity(new Intent(this, FunctionTypeActivity.class));
    }

    private void writeExtra(Intent intent, String tag, int id)
    {
        EditText edit = findViewById(id);
        String text = edit.getText().toString();
        double val = 0;
        if (!text.isEmpty())
        {
            val = Double.parseDouble(text);
        }
        intent.putExtra(tag, val);
    }

    public void goToCalculations(View view)
    {
        Intent intent = new Intent(this, CalculationsActivity.class);

        writeExtra(intent, Globals.COEFFICIENT_A, R.id.aValueEdit);
        writeExtra(intent, Globals.COEFFICIENT_B, R.id.bValueEdit);
        writeExtra(intent, Globals.COEFFICIENT_C, R.id.cValueEdit);
        writeExtra(intent, Globals.BEGIN_PERIOD, R.id.beginPeriodEdit);
        writeExtra(intent, Globals.END_PERIOD, R.id.endPeriodEdit);
        intent.putExtra(Globals.FUNCTION_TYPE_NAME, cachedType);

        startActivity(intent);
    }
}