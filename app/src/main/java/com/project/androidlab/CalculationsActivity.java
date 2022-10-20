package com.project.androidlab;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class CalculationsActivity extends AppCompatActivity {

    double aValue = 0.0, bValue = 0.0, cValue = 0.0, beginPeriod = 0.0, endPeriod = 0.0;
    FunctionType type = FunctionType.POWER;


    class ThreadImpl implements Runnable
    {
        ThreadImpl(Supplier<Double> func, int resultId, int timeId, int decimalPlaces)
        {
            this.func = func;
            this.resultId = resultId;
            this.timeId = timeId;
            this.decimalPlaces = decimalPlaces;
        }
        Supplier<Double> func;
        int resultId, timeId;
        int decimalPlaces;
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run()
        {
            long startTime = System.nanoTime();
            double result = func.get();
            long endTime = System.nanoTime();
            double time = (endTime - startTime) / 1000000.0; //ms

            double coeff = Math.pow(10, decimalPlaces);
            TextView resView = findViewById(resultId);
            result = (int)(result * coeff) / coeff;
            TextView timeView = findViewById(timeId);
            class ViewSetter implements  Runnable
            {
                TextView resView, timeView;
                double value, timeValue;
                ViewSetter(TextView resView, TextView timeView, double value, double timeValue)
                {
                    this.resView = resView;
                    this.timeView = timeView;
                    this.timeValue = timeValue;
                    this.value = value;
                }
                public void run() {
                    resView.setText(Double.toString(value));
                    timeView.setText(Double.toString(timeValue));
                }
            }
            ViewSetter setter = new ViewSetter(resView, timeView, result, time);
            runOnUiThread(setter);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculations_activiry);
        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
            return;
        }
        aValue = extras.getDouble(Globals.COEFFICIENT_A);
        bValue = extras.getDouble(Globals.COEFFICIENT_B);
        cValue = extras.getDouble(Globals.COEFFICIENT_C);
        beginPeriod = extras.getDouble(Globals.BEGIN_PERIOD);
        endPeriod = extras.getDouble(Globals.END_PERIOD);
        type = (FunctionType) extras.get(Globals.FUNCTION_TYPE_NAME);

        String func = getFunction(type);
        func = func.replace("a", Double.toString(aValue));
        func = func.replace("b", Double.toString(bValue));
        func = func.replace("c", Double.toString(cValue));

        TextView functionLabelView = findViewById(R.id.functionLabelView);
        functionLabelView.setText(func);

        Function<Double, Double> integratingFunction;
        if (type == FunctionType.EXPOTENTIAL) {
            integratingFunction = x -> aValue * Math.pow(bValue, x) + cValue;
        }
        else {
            integratingFunction = x -> aValue * Math.pow(x, bValue) + cValue;
        }

        TextView fromValue = findViewById(R.id.fromValue);
        fromValue.setText(Double.toString(beginPeriod));
        TextView toValue = findViewById(R.id.toValue);
        toValue.setText(Double.toString(endPeriod));

        ArrayList<Entry> functionDots = new ArrayList<>();
        int dotsCount = 100;
        double step = (endPeriod - beginPeriod) / (double)dotsCount;
        for (int i = 0; i < dotsCount; i++)
        {
            double dot = beginPeriod + step * i;
            functionDots.add(new Entry((float)dot, integratingFunction.apply(dot).floatValue()));
        }
        LineChart chart = findViewById(R.id.lineChart);
        LineDataSet set = new LineDataSet(functionDots, "graph");
        LineData values = new LineData(set);
        chart.setData(values);

        int decimalPlaces = 5;
        double precision = 1.0 / Math.pow(10, decimalPlaces);

        Supplier<Double> rectangleSupplier = ()->rectangleMethod(beginPeriod, endPeriod, integratingFunction, precision);
        ThreadImpl rectangleImpl = new ThreadImpl(rectangleSupplier,
                R.id.rectangleResult, R.id.rectangleTimeResult, decimalPlaces);
        Thread rectangleThread = new Thread(rectangleImpl);

        Supplier<Double> trapezoidalSupplier = ()->trapezoidalMethod(beginPeriod, endPeriod, integratingFunction, precision);
        ThreadImpl trapezoidalImpl = new ThreadImpl(trapezoidalSupplier,
                R.id.trapezoidalResult, R.id.trapezoidalTimeResult, decimalPlaces);
        Thread trapezoidalThread = new Thread(trapezoidalImpl);

        Supplier<Double> simpsonSupplier = ()->simpsonMethod(beginPeriod, endPeriod, integratingFunction, precision);
        ThreadImpl simpsonImpl = new ThreadImpl(simpsonSupplier,
                R.id.simpsonResult, R.id.simpsonTimeResult, decimalPlaces);
        Thread simpsonThread = new Thread(simpsonImpl);

        rectangleThread.start();
        trapezoidalThread.start();
        simpsonThread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double rectangleMethod(double leftBound, double rightBound, Function<Double, Double> func, double precision) {
        double prev = func.apply(rightBound);
        for (long n = 2;; n *= 2) {
            double accumulator = 0;
            double accuracy = 0;
            double h = (rightBound - leftBound) / n;
            for (int i = 0; i < n; ++i) {
                accumulator += func.apply(leftBound + i*h);
            }
            accumulator *= h;
            accuracy = Math.abs(accumulator - prev);
            prev = accumulator;
            if (accuracy < precision)
                return accumulator;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double trapezoidalMethod(double left, double right, Function<Double, Double> func, double precision)
    {
        double prev = 0.5 * (func.apply(left) + func.apply(right));
        for (long n = 2;; n *= 2) {

            double delta = (right - left) / n;              // step size
            double accumulator = 0.5 * (func.apply(left) + func.apply(right));    // area
            for (int i = 1; i < n; i++) {
                double x = left + delta * i;
                accumulator = accumulator + func.apply(x);
            }
            accumulator *= delta;
            double accuracy = Math.abs(accumulator - prev);
            prev = accumulator;
            if (accuracy < precision)
                return accumulator;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static double simpsonIteration(double left, double right, Function<Double, Double> func, long n)
    {
        double delta = (right - left) / n;              // step size
        double accumulator = 1.0 / 3.0 * (func.apply(left) + func.apply(right));    // area

        // 4/3 terms
        for (int i = 1; i < n - 1; i += 2) {
            double x = left + delta * i;
            accumulator += 4.0 / 3.0 * func.apply(x);
        }

        // 2/3 terms
        for (int i = 2; i < n - 1; i += 2) {
            double x = left + delta * i;
            accumulator += 2.0 / 3.0 * func.apply(x);
        }

        accumulator *= delta;
        return accumulator;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double simpsonMethod(double left, double right, Function<Double, Double> func, double precision)
    {
        double prev = simpsonIteration(left, right, func, 2);
        for (long n = 4;; n *= 2) {
            double accumulator = simpsonIteration(left, right, func, n);
            double accuracy = Math.abs(accumulator - prev);
            prev = accumulator;
            if (accuracy < precision)
                return accumulator;
        }
    }

    private String getFunction(FunctionType type)
    {
        int stringId = R.string.expFunc;
        if (type == FunctionType.POWER)
        {
            stringId = R.string.powerFunc;
        }
        return getString(stringId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void goBack(View view)
    {
        Intent intent = new Intent(this, CoefficientsActivity.class);
        intent.putExtra(Globals.FUNCTION_TYPE_NAME, type);
        intent.putExtra(Globals.COEFFICIENT_A, aValue);
        intent.putExtra(Globals.COEFFICIENT_B, bValue);
        intent.putExtra(Globals.COEFFICIENT_C, cValue);
        intent.putExtra(Globals.BEGIN_PERIOD, beginPeriod);
        intent.putExtra(Globals.END_PERIOD, endPeriod);
        startActivity(intent);
    }

    public void goToBeginning(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }
}