package com.cp_ppa.ettinews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    private TextView mTextViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewResult = findViewById(R.id.text_view_result);

        OkHttpClient client = new OkHttpClient();
        int index = 5;
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

