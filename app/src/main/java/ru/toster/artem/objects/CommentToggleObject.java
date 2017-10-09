package ru.toster.artem.objects;



public class CommentToggleObject {
    private String urlImage;
    private String name;
    private String dogName;
    private String text;
    private String date;
    private boolean isLike;
    private String like;

    public CommentToggleObject(String urlImage, String name, String dogName, String text, String date, boolean isLike, String like) {
        this.urlImage = urlImage;
        this.name = name;
        this.dogName = dogName;
        this.text = text;
        this.date = date;
        this.isLike = isLike;
        this.like = like;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
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

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public CommentToggleObject() {
    }

    public CommentToggleObject(String name, String dogName, String text, String date, boolean isLike, String like) {
        this.name = name;
        this.dogName = dogName;
        this.text = text;
        this.date = date;
        this.isLike = isLike;
        this.like = like;
    }
}
