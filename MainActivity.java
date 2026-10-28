
package com.example.appdb;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etTitle, etGenre;
    TextView tMovies;
    DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        etTitle = findViewById(R.id.etTitle);
        etGenre = findViewById(R.id.etGenre);
        tMovies = findViewById(R.id.tMovies);

        dbhelper = new DatabaseHelper(this);

        show(null);
    }

    // Add a movie
    public void addMovie(View view) {
        String title = etTitle.getText().toString().trim();
        String genre = etGenre.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Enter movie title");
            return;
        }

        if (dbhelper.addMovie(title, genre)) {
            Toast.makeText(this, "Movie added!",
                    Toast.LENGTH_SHORT).show();

            etTitle.setText("");
            etGenre.setText("");
            show(null);
        } else {
            Toast.makeText(this,
                    "Could not add movie. It may already exist.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Display movies with sequential numbering
    public void show(View view) {
        String movies = dbhelper.getAllMovies();

        if (movies == null || movies.trim().isEmpty()) {
            tMovies.setText("No movies found.");
        } else {
            tMovies.setText(movies);
        }
    }

    // Mark a movie as watched
    public void markWatched(View view) {
        String title = etTitle.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Enter the movie title");
            return;
        }

        if (dbhelper.markWatched(title)) {
            Toast.makeText(this, "Marked as watched!",
                    Toast.LENGTH_SHORT).show();

            etTitle.setText("");
            show(null);
        } else {
            Toast.makeText(this, "Movie not found",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Delete a movie
    public void deleteMovie(View view) {
        String title = etTitle.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Enter the movie title to delete");
            return;
        }

        if (dbhelper.deleteMovie(title)) {
            Toast.makeText(this, "Movie deleted!",
                    Toast.LENGTH_SHORT).show();

            etTitle.setText("");
            show(null);
        } else {
            Toast.makeText(this, "Movie not found",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (dbhelper != null) {
            dbhelper.close();
        }
        super.onDestroy();
    }
}
