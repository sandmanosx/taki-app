package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigDecimal;

import com.example.myapplication.data.OkClient;
import com.example.myapplication.data.ResolveJson;
import com.example.myapplication.ui.login.LoginActivity;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilmBookDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bookButton,freshButton;
    private double TotalPrice;
    private String cookie = null;
    private HashMap<Integer, JSONObject> TikToBut = new HashMap<>();
    public static int x, c;
    public static final int MAX_GRID = 56;
    public int[] count = new int[MAX_GRID];
    private ArrayList<String> ticketsList = new ArrayList<>();
    public static int[] colors = new int[]{R.color.cl_seat_unpressed, R.color.cl_seat_pressed};
    Button sb1a, sb1b, sb1c, sb1d, sb1e, sb1f, sb1g, sb1h, sb2a, sb2b, sb2c, sb2d, sb2e, sb2f, sb2g, sb2h, sb3a, sb3b, sb3c, sb3d, sb3e, sb3f, sb3g, sb3h, sb4a, sb4b, sb4c, sb4d, sb4e, sb4f, sb4g, sb4h, sb5a, sb5b, sb5c, sb5d, sb5e, sb5f, sb5g, sb5h, sb6a, sb6b, sb6c, sb6d, sb6e, sb6f, sb6g, sb6h, sb7a, sb7b, sb7c, sb7d, sb7e, sb7f, sb7g, sb7h;


    public static Integer[] idB = new Integer[]{R.id.B1a, R.id.B1b, R.id.B1c, R.id.B1d, R.id.B1e, R.id.B1f, R.id.B1g, R.id.B1h,
            R.id.B2a, R.id.B2b, R.id.B2c, R.id.B2d, R.id.B2e, R.id.B2f, R.id.B2g, R.id.B2h,
            R.id.B3a, R.id.B3b, R.id.B3c, R.id.B3d, R.id.B3e, R.id.B3f, R.id.B3g, R.id.B3h,
            R.id.B4a, R.id.B4b, R.id.B4c, R.id.B4d, R.id.B4e, R.id.B4f, R.id.B4g, R.id.B4h,
            R.id.B5a, R.id.B5b, R.id.B5c, R.id.B5d, R.id.B5e, R.id.B5f, R.id.B5g, R.id.B5h,
            R.id.B6a, R.id.B6b, R.id.B6c, R.id.B6d, R.id.B6e, R.id.B6f, R.id.B6g, R.id.B6h,
            R.id.B7a, R.id.B7b, R.id.B7c, R.id.B7d, R.id.B7e, R.id.B7f, R.id.B7g, R.id.B7h};

    Button[] sButtons = new Button[]{sb1a, sb1b, sb1c, sb1d, sb1e, sb1f, sb1g, sb1h
            , sb2a, sb2b, sb2c, sb2d, sb2e, sb2f, sb2g, sb2h
            , sb3a, sb3b, sb3c, sb3d, sb3e, sb3f, sb3g, sb3h
            , sb4a, sb4b, sb4c, sb4d, sb4e, sb4f, sb4g, sb4h
            , sb5a, sb5b, sb5c, sb5d, sb5e, sb5f, sb5g, sb5h
            , sb6a, sb6b, sb6c, sb6d, sb6e, sb6f, sb6g, sb6h
            , sb7a, sb7b, sb7c, sb7d, sb7e, sb7f, sb7g, sb7h};
    TextView showPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_book_detail);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");
        bookButton = findViewById(R.id.book_Dbutton0);
        showPrice = findViewById(R.id.price_total);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilmBookDetailActivity.this, FilmActivity.class);
                SharedPreferences.Editor editor=sharedPreferences.edit();

                /**如果你跳不到订单界面进行注销工作，点return也可以进行注销*/
                editor.remove("cookie");
                editor.commit();
                startActivity(intent);
            }
        });
        freshButton = findViewById(R.id.book_refresh);
        freshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilmBookDetailActivity.this,FilmBookDetailActivity.class);
                startActivity(intent);
                finish();
            }
        });
        for (x = 0; x < MAX_GRID; x++) {
            sButtons[x] = findViewById(idB[x]);
        }
        for (x = 0; x < MAX_GRID; x++) {
            count[x] = 0;
        }

        for (x = 0; x < MAX_GRID; x++) {
            sButtons[x].setOnClickListener(this);
        }

