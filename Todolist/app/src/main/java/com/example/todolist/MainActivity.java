package com.example.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private static final int req_code = 10;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        listView = findViewById(R.id.listViewTasks);

        db = openOrCreateDatabase("TasksDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tasks(id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT)");

        arrayList = new ArrayList<>();
        loadTasksFromDatabase();

        adapter = new ArrayAdapter<>(this, R.layout.item_list, R.id.textViewTask, arrayList);
        listView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivityForResult(intent, req_code);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete Task");
                alert.setMessage("Are you sure you want to delete this Task?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTaskFromDatabase(arrayList.get(position));
                        arrayList.remove(position);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CheckBox checkBox = view.findViewById(R.id.checkBox);
                checkBox.toggle();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == req_code && resultCode == RESULT_OK && data != null) {
            String message = data.getStringExtra("Message");
            if (message != null && !message.isEmpty()) {
                arrayList.add(message);
                adapter.notifyDataSetChanged();
                insertTaskIntoDatabase(message);
            }
        }
    }

    private void loadTasksFromDatabase() {
        Cursor cursor = db.rawQuery("SELECT * FROM tasks", null);
        if (cursor.moveToFirst()) {
            do {
                String task = cursor.getString(1);
                arrayList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void insertTaskIntoDatabase(String task) {
        db.execSQL("INSERT INTO tasks(task) VALUES(?)", new String[]{task});
    }

    private void deleteTaskFromDatabase(String task) {
        db.execSQL("DELETE FROM tasks WHERE task=?", new String[]{task});
    }
}
