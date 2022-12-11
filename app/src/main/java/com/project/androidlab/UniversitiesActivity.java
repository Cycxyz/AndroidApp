package com.project.androidlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;

import com.project.androidlab.db.Constants;
import com.project.androidlab.db.DbHelper;
import com.project.androidlab.db.University;

public class UniversitiesActivity extends AppCompatActivity {

    CheckBox showNamesSelectionByVariantCheckBox;
    CheckBox showMinAndMaxRatingValueCheckBox;
    RecyclerView universitiesRecyclerView;

    LinearLayout maxRatingLinearLayout;
    LinearLayout minRatingLinearLayout;
    TextView maxRatingTextView;
    TextView minRatingTextView;

    UniversitiesAdapter adapter = new UniversitiesAdapter();

    DbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universities);
        showNamesSelectionByVariantCheckBox = findViewById(R.id.show_names_selection_by_variant_check_box);
        showMinAndMaxRatingValueCheckBox = findViewById(R.id.show_min_and_max_rating_value_check_box);
        universitiesRecyclerView = findViewById(R.id.universities_recycler_view);

        maxRatingLinearLayout = findViewById(R.id.max_rating_linear_layout);
        minRatingLinearLayout = findViewById(R.id.min_rating_linear_layout);
        maxRatingTextView = findViewById(R.id.max_rating_text_view);
        minRatingTextView = findViewById(R.id.min_rating_text_view);

        dbHelper = new DbHelper(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(universitiesRecyclerView.getContext(), layoutManager.getOrientation());
        universitiesRecyclerView.setAdapter(adapter);
        universitiesRecyclerView.setLayoutManager(layoutManager);
        universitiesRecyclerView.addItemDecoration(dividerItemDecoration);

        showNamesSelectionByVariantCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    getUniversitiesNamesByVariantCondition();
                } else {
                    getAllUniversities();
                }
            }
        });

        showMinAndMaxRatingValueCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    maxRatingLinearLayout.setVisibility(View.VISIBLE);
                    minRatingLinearLayout.setVisibility(View.VISIBLE);
                    getMaxAndMinRating();
                } else {
                    maxRatingLinearLayout.setVisibility(View.GONE);
                    minRatingLinearLayout.setVisibility(View.GONE);
                }
            }
        });

        getAllUniversities();
    }

    private void getAllUniversities() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<University> universities = dbHelper.getAllUniversities();
                runOnUiThread(() -> {
                    adapter.setUniversities(universities);
                });
            }
        }).start();
    }

    private void getUniversitiesNamesByVariantCondition() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> universitiesNames = dbHelper.getUniversitiesNamesByVariantCondition();
                runOnUiThread(() -> {
                    adapter.setUniversitiesNames(universitiesNames);
                });
            }
        }).start();
    }

    private void getMaxAndMinRating() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Integer> maxAndMinRating = dbHelper.getMaxAndMinRating();
                runOnUiThread(() -> {
                    if (maxAndMinRating.containsKey(Constants.MAX_RATING_KEY)) {
                        maxRatingTextView.setText(String.valueOf(maxAndMinRating.get(Constants.MAX_RATING_KEY)));
                    }
                    if (maxAndMinRating.containsKey(Constants.MIN_RATING_KEY)) {
                        minRatingTextView.setText(String.valueOf(maxAndMinRating.get(Constants.MIN_RATING_KEY)));
                    }
                });
            }
        }).start();
    }

    public void goBack(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }
}