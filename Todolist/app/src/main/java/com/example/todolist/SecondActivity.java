package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    private EditText text;
    private Button button;
    private SQLiteDatabase db;
    private static final String TABLE_NAME="todolist";

    private static final String ID="id";
    private static final String TASK="tasks";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);

        text=findViewById(R.id.Text);
        button=findViewById(R.id.addbtn);

        db=openOrCreateDatabase("Database", Context.MODE_PRIVATE,null);
        String table = "create table if not exists " + TABLE_NAME + " ("+ ID + " serial primary key, " + TASK + " varchar(20));";
        db.execSQL(table);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tasks = text.getText().toString();

                if (!TextUtils.isEmpty(tasks))
                {
                    String insert = "insert into " + TABLE_NAME + " (" + TASK + ") values('" + tasks + "');";
                    db.execSQL(insert);
                    Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                    intent.putExtra("Message",tasks);
                    setResult(RESULT_OK , intent);
                    finish();
                }
                else {
                    Toast.makeText(SecondActivity.this,"Enter Task",Toast.LENGTH_SHORT).show();
                }



            }
        });


    }
}