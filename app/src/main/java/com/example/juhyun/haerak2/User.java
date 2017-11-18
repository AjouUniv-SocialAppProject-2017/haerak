package com.example.juhyun.haerak2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SeoYeon Choi on 2017-11-17.
 */

public class User {

    private String userId;
    private String nickName;
    private String passWord;
    private String gender;

    public User() {
    }

    public User(String userId, String nickName, String passWord, String gender){
        this.userId = userId;
        this.nickName = nickName;
        this.passWord = passWord;
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", userId);
        result.put("nickName", nickName);
        result.put("password", passWord);
        result.put("gender", gender);

        return result;
    }
}
