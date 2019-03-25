package com.cp_ppa.ettinews;

import java.util.List;

public class News{
    private List<String> url;
    private String _type;
    private List<String> title;

    public News(List<String> url, String _type, List<String> title){
        this.url = url;
        this._type = _type;
        this.title = title;
    }

    public List<String> getNews(){
        return this.title;
    }
}
