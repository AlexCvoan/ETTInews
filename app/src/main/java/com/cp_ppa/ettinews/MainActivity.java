package com.cp_ppa.ettinews;

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

    private ArrayList<String> mTitlesFixed = new ArrayList<>(20);
    private ArrayList<String> mUrlsFixed = new ArrayList<>(20);
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String mLastKey;
    private String mLastButOneKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button mRefreshButton = findViewById(R.id.refreshButton);

  //      refreshNews();
        getJobCount();




        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             getNews(mLastKey);
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



        private void getNews(String key){
            OkHttpClient client = new OkHttpClient();

            String requestUrl = "https://storage.scrapinghub.com/items/" + key;

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
                        String myResponse = response.body().string();
                        String myCustomResponse = myResponse.substring(1, myResponse.length()-1);

                        Gson gson = new Gson();
                        final News latestNews = gson.fromJson(myCustomResponse, News.class);


                        ArrayList<String> mTitles = latestNews.getTitle();

                        for(int i = 0; i < mTitles.size(); i++){
                            mTitlesFixed.add(mTitles.get(i).substring(4));
                        }


                        ArrayList<String> mUrls = latestNews.getUrl();

                        for(int i = 0; i< mUrls.size(); i++){
                            mUrlsFixed.add("http://www.electronica.pub.ro"+mUrls.get(i));
                        }

                        System.out.println("it is working");



                     MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                           public void run() {
                            recyclerView = findViewById(R.id.recycler_view);
                            recyclerView.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(MainActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            mAdapter = new RecyclerViewAdapter(mTitlesFixed, mUrlsFixed);
                            recyclerView.setAdapter(mAdapter);
                            }
                        });
                    }
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

}

