package com.cp_ppa.ettinews;

import android.annotation.SuppressLint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainScreen extends AppCompatActivity {

    private ArrayList<String> mTitlesFixed = new ArrayList<>(20);
    private ArrayList<String> mUrlsFixed = new ArrayList<>(20);
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String mLastKey;
    private String mLastButOneKey;
    private String mTest;
    private boolean isJobRunning;
    private News latestNews, previousNews;




    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        mLastKey = super.getIntent().getExtras().getString("mLastKey");
        mLastButOneKey = super.getIntent().getExtras().getString("mLastButOneKey");
        isJobRunning = super.getIntent().getExtras().getBoolean("isJobRunning");
        mTest = mLastKey;
        getNews(mLastKey);
        previousNews = latestNews;
        if(isJobRunning) {
            new CheckForNews() {
                @Override
                public void onPostExecute(String result) {
                    isJobRunning = false;
                    mLastKey = result;
                    Toast toast = Toast.makeText(MainScreen.this, "Paianjenul a terminat jobul", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }.execute(mTest);
        }
        final SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(!mTest.equals(mLastButOneKey)){
                    if(isJobRunning){
                        Toast toast = Toast.makeText(MainScreen.this ,"Paianjenul nu a terminat jobul", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        Toast toast = Toast.makeText(MainScreen.this ,"Nu exista stiri noi", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
                else{

                    getNews(mLastKey);

                    if(latestNews == previousNews){
                        Toast toast = Toast.makeText(MainScreen.this ,"Nu exista stiri noi", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                mSwipeRefreshLayout.setRefreshing(false);
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
                    latestNews = gson.fromJson(myCustomResponse, News.class);



                    ArrayList<String> mTitles = latestNews.getTitle();

                    for(int i = 0; i < mTitles.size(); i++){
                        mTitlesFixed.add(mTitles.get(i).substring(4));
                    }


                    ArrayList<String> mUrls = latestNews.getUrl();

                    for(int i = 0; i< mUrls.size(); i++){
                        mUrlsFixed.add("http://www.electronica.pub.ro"+mUrls.get(i));
                    }

                    Log.d("info", "GetNews done");





                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView = findViewById(R.id.recycler_view);
                            recyclerView.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(MainScreen.this);
                            recyclerView.setLayoutManager(layoutManager);
                            mAdapter = new RecyclerViewAdapter(mTitlesFixed, mUrlsFixed);
                            recyclerView.setAdapter(mAdapter);
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
                            recyclerView.addItemDecoration(dividerItemDecoration);
                        }
                    });
                }
            }
        });

        previousNews = latestNews;
    }

}
