package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.OkClient;
import com.example.myapplication.data.ResolveJson;
import com.example.myapplication.data.QRCodeUtil;
import com.example.myapplication.ui.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CodeActivity extends AppCompatActivity {
    private Button c_button;
    private ArrayList<String > i = new ArrayList<>();
    private String cookie = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String recentOrderId = sharedPreferences.getString("recentOrderId","");
        String cookie = sharedPreferences.getString("cookie","");
        System.out.println("orderid"+recentOrderId+"\ncookie"+cookie);
        showOrder(recentOrderId,cookie);
//        String json = "";
//        String date = "no date available";
//        String time = "no time available";
//        String screen = "no screen available";
//        String name = "no name get";
//        String blurb="no blurb get";
//        String director="no director get";
//        String leadActors = "no leadActors get";
//        try{
//            json = getIntent().getStringExtra("json");
//            JSONArray jsonArray = new JSONArray(json);
//            JSONObject jsonObject = jsonArray.getJSONObject(0);
//            name = jsonObject.get("name").toString();
//            blurb = jsonObject.get("blurb").toString();
//            director = jsonObject.get("director").toString();
//            leadActors = jsonObject.get("leadActors").toString();
//            //获取排片
//            JSONArray screeninglist = (JSONArray)jsonObject.getJSONArray("screeningList");
//            JSONObject screening = screeninglist.getJSONObject(0);
//            date = screening.get("date").toString();
//            time = screening.get("time").toString();
//            screen = screening.get("cinemaScreen").toString();
//        }catch (Exception e){
//            Log.e("error in","get Extra");
//            e.printStackTrace();
//        }
        c_button=findViewById(R.id.book_btn0);
        c_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return
                Intent intent=new Intent(CodeActivity.this,FilmActivity.class);
                startActivity(intent);
            }
        });


        c_button=findViewById(R.id.book_btn1);
        c_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return
                Intent intent=new Intent(CodeActivity.this, LoginActivity.class);
                editor.remove("cookie");
                editor.commit();
                startActivity(intent);
            }
        });

        // 设置显示更改ui
//        TextView Mname = findViewById(R.id.fB_3);
//        Mname.setText(name);
//        TextView Mblurb = findViewById(R.id.bl_1);
//        Mblurb.setText(blurb);
//        Mblurb.append("...");
//
//        TextView Mdirt = findViewById(R.id.dir_1);
//        Mdirt.setText(director);
//
//        TextView MAct = findViewById(R.id.act_2);
//        MAct.setText(leadActors);
//
//        TextView MScreen = findViewById(R.id.screening_1);
//        MScreen.setText(date+"\n");
//        MScreen.append(time+"\n");
//        MScreen.append("cinemaScreen: "+screen);
    }

    //未完成
    private void showOrder(String recentOrderId,String cookie) {
        OkClient OkClient = new OkClient();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkClient.getOrders(cookie);
                    ResolveJson getOrderById = new ResolveJson(OkClient.getResult());
                    getOrderById.getOrdersById(recentOrderId);
                    String result = getOrderById.getResult();
                    String status = new JSONObject(result).getString("status")+"\n";
                    String price ="Total price is "+ new JSONObject(result).getString("totalCost");
                    String screeningId = new JSONArray(new JSONObject(result).getString("tickets")).getJSONObject(0).getString("screeningId");

                    /**杂乱无章的获取电影信息过程，后期会优化，大概*/
                    OkClient getMovieData = new OkClient();
                    getMovieData.setMode("screenings",screeningId);
                    String MovieId = new JSONObject(getMovieData.getResult()).getString("movieId");
                    getMovieData.setMode("movies",MovieId);
                    JSONObject movie = new JSONObject(getMovieData.getResult());

                    ImageView imageView = findViewById(R.id.img_qrcode);
                    TextView textView = findViewById(R.id.code_3),title = findViewById(R.id.fB_3),blurb = findViewById(R.id.bl_1);
                    Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(result,450,450);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(status);
                            textView.append(price);
                            imageView.setImageBitmap(bitmap);
                            try {
                                title.setText(movie.getString("name"));
                                blurb.setText(movie.getString("blurb"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }



}
