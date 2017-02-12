package su.ict.business59.partnersforpurchasing.models;

import com.google.gson.annotations.Expose;

/**
 * Created by kaowneaw on 1/28/2017.
 */

public class Message {
    @Expose
    String userId;
    @Expose
    String userName;
    @Expose
    String imgProfile;
    @Expose
    String text;
    @Expose
    String date;

    public Message() {
    }

    public Message(String userId, String userName, String imgProfile, String text, String date) {
        this.userId = userId;
        this.userName = userName;
        this.imgProfile = imgProfile;
        this.text = text;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
