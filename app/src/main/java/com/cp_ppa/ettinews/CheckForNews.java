package com.cp_ppa.ettinews;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CheckForNews extends AsyncTask<String, Void, String> {

    private String mLastKey, mLastButOneKey;


    @Override
    protected String doInBackground(String... params) {
        getJobCount();
        while (!params[0].equals(mLastButOneKey)) {
            Log.d("Checking","Checking for news");
            Log.d("test",params[0]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getJobCount();
            if(params[0].equals(mLastButOneKey))
                return mLastButOneKey;
        }
        return "Fail.";
    }



    private void getJobCount() {

        OkHttpClient client = new OkHttpClient();
        String requestUrl = "https://storage.scrapinghub.com/jobq/381388/list";

        HttpUrl.Builder urlBuilder =
                HttpUrl.parse(requestUrl).newBuilder();
        urlBuilder.addQueryParameter("apikey", "ea529b13f1004c559834d391160d17c4");
        urlBuilder.addQueryParameter("count", "2");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
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
                    String myResponse = response.body().string();

                    int mLast = myResponse.indexOf("key") + 7;
                    int mLastButOne = myResponse.indexOf("key", myResponse.indexOf("key") + 1) + 7;
                    mLastKey = myResponse.substring(mLast, mLast + 12);
                    mLastButOneKey = myResponse.substring(mLastButOne, mLastButOne + 12);

                }
            }
        });


    }
}