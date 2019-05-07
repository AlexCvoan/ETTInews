package com.cp_ppa.ettinews;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    private String mLastKey;
    private String mLastButOneKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button mStartButton = findViewById(R.id.startButton);

        refreshNews();
        getJobCount();




        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putString("mLastKey", mLastKey);
                bundle.putString("mLastButOneKey",mLastButOneKey);
                Intent intent = new Intent(MainActivity.this, MainScreen.class);
                intent.putExtras(bundle);
                MainActivity.this.startActivity(intent);
            // getNews(mLastKey);
            }
        });
    }


    private void getJobCount(){

        OkHttpClient client = new OkHttpClient();
        String requestUrl = "https://storage.scrapinghub.com/jobq/381388/list";

        HttpUrl.Builder urlBuilder =
                HttpUrl.parse(requestUrl).newBuilder();
        urlBuilder.addQueryParameter("apikey","ea529b13f1004c559834d391160d17c4");
        urlBuilder.addQueryParameter("count","2");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("Accept","application/json")
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();

                    int mLast = myResponse.indexOf("key") + 7;
                    int mLastButOne = myResponse.indexOf("key", myResponse.indexOf("key") + 1) + 7;
                    mLastKey = myResponse.substring(mLast, mLast + 11);
                    mLastButOneKey = myResponse.substring(mLastButOne, mLastButOne + 11);

                }
            }
        });


    }


    private void refreshNews() {
        OkHttpClient client = new OkHttpClient();
        String requestUrl = "https://app.scrapinghub.com/api/run.json";

        HttpUrl.Builder urlBuilder =
                HttpUrl.parse(requestUrl).newBuilder();
        urlBuilder.addQueryParameter("apikey", "ea529b13f1004c559834d391160d17c4");
        String url = urlBuilder.build().toString();

        RequestBody formBody = new FormBody.Builder().add("project", "381388").add("spider","etti").build();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("PROJECT", "etti")
                .header("SPIDER", "etti")
                .post(formBody)
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("refresh done");
                }
            }

        });
    }



}




