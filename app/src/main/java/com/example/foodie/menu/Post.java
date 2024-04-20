package com.example.foodie.menu;

public class Post {
    private String content;
    private String imageUrl;
    private String date;
    private String time;
    private String username;
    private String id;
    private int totallike;

    // Required default constructor for Firebase
    public Post() {
    }

    public Post(String content, String imageUrl, String date, String time, String username, int totalLike, String id) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.date = date;
        this.time = time;
        this.username = username;
        this.totallike= totalLike;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() { // Getter for username
        return username;
    }

    public void setUsername(String username) { // Setter for username
        this.username = username;
    }

    public void setTotalLike(int totalLike) {
        this.totallike = totalLike;
    }

    public int getTotalLike() {
        return totallike;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
