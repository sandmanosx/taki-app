package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.data.OkClient;
import com.example.myapplication.data.ResolveJson;

import org.json.JSONArray;
import org.json.JSONObject;



public class FilmActivity extends AppCompatActivity{

    private Button rButton,sButton,fButton;
    private EditText editText;
    //计划使用一个按钮的list来动态管理所有按钮
    private Button[] ButtonList;
    private LinearLayout[] resultList;
    private String screenData,movieData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        rButton = findViewById(R.id._return);
        sButton = findViewById(R.id.search_button);
        fButton = findViewById(R.id.refresh);

        final Bitmap[] bitmap = {null};

        //获取本地存储数据
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String cookie = sharedPreferences.getString("cookie", "");
        //初始化各种数据
        init();

        //搜索模块
        editText = findViewById(R.id.search_view);

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editText.getText().toString();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkClient okClient = new OkClient();
                        try {
                            JSONArray searchresult = new JSONArray(new ResolveJson(okClient.getSearch(keyword)).readSearch());
                            Intent intent = new Intent(FilmActivity.this,SearchResActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            }
        });

        fButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(FilmActivity.this,FilmActivity.class);
            startActivity(intent);
          }
        });

        rButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
        });
    }
    //初始化界面
    private void init(){
        Thread t = new Thread(new Runnable() {
          @Override
          public void run() {
            LinearLayout movielist = findViewById(R.id.movies_list);
            movieData=getMoviesData();
            try {
              JSONArray movies = new JSONArray(movieData);
              for(int index=0;index<movies.length();index++){
                JSONObject movie = movies.getJSONObject(index);
                runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                    try {
                      movielist.addView(setMovieLayout(movie.toString()));
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                  }
                });
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
        t.start();

    }
    private LinearLayout setMovieLayout(String data) throws Exception {
      JSONObject movieData = new JSONObject(data);

      String id = movieData.getString("id");
      String name = movieData.getString("name");
      String blurb = movieData.getString("blurb");
      String covername = movieData.getString("cover");
      String director = movieData.getString("director");
      String duration = movieData.getString("duration");

      //新建最外层layout
      LinearLayout layout = new LinearLayout(FilmActivity.this);

      //设置相关margin和padding
      int margin = dip2px(FilmActivity.this,10);
      int lpadding = dip2px(FilmActivity.this,1);
      int tpadding = dip2px(FilmActivity.this,8);


      LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,dip2px(FilmActivity.this,173));
      lLayoutlayoutParams.setMargins(margin,margin,margin,margin);
      layout.setLayoutParams(lLayoutlayoutParams);
      // 设置属性
      layout.setBackgroundColor(Color.parseColor("#000000"));	//
      layout.setPadding(lpadding,lpadding,lpadding,lpadding);
      layout.setOrientation(LinearLayout.HORIZONTAL);

      //以下为下载图片部分
      ImageView cover = new ImageView(FilmActivity.this);
      LinearLayout.LayoutParams imgViewParams = new LinearLayout.LayoutParams(dip2px(FilmActivity.this,108),ViewGroup.LayoutParams.MATCH_PARENT,4);
      cover.setLayoutParams(imgViewParams);
      cover.setImageResource(R.mipmap.ic_launcher_round);
      cover.setBackgroundColor(Color.parseColor("#CFA8AA"));
      if (covername != "null") {
        Thread m = new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              Bitmap bitmap = new OkClient().getImg(covername);
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  cover.setImageBitmap(bitmap);
                }
              });
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
        m.start();
      }

      //设置文本
      LinearLayout movieText = new LinearLayout(FilmActivity.this);
      LinearLayout.LayoutParams textBox = new LinearLayout.LayoutParams(dip2px(FilmActivity.this,265),ViewGroup.LayoutParams.MATCH_PARENT);
      movieText.setPadding(tpadding,tpadding,tpadding,tpadding);
      movieText.setOrientation(LinearLayout.VERTICAL);
      movieText.setBackgroundColor(Color.parseColor("#ffffff"));
      movieText.setGravity(Gravity.RIGHT);
      movieText.setLayoutParams(textBox);

      TextView context = new TextView(FilmActivity.this);
      LinearLayout.LayoutParams contextparams = new LinearLayout.LayoutParams(dip2px(FilmActivity.this,250),ViewGroup.LayoutParams.MATCH_PARENT,8);
      context.setBackgroundColor(Color.parseColor("#D6EFDD"));
      context.setGravity(Gravity.CENTER);
      context.setTextColor(Color.parseColor("#000000"));
      context.setTextSize(30);
      context.setLayoutParams(contextparams);
      context.setText(name+"\n"+blurb);

      movieText.addView(context);

      //把所有东西塞进layout中
      layout.addView(cover);
      layout.addView(movieText);

      layout.setClickable(true);
      layout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
          SharedPreferences.Editor editor = sharedPreferences.edit();
          editor.putString("movie",id);
          editor.commit();
          Intent intent = new Intent(FilmActivity.this,FilmBookActivity.class);
          startActivity(intent);
        }
      });
      return layout;
    }

    private String getMoviesData(){
      final String[] res = {""};
          try {
            OkClient OkClient = new OkClient();
            OkClient.setMode("movies");
            String result = OkClient.getResult();
            result = new JSONObject(result).getString("content");
            JSONArray array = new JSONArray(result);
            JSONArray resultArray = new JSONArray();
            res[0]=result;
          } catch (Exception e) {
            e.printStackTrace();
          }
      return res[0];
    }
  private int dip2px(Context context, float dipValue)
  {
    Resources r = context.getResources();
    return (int) TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
  }
}
