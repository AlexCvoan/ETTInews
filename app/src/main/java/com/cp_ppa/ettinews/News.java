package com.cp_ppa.ettinews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class News{
    @SerializedName("url")
    @Expose
    public ArrayList<String> url = null;
    @SerializedName("_type")
    @Expose
    public String type;
    @SerializedName("title")
    @Expose
    public ArrayList<String> title = null;

    public News(ArrayList<String> url, String _type, ArrayList<String> title){
        this.url = url;
        this.type = _type;
        this.title = title;
    }

    public ArrayList<String> getTitle(){
        return this.title;
    }
    public ArrayList<String> getUrl() {
        return this.url;
    }
}