//            bookButton = findViewById(R.id.book_confirm);
//            bookButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    for(x=0,c=0;x<MAX_GRID;x++){
//                    c=c+count[x];
//                }
//                    if(c>0) {
//                        Intent intent = new Intent(FilmBookDetailActivity.this, CodeActivity.class);
//                        startActivity(intent);
//                    }
//
//                }
//            });
        getSeatData();
    }

    @Override
    public void onClick(View v) {
        for (x = 0; x < MAX_GRID; x++) {
            if (v.getId() == idB[x]) break;
        }
        String id = "";
        try {
            id = TikToBut.get(idB[x]).getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count[x] % 2 == 1) {
            sButtons[x].setBackgroundDrawable(getResources().getDrawable(R.drawable.seat_free));
            ticketsList.remove(id);
            try {
                TotalPrice -= Double.valueOf(TikToBut.get(idB[x]).getString("originalPrice"));
                Log.e("price", String.valueOf(TotalPrice));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            count[x]--;
        } else {
            Log.e("count x ", String.valueOf(x) + " " + String.valueOf(count[x]));
            sButtons[x].setBackgroundDrawable(getResources().getDrawable(R.drawable.seat_check));
            ticketsList.add(id);
            try {
                TotalPrice += Double.valueOf(TikToBut.get(idB[x]).getString("originalPrice"));
                Log.e("price", String.valueOf(TotalPrice));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            count[x]++;
        }
        if (TotalPrice < 0) {
            TotalPrice = 0;
        }
        BigDecimal t = new BigDecimal(TotalPrice);
        showPrice.setText("Total Price: " + t.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public void dialog(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String cookie = sharedPreferences.getString("cookie","");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round);
        if (cookie != "") {
            OkClient okClient = new OkClient();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        okClient.sendTicket(ticketsList, cookie, "0");
                        String recentOrderId = new JSONObject(okClient.getResult()).getString("id");
                        editor.putString("recentOrderId",recentOrderId);
                        editor.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            try{
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            for (x = 0, c = 0; x < MAX_GRID; x++) {
                c = c + count[x];
            }
            if (c == 0) {
                builder.setTitle("Caution!");
                builder.setMessage("You haven't chosen any seats!");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(FilmBookDetailActivity.this, "Retry", Toast.LENGTH_SHORT).show();
                    }
                });
//            builder.setNegativeButton("cancel", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            } else {
                builder.setTitle("Order");
                builder.setMessage("You sure your order is as following:\n" + ticketsList.size() + " tickets");
                builder.setNegativeButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Thread c = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 try{
                                 okClient.payOrder(cookie);
                                 } catch (IOException e) {
                                 e.printStackTrace();
                                 }*/
                            }
                        });
                        c.start();
                        try{
                            c.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(FilmBookDetailActivity.this, CodeActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Thread c = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    okClient.cancelOrder(cookie);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        c.start();
                        try{
                            c.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }else{
            builder.setTitle("Caution");
            builder.setMessage("You haven't login yet\ngo to login?");
            builder.setNegativeButton("Go to Login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(FilmBookDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

//    public static void main(String args[]){
//        System.out.println(count);
//    }

    private void getSeatData() {
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        String id = sharedPreferences.getString("screenId","");
        Log.e("in detail id is", id);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Integer row=7, col=8;
                Log.e("start", "set seats");
                OkClient OkClient = new OkClient();
                OkClient.setMode("screenings", id);
                ResolveJson resolveJson = new ResolveJson(OkClient.getResult());
                List<String> result = new ArrayList<>();
                try {
                    resolveJson.readSeats();
                    result = resolveJson.getOutput();
                    int i =0;int[] ifSet = new int[MAX_GRID];
                    while(i<MAX_GRID) {
                        if(ifSet[i]==1){
                            i++;
                            continue;
                        }
                        if (i < result.size()) {
                            JSONObject ticket = new JSONObject(result.get(i));
                            Integer tRow = Integer.valueOf(ticket.getString("row"));
                            Integer tCol = Integer.valueOf(ticket.getString("col"));
                            Integer Pos = (tRow-1)*col+tCol-1;
                            TikToBut.put(idB[Pos], ticket);
                            ifSet[i]=1;
                        } else {
                            TikToBut.put(idB[i], null);
                        }
                        i++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e("start", "set seat status");
                            setSeatStatus(TikToBut);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
        t.start();
    }

    private void setSeatStatus(HashMap<Integer, JSONObject> TikToBut) throws JSONException {
        int c = 0;
        for (int i = 0; i < MAX_GRID; i++) {
            JSONObject ticket = TikToBut.get(idB[i]);
            if (ticket != null) {
                ticket = new JSONObject(TikToBut.get(idB[i]).toString());
                String status = ticket.get("status").toString();
                if (status.equals("UNAVAILABLE")) {
                    sButtons[i].setClickable(false);
                    sButtons[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.seat_check));
                } else {
                    sButtons[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.seat_free));
                }
            } else {
                c++;
                sButtons[i].setClickable(false);
                sButtons[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.seat_unable));
            }


        }
        Log.e("all empty tickets", String.valueOf(c));
    }
}

