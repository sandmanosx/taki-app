package com.example.myapplication.data;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResolveJson {
    private String json = "";
    private JSONObject[] jsonObjectList;
    private JSONArray jsonArray;
    private String[] result = {};
    private List<String> output = new ArrayList<>();


    public ResolveJson(String json){
        this.json = json;
    }



    public String[] readScreeningsMovId(){
        try{
            jsonArray = new JSONArray(json);
            int i = 0;
            while(i<jsonArray.length()){
                result[i] = jsonArray.getJSONObject(i).get("movieId").toString();
                i++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public String[] readMoviesName(){

        return result;
    }


    public void readSingleMovieName() throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        String name = jsonObject.get("name").toString();
        output.add(name);
    }


    public void readSeats() throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = new JSONArray(jsonObject.get("tickets").toString());
        Log.e("size", String.valueOf(jsonArray.length()));
        int i =0;
        while(i<jsonArray.length()){
            JSONObject ticket = jsonArray.getJSONObject(i);
            output.add(ticket.toString());
            i++;
        }
    }
    public void readOrdersId() throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        String id = jsonObject.getString("id");
        output.add(id);
    }

    public void getOrdersById(String id)throws Exception{
        JSONArray orders = new JSONArray(json);
        JSONObject target = null;
        for(int i=0;i<orders.length();i++){
            JSONObject sample = orders.getJSONObject(i);
            Log.e("now sample is",sample.toString());
            if(sample.getString("id").equals(id)){
                target=sample;
                break;
            }
        }
        output.add(target.toString());
    }
    public void readSingleScreening(){

    }
    public String getImgName() throws Exception{
        JSONObject object = new JSONObject(json);
        String ImgName = object.getString("cover");
        return ImgName;
    }

    public String readSearch() throws Exception{
        JSONObject jsonObject = new JSONObject(json);
        String content = jsonObject.getString("content");
        return content;

    }
    public String getResult(){
        return output.get(0);
    }
    public List<String> getOutput(){
        return output;
    }
}
