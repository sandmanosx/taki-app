package com.example.myapplication.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class OkClient {
    private OkHttpClient okHttpClient = new OkHttpClient();
    private String url, result = new String(),cookie="";
    public OkClient() {
        this.url = "http://106.12.203.34:8080/";
    }

    private void ResetUrl() {
        this.url = "http://106.12.203.34:8080/";
    }

    public String getResult() {
        return result;
    }
    public String getCookie() {
        if(cookie==null||cookie==""){
            return "cookie is empty";
        }
        return cookie;
    }

    //设置各种请求
    public void setMode(String mode){
        switch (mode) {
            case "movies":
                url += "movies";
                getMovies();
                ResetUrl();
                break;
            case "screenings":
                url += "screenings";
                getScreenings();
                ResetUrl();
                break;
            default:
                getMovies();
                ResetUrl();
                break;
        }
    }
    public void setMode(String mode,String id){
        url+=mode;
        url+="/"+id;
        Log.e("url",url);
        mode="withid";
        setMode(mode);
    }
    public void setMode(String mode, String username, String password) {
        switch (mode) {
            case "movies":
                setMode(mode);
                break;
            case "login":
                url += "login";
                login(username, password);
                ResetUrl();
                break;
            case "screenings":
                setMode(mode);
                break;
        }
    }



    private String readJ(Call call) throws IOException {
        Response response = call.execute();
        return response.body().string();
    }

    //登录模块
    public void login(String username, String password) {
        okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url, cookies);
                cookie = cookies.get(0).value();
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(HttpUrl.parse("http://106.12.203.34:8080/"));
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).build();
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        try{
            Response response = call.execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //尝试使用enqueue来进行异步处理，但好像我要的是同步处理，就此放弃
        /**call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("post","failed");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string();
                System.out.println("result is"+result);
            }
        });*/
    }
    //获取电影列表模块
    private void getMovies() {
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            result = readJ(call);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //这玩意好像和上面那个是同一个东西
    public void getScreenings(){
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            result = readJ(call);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    public void getMovieIdFromScreen(String screenId){
        okHttpClient = new OkHttpClient();
        setMode("movies",screenId);

    }*/

    //上传选座信息
    public void sendTicket(ArrayList<String> ticketsList,String cookie,String discountNumber) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        List<Integer> ticketsId = new ArrayList<>();
        for(int i=0;i<ticketsList.size();i++){
            String Sid = ticketsList.get(i);
            ticketsId.add(Integer.valueOf(Sid));
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, ticketsId.toString());

        Request confirm = new Request.Builder()
                .url(url+"orders/"+discountNumber)
                .header("cookie","JSESSIONID="+cookie)
                .addHeader("Content-Type","application/json")
                .post(body)
                .build();
        result = okHttpClient.newCall(confirm).execute().body().string();
        Log.e("result",result);
    }

    public void payOrder(String cookie) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request confirm = new Request.Builder()
                .url(url+"orders/pay")
                .header("cookie","JSESSIONID="+cookie)
                .put(body)
                .build();
        result = okHttpClient.newCall(confirm).execute().body().string();
    }

    public void cancelOrder(String cookie) throws IOException{
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request confirm = new Request.Builder()
                .url(url+"orders/cancel")
                .header("cookie","JSESSIONID="+cookie)
                .put(body)
                .build();
        result = okHttpClient.newCall(confirm).execute().body().string();
        Log.e("result cancel",result);
    }

    public void getOrders(String cookie) throws Exception{
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"users/orders")
                .header("cookie","JSESSIONID="+cookie)
                .build();
        result = okHttpClient.newCall(request).execute().body().string();
    }

    public Bitmap getImg(String name) throws Exception{
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+ "file/" +name)
                .build();
        ResponseBody body = okHttpClient.newCall(request).execute().body();
        InputStream in = body.byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(in);
        return bitmap;
    }

    public String getSearch(String keyword)throws Exception{
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"movies/search?q="+keyword)
                .build();
        result = okHttpClient.newCall(request).execute().body().string();
        return result;
    }
}
