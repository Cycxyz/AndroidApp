package com.project.androidlab;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Optional;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CoefficientsActivity extends AppCompatActivity {
    Optional<FunctionType> cachedType;

    private String getFunctionName()
    {
        int stringId = R.string.expFunc;
        if (cachedType.isPresent() && cachedType.get() == FunctionType.POWER)
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
            cachedType = Optional.of((FunctionType) extras.get(Globals.FUNCTION_TYPE_NAME));
        }
        setFunctionLabel(getFunctionName());
    }
}