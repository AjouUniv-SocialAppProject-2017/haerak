package com.example.juhyun.haerak2;

import java.io.Serializable;

/**
 * Created by SeoYeon Choi on 2017-11-26.
 */

public class BucketComment implements Serializable{

    private String writer;
    private String content;
    private String date;

    public BucketComment(){

    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
