
package com.example.appdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie_watchlist.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MOVIES = "Movies";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_GENRE = "genre";
    private static final String COL_STATUS = "status";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_MOVIES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT NOT NULL UNIQUE, " +
                COL_GENRE + " TEXT, " +
                COL_STATUS + " TEXT NOT NULL DEFAULT 'To Watch')";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        // Add database migration code here when needed.
    }

    // Add a movie
    public boolean addMovie(String title, String genre) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_GENRE, genre);
        values.put(COL_STATUS, "To Watch");

        long result = db.insert(TABLE_MOVIES, null, values);

        return result != -1;
    }

    // Display all movies with numbering starting from 1
    public String getAllMovies() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_MOVIES,
                new String[]{
                        COL_ID,
                        COL_TITLE,
                        COL_GENRE,
                        COL_STATUS
                },
                null,
                null,
                null,
                null,
                COL_TITLE + " COLLATE NOCASE ASC"
        );

        StringBuilder movies = new StringBuilder();

        try {
            int count = 1;

            while (cursor.moveToNext()) {
                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_TITLE));

                String genre = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_GENRE));

                String status = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_STATUS));

                // Use the current list position, not the database ID
                movies.append(count).append(". ")
                        .append(title)
                        .append("\nGenre: ")
                        .append(genre == null || genre.isEmpty()
                                ? "Not specified" : genre)
                        .append("\nStatus: ")
                        .append(status)
                        .append("\n\n");

                count++;
            }
        } finally {
            cursor.close();
        }

        if (movies.length() == 0) {
            return "Your watchlist is empty. Add a movie!";
        }

        return movies.toString();
    }

    // Mark a movie as watched using its title
    public boolean markWatched(String title) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_STATUS, "Watched");

        int rows = db.update(
                TABLE_MOVIES,
                values,
                COL_TITLE + " = ? COLLATE NOCASE",
                new String[]{title}
        );

        return rows > 0;
    }

    // Delete a movie using its title
    public boolean deleteMovie(String title) {
        SQLiteDatabase db = getWritableDatabase();

        int rows = db.delete(
                TABLE_MOVIES,
                COL_TITLE + " = ? COLLATE NOCASE",
                new String[]{title}
        );

        return rows > 0;
    }
}
