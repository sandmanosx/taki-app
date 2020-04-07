package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.data.OkClient;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class FilmBookActivity extends AppCompatActivity {
    private Button b_button, c_button;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_book);
        imageView = findViewById(R.id.fB_1);
        int empty1 = 1, empty2 = 1;
        String json = "";
        String id1 = "", id2 = "";
        String date = "no date available";
        String time = "no time available";
        String screen = "no screen available";
        String name = "no name get";
        String blurb = "no blurb get";
        String director = "no director get";
        String leadActors = "no leadActors get";
        String coverName = null;
        String date1 = "no date available";
        String time1 = "no time available";
        String screen1 = "no screen available";
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            json = getIntent().getStringExtra("json");
            JSONObject jsonObject = new JSONObject(json);
            name = jsonObject.get("name").toString();
            blurb = jsonObject.get("blurb").toString();
            director = jsonObject.get("director").toString();
            leadActors = jsonObject.get("leadActors").toString();
            coverName = jsonObject.getString("cover");
            //获取排片
            JSONArray screeninglist = (JSONArray) jsonObject.getJSONArray("screenings");
            JSONObject screening = screeninglist.getJSONObject(0);
            date = screening.get("date").toString();
            time = screening.get("time").toString();
            screen = screening.get("auditoriumId").toString();
            id1 = screening.get("id").toString();
            JSONArray a1 = new JSONArray(screening.getString("tickets"));
            if (a1.isNull(0)) {
                empty1 = 0;
            }

            JSONObject screening2 = screeninglist.getJSONObject(1);
            date1 = screening2.get("date").toString();
            time1 = screening2.get("time").toString();
            screen1 = screening2.get("auditoriumId").toString();
            id2 = screening2.get("id").toString();
            a1 = new JSONArray(screening2.getString("tickets"));
            if (a1.isNull(0)) {
                empty2 = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String finalCoverName = coverName;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (finalCoverName != null) {
                        Bitmap bitmap = new OkClient().getImg(finalCoverName);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        String finalId = id1, finalId1 = id2;
        b_button = findViewById(R.id.go_book_button0);
        int finalEmpty = empty1;
        b_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jump to films
                Intent intent = new Intent(FilmBookActivity.this, FilmBookDetailActivity.class);
                intent.putExtra("screenId", finalId);
                if (finalEmpty == 1) {
                    editor.putString("screenId", finalId);
                    editor.commit();
                    startActivity(intent);
                } else {
                    Toast.makeText(FilmBookActivity.this, "No tickets available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        c_button = findViewById(R.id.go_book_button1);
        int finalEmpty1 = empty2;
        c_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilmBookActivity.this, FilmBookDetailActivity.class);
                intent.putExtra("screenId", finalId1);
                if (finalEmpty1 == 1) {
                    editor.putString("screenId", finalId1);
                    editor.commit();
                    startActivity(intent);
                } else {
                    Toast.makeText(FilmBookActivity.this, "No tickets available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b_button = findViewById(R.id.book_btn0);
        b_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return
                Intent intent = new Intent(FilmBookActivity.this, FilmActivity.class);
                startActivity(intent);
            }
        });


        b_button = findViewById(R.id.book_btn1);
        b_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return
                Intent intent = new Intent(FilmBookActivity.this, FilmBookActivity.class);
                startActivity(intent);
            }
        });

        // 设置显示更改ui
        TextView Mname = findViewById(R.id.fB_3);
        Mname.setText(name);
        TextView Mblurb = findViewById(R.id.bl_1);
        Mblurb.setText(blurb);
        Mblurb.append("...");

        TextView Mdirt = findViewById(R.id.dir_1);
        Mdirt.setText(director);

        TextView MAct = findViewById(R.id.act_2);
        MAct.setText(leadActors);

        TextView MScreen = findViewById(R.id.screening_1);
        MScreen.setText(date + "\n");
        MScreen.append(time + "\n");
        MScreen.append("cinemaScreen: " + screen);

        TextView MScreen1 = findViewById(R.id.screening_2);
        MScreen1.setText(date1 + "\n");
        MScreen1.append(time1 + "\n");
        MScreen1.append("cinemaScreen: " + screen1);
    }
}

