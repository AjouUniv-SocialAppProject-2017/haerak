package com.example.juhyun.haerak2;

import java.util.ArrayList;

/**
 * Created by SeoYeon Choi on 2017-11-30.
 */

public class BucketGroup {
    String title;
    String content;
    int progressRate;
    ArrayList<String> members;

    BucketGroup(){}

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

    public int getProgressRate() {
        return progressRate;
    }

    public void setProgressRate(int progressRate) {
        this.progressRate = progressRate;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
}
