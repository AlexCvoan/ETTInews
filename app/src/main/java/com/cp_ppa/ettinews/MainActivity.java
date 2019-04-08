package com.cp_ppa.ettinews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
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

    private TextView mTextViewResult;
    private Button mRefreshButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int index = 7;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewResult = findViewById(R.id.text_view_result);
        mRefreshButton = findViewById(R.id.refreshButton);


        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (refreshNews(index))  {
                    try {
                        TimeUnit.SECONDS.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getNews(index);
                }

            }
        });




    }

    private boolean refreshNews(int index) {
        OkHttpClient client = new OkHttpClient();
        index++;
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
//                    final String myResponse = response.body().string();
//
//                    Gson gson = new Gson();
//                    News[] latestnews = gson.fromJson(myResponse, News[].class);
//
//                    System.out.println("it is working");
//
//                    MainActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mTextViewResult.setText(myResponse);
//                        }
//                    });
                    System.out.println("refresh done");


                }
            }

        });

        return true;
    }
        private void getNews(int index){
            OkHttpClient client = new OkHttpClient();

            String requestUrl = "https://storage.scrapinghub.com/items/381388/1/" + index;

            HttpUrl.Builder urlBuilder =
                    HttpUrl.parse(requestUrl).newBuilder();
            urlBuilder.addQueryParameter("apikey","ea529b13f1004c559834d391160d17c4");
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .header("Accept","application/json")
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
                        final String myResponse = response.body().string();

                        Gson gson = new Gson();
                        News[] latestnews = gson.fromJson(myResponse, News[].class);

                        System.out.println("it is working");

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextViewResult.setText(myResponse);
                            }
                        });
                    }
                }
            });


    }

}

