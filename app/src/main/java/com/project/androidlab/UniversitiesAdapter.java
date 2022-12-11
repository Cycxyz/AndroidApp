package com.project.androidlab;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.project.androidlab.db.University;

import java.util.ArrayList;
import java.util.List;

public class UniversitiesAdapter extends RecyclerView.Adapter<UniversitiesAdapter.UniversityViewHolder> {

    private List<University> universities = new ArrayList<>();
    private List<String> universitiesNames = new ArrayList<>();
    private boolean showOnlyNames;

    public void setUniversities(List<University> universities) {
        this.universities = universities;
        showOnlyNames = false;
        notifyDataSetChanged();
    }

    public void setUniversitiesNames(List<String> universitiesNames) {
        this.universitiesNames = universitiesNames;
        showOnlyNames = true;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UniversityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_university, parent, false);
        return new UniversityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversityViewHolder holder, int position) {
        if (showOnlyNames) {
            if (!universitiesNames.isEmpty()) {
                holder.setUniversityNameData(universitiesNames.get(position));
            }
        } else {
            if (!universities.isEmpty()) {
                holder.setUniversityData(universities.get(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (showOnlyNames) {
            return universitiesNames.size();
        }
        return universities.size();
    }

    class UniversityViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, cityTextView, studentsNumberTextView, ratingTextView;

        public UniversityViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            cityTextView = itemView.findViewById(R.id.city);
            studentsNumberTextView = itemView.findViewById(R.id.students_number);
            ratingTextView = itemView.findViewById(R.id.rating);
        }

        public void setUniversityData(University university) {
            nameTextView.setText("Назва: " + university.getUniversityName());

            cityTextView.setVisibility(View.VISIBLE);
            cityTextView.setText("Місто: " + university.getCity());

            studentsNumberTextView.setVisibility(View.VISIBLE);
            studentsNumberTextView.setText("Кількість студентів: " + String.valueOf(university.getStudentsNumber()));

            ratingTextView.setVisibility(View.VISIBLE);
            ratingTextView.setText("Рейтинг Webometrics: " + String.valueOf(university.getRating()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setMessage("Показати маршрут?")
                            .setPositiveButton("Так", (dialogInterface, i) -> {
                                String city = university.getCity();
                                Uri mapsIntentUri = Uri.parse(String.format("https://www.google.com/maps/dir/?api=1&destination=%s&travelmode=driving", city));
                                Intent mapsIntent = new Intent(Intent.ACTION_VIEW, mapsIntentUri);
                                mapsIntent.setPackage("com.google.android.apps.maps");
                                itemView.getContext().startActivity(mapsIntent);
                            })
                            .setNegativeButton("Скасувати", ((dialogInterface, i) -> {}))
                            .create()
                            .show();
                }
            });
        }

        public void setUniversityNameData(String name) {
            nameTextView.setText(name);

            cityTextView.setVisibility(View.GONE);
            studentsNumberTextView.setVisibility(View.GONE);
            ratingTextView.setVisibility(View.GONE);

            itemView.setOnClickListener(null);
        }
    }
}
