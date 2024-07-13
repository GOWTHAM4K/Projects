package com.example.quotes;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private ListView favListView;
    private ArrayList<String> favList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        favListView = findViewById(R.id.listview);
        favList = new ArrayList<>();

        db = openOrCreateDatabase("Quotes", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS favorites(id INTEGER PRIMARY KEY AUTOINCREMENT, quotes TEXT)");

        String favoriteQuote = getIntent().getStringExtra("Message");

        if (favoriteQuote != null) {
            insertFavorite(favoriteQuote);
        }

        loadFavorites();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favList);
        favListView.setAdapter(adapter);

        favListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SecondActivity.this);
                alert.setTitle("Delete Quote");
                alert.setMessage("Are you sure you want to delete this quote?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteFavorites(favList.get(position));
                        favList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.create().show();
                return true;
            }
        });

    }

    private void insertFavorite(String quote) {
        String sql = "INSERT INTO favorites(quotes) VALUES(?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, quote);
        statement.execute();
        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
    }

    private void loadFavorites() {
        Cursor cursor = db.rawQuery("SELECT quotes FROM favorites", null);
        if (cursor.moveToFirst()) {
            do {
                String quote = cursor.getString(0);
                favList.add(quote);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void deleteFavorites(String fav) {
        db.execSQL("DELETE FROM favorites WHERE quotes=?", new String[]{fav});
    }
}
