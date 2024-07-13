package com.example.quotes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageButton refreshbtn, sharebtn, addfav, viewfav;
    private SQLiteDatabase db;
    private final static int req_code = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView3);
        refreshbtn=findViewById(R.id.refreshbtn);
        sharebtn=findViewById(R.id.sharebtn);
        addfav=findViewById(R.id.addfav);

        db=openOrCreateDatabase("Quotes", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS quote(id INTEGER PRIMARY KEY AUTOINCREMENT, quotes TEXT)");

        insertquote("I believe that we are fundamentally the same and have the same basic potential.");
        insertquote("Love is the master key that opens the gates of happiness.");
        insertquote("You spend a good piece of your life gripping a baseball and in the end it turns out that it was the other way around all the time.");
        insertquote("Being entirely honest with oneself is a good exercise.");
        insertquote("What matters is the value we've created in our lives, the people we've made happy and how much we've grown as people.");
        insertquote("Technological progress has merely provided us with more efficient means for going backwards.");
        insertquote("Kindness in words creates confidence. Kindness in thinking creates profoundness. Kindness in giving creates love.");
        insertquote("What worries you masters you.");
        insertquote("Wherever a man turns he can find someone who needs him.");
        insertquote("The truth is not for all men, but only for those who seek it.");
        insertquote("The world needs dreamers and the world needs doers. But above all, the world needs dreamers who do.");
        insertquote("Happiness is enhanced by others, but does not depend upon others.");
        insertquote("Decide on what you think is right, and stick to it.");
        insertquote("We have been taught to believe that negative equals realistic and positive equals unrealistic.");
        insertquote("The best proof of love is trust.");
        insertquote("Most true happiness comes from one's inner life, from the disposition of the mind and soul. Admittedly, a good inner life is hard to achieve, especially in these trying times. It takes reflection and contemplation and self-discipline.");
        insertquote("When you get right down to the root of the meaning of the word 'succeed,' you find that it simply means to follow through.");
        insertquote("Happiness comes when you least expect it and rarely when you try to make it happen.");
        insertquote("Your incredible brain can take you from rags to riches, from loneliness to popularity and from depression to happiness and joy if you use it properly.");


        Cursor cursor = db.rawQuery("SELECT quotes FROM quote ORDER BY RANDOM() LIMIT 1", null);
        if (cursor.moveToFirst()) {
            do {
                String quotestr = cursor.getString(0);
                textView.setText(quotestr);
            } while (cursor.moveToNext());
        }
        cursor.close();



        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = db.rawQuery("SELECT quotes FROM quote ORDER BY RANDOM()", null);
                if (cursor.moveToFirst()) {
                    do {
                        String quotestr1 = cursor.getString(0);
                        textView.setText(quotestr1);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

        addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Quote added", Toast.LENGTH_SHORT).show();
                String fav = textView.getText().toString();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("Message", fav);
                startActivity(intent);
            }
        });

        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sharequote = textView.getText().toString();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, sharequote);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share quotes via"));
            }
        });


    }

    private  void insertquote(String quotes){
        String sql = "INSERT INTO quote(quotes) values(?)";
        db.execSQL(sql,new Object[]{quotes});
    }

}