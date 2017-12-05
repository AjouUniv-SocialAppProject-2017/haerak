package com.example.juhyun.haerak2;

import java.util.ArrayList;

/**
 * Created by SeoYeon Choi on 2017-12-06.
 */

public class GroupPost {

    private String title;
    private String content;
    private String writer;
    private ArrayList<String> likeMembers;

    public GroupPost(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public ArrayList<String> getLikeMembers() {
        return likeMembers;
    }

    public void setLikeMembers(ArrayList<String> likeMembers) {
        this.likeMembers = likeMembers;
    }
}
