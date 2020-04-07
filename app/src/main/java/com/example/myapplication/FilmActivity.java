package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.data.OkClient;
import com.example.myapplication.ui.login.LoginActivity;
import com.example.myapplication.data.ResolveJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FilmActivity extends AppCompatActivity{

    private static Object lockObject = new Object();
    private Button rButton,sButton;
    private ImageView imgview,imageView2,imageView3,imageView4;
    private SearchView searchView;
    private EditText editText;
    //计划使用一个按钮的list来动态管理所有按钮
    private Button[] ButtonList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        rButton = findViewById(R.id._return);
        sButton = findViewById(R.id.search_button);
        String json = "";
        String name = "none",anotherName="none";
        final Bitmap[] bitmap = {null};
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String cookie = sharedPreferences.getString("cookie", "");
        //解析数据
        try {
            json = getScreenings();
            JSONArray array = new JSONArray(json);
            JSONObject object = array.getJSONObject(0);

            Log.e("content",object.get("movieId").toString());
            json = getMovie(object.get("movieId").toString());

            ResolveJson resolveJson = new ResolveJson(json);
            resolveJson.readSingleMovieName();
            name = resolveJson.getResult();
            String ImgName = resolveJson.getImgName();
            resolveJson = new ResolveJson(getMovie(array.getJSONObject(1).getString("movieId")));
            resolveJson.readSingleMovieName();
            anotherName = resolveJson.getResult();
            Log.e("imgname",ImgName);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    OkClient okClient = new OkClient();
                    try {
                        bitmap[0] =okClient.getImg(ImgName);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imgview.setImageBitmap(bitmap[0]);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


        TextView view = findViewById(R.id.tv);
        view.setText(name);
        TextView view2 = findViewById(R.id.tv2);
        view2.setText(anotherName);
        final String finalJson = json;
        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return
                Intent intent = new Intent(FilmActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        rButton = findViewById(R.id.refresh);
        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this
                Intent intent = new Intent(FilmActivity.this, FilmActivity.class);
                startActivity(intent);
            }
        });


        imgview = findViewById(R.id.fB0);
        imgview.setClickable(true);
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this
                Intent intent = new Intent(FilmActivity.this, FilmBookActivity.class);
                intent.putExtra("json", finalJson);
                startActivity(intent);
            }
        });

        imageView2 = findViewById(R.id.fB1);
        imageView2.setClickable(false);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this
                Intent intent = new Intent(FilmActivity.this, FilmBookActivity.class);
                startActivity(intent);
            }
        });

        imageView3 = findViewById(R.id.fB2);
        imageView3.setClickable(false);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this
                Intent intent = new Intent(FilmActivity.this, FilmBookActivity.class);
                startActivity(intent);
            }
        });

        imageView4 = findViewById(R.id.fB3);
        imageView4.setClickable(false);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this
                Intent intent = new Intent(FilmActivity.this, FilmBookActivity.class);
                startActivity(intent);
            }
        });

        rButton = findViewById(R.id.fB4);
        rButton.setClickable(false);
        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this
                Intent intent = new Intent(FilmActivity.this, FilmBookActivity.class);
                startActivity(intent);
            }
        });

        //搜索模块
        editText = findViewById(R.id.search_view);

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editText.getText().toString();
                Log.e("content",keyword);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkClient okClient = new OkClient();
                        view2.setText("");
                        try {
                            JSONArray searchresult = new JSONArray(new ResolveJson(okClient.getSearch(keyword)).readSearch());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                view.setText(searchresult.getJSONObject(0).getString("name"));
                                                imgview.setImageBitmap(new OkClient().getImg(searchresult.getJSONObject(0).getString("cover")));
                                                if(searchresult.length()>1){
                                                    view2.setText(searchresult.getJSONObject(1).getString("name"));
                                                    if(searchresult.getJSONObject(1).getString("cover")!=null){
                                                        imageView2.setImageBitmap(new OkClient().getImg(searchresult.getJSONObject(1).getString("cover")));
                                                    }
                                                }
                                            } catch (Exception e) {
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
        });
    }


    //获取数据的函数，其实主要是把电影名字get到就行
    private String getScreenings() {
        final String[] res = {""};
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkClient OkClient = new OkClient();
                    OkClient.setMode("screenings");
                    String result = OkClient.getResult();
                    JSONArray array = new JSONArray(result);
                    String movieId = "";
                    JSONArray resultArray = new JSONArray();
                    for(int i=0;i<array.length();i++){
                        JSONObject screen = array.getJSONObject(i);
                        Log.e("screen",screen.toString());
                        Log.i("here","pause");
                        if(!screen.getString("movieId").equals(movieId)){
                            movieId = screen.getString("movieId");
                            resultArray.put(screen);
                            Log.e("res[0]",resultArray.toString()+" "+screen.getString("movieId"));
                        }
                    }
                    res[0]=resultArray.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            //等待链接进程结束
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res[0];
    }
    private String getMovie(String id) {
        final String[] res = {"movie"};
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkClient OkClient = new OkClient();
                    OkClient.setMode("movies", id);
                    String result = OkClient.getResult();
                    res[0] = result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            //等待链接进程结束
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res[0];
    }
}
