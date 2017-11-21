package com.example.juhyun.haerak2;

import java.io.Serializable;

/**
 * Created by SeoYeon Choi on 2017-11-19.
 */

public class Bucket implements Serializable{

    private String title;
    private String writer;
    private String category;
    private String date;
    private String content;
    private int limitNumber;

    public Bucket(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLimitNumber() {
        return limitNumber;
    }

    public void setLimitNumber(int limitNumber) {
        this.limitNumber = limitNumber;
    }
}
