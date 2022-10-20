package com.project.androidlab;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

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

    private void setValueIfContains(Bundle extras, String key, int textEditId)
    {
        if (extras.containsKey(key)) {
            EditText edit = findViewById(textEditId);
            double res = extras.getDouble(key);
            edit.setText(Double.toString(res));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coefficients);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            cachedType = ((FunctionType) extras.get(Globals.FUNCTION_TYPE_NAME));
            setValueIfContains(extras, Globals.COEFFICIENT_A, R.id.aValueEdit);
            setValueIfContains(extras, Globals.COEFFICIENT_B, R.id.bValueEdit);
            setValueIfContains(extras, Globals.COEFFICIENT_C, R.id.cValueEdit);
            setValueIfContains(extras, Globals.BEGIN_PERIOD, R.id.beginPeriodEdit);
            setValueIfContains(extras, Globals.END_PERIOD, R.id.endPeriodEdit);
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

    private File getExternalPath() {
        return new File(getExternalFilesDir(null), Globals.FILE_NAME);
    }

    StringBuilder appendCoefficient(int coeffId, StringBuilder builder)
    {
        EditText edit = findViewById(coeffId);
        String text = edit.getText().toString();
        if (text.isEmpty()) {
            builder.append("0 ");
        }
        else {
            builder.append(text);
            builder.append(" ");
        }
        return builder;
    }

    public void saveText(View view){

        try(FileOutputStream fos = new FileOutputStream(getExternalPath())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder = appendCoefficient(R.id.aValueEdit, stringBuilder);
            stringBuilder = appendCoefficient(R.id.bValueEdit, stringBuilder);
            stringBuilder = appendCoefficient(R.id.cValueEdit, stringBuilder);
            stringBuilder = appendCoefficient(R.id.beginPeriodEdit, stringBuilder);
            stringBuilder = appendCoefficient(R.id.endPeriodEdit, stringBuilder);
            fos.write(stringBuilder.toString().getBytes());
            Toast.makeText(this, "File saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private Scanner scanner;
    double tryReadDouble() throws NoSuchFieldException {
        if (scanner.hasNextDouble())
        {
            return scanner.nextDouble();
        }
        throw new NoSuchFieldException();
    }
    
    void setText(int textEditId, double value)
    {
        EditText edit = findViewById(textEditId);
        edit.setText(Double.toString(value));
    }
    // открытие файла
    public void openText(View view){

        File file = getExternalPath();
        if(!file.exists()) return;

        try {
            scanner = new Scanner(file);
            double aTemp, bTemp, cTemp, periodBeginTemp, periodEndTemp;
            aTemp = tryReadDouble();
            bTemp = tryReadDouble();
            cTemp = tryReadDouble();
            periodBeginTemp = tryReadDouble();
            periodEndTemp = tryReadDouble();
            setText(R.id.aValueEdit, aTemp);
            setText(R.id.bValueEdit, bTemp);
            setText(R.id.cValueEdit, cTemp);
            setText(R.id.beginPeriodEdit, periodBeginTemp);
            setText(R.id.endPeriodEdit, periodEndTemp);
        }
        catch(IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        catch(NoSuchFieldException ex) {
            Toast.makeText(this, "Bad file, can't find coefficients", Toast.LENGTH_SHORT).show();
        }
    }
}