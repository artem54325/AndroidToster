package ru.toster.artem.objects;


import java.util.ArrayList;
import java.util.List;

public class CommentAnswerObject {
    private String urlImage;
    private String name;
    private String dogName;
    private String text;
    private String date;
    private int numberUserComments;
    private int numberLikeComents;
    private boolean like;
    private List<CommentToggleObject> toggleObjects = new ArrayList<>();

    public CommentAnswerObject(String urlImage, String name, String dogName, String text, String date, int numberUserComments, int numberLikeComents, boolean like, List<CommentToggleObject> toggleObjects) {
        this.urlImage = urlImage;
        this.name = name;
        this.dogName = dogName;
        this.text = text;
        this.date = date;
        this.numberUserComments = numberUserComments;
        this.numberLikeComents = numberLikeComents;
        this.like = like;
        this.toggleObjects = toggleObjects;
    }

    public CommentAnswerObject(String urlImage, String name, String dogName, String text, String date, int numberUserComments, int numberLikeComents, boolean like) {
        this.urlImage = urlImage;
        this.name = name;
        this.dogName = dogName;
        this.text = text;
        this.date = date;
        this.numberUserComments = numberUserComments;
        this.numberLikeComents = numberLikeComents;
        this.like = like;
    }

    public CommentAnswerObject() {
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

    public int getNumberUserComments() {
        return numberUserComments;
    }

    public void setNumberUserComments(int numberUserComments) {
        this.numberUserComments = numberUserComments;
    }

    public int getNumberLikeComents() {
        return numberLikeComents;
    }

    public void setNumberLikeComents(int numberLikeComents) {
        this.numberLikeComents = numberLikeComents;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public List<CommentToggleObject> getToggleObjects() {
        return toggleObjects;
    }

    public void setToggleObjects(List<CommentToggleObject> toggleObjects) {
        this.toggleObjects = toggleObjects;
    }
}
