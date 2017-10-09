package ru.toster.artem.objects;


import java.util.ArrayList;
import java.util.List;

public class QuestionPageObject {
    private String name;
    private String dogName;
    private String tag;
    private String textName;
    private String text;
    private String date;
    private boolean isLike;
    private int like;
    private List<CommentToggleObject> toggleObjects = new ArrayList<>();
    private List<CommentAnswerObject> answerObjects = new ArrayList<>();
    private List<CommentAnswerObject> answerObjectsRight = new ArrayList<>();

    public List<CommentAnswerObject> getAnswerObjectsRight() {
        return answerObjectsRight;
    }

    public void setAnswerObjectsRight(List<CommentAnswerObject> answerObjectsRight) {
        this.answerObjectsRight = answerObjectsRight;
    }

    public QuestionPageObject() {
    }

    public QuestionPageObject(String name, String dogName, String tag, String textName, String text, String date, boolean isLike, int like, List<CommentToggleObject> toggleObjects, List<CommentAnswerObject> answerObjects) {
        this.name = name;
        this.dogName = dogName;
        this.tag = tag;
        this.textName = textName;
        this.text = text;
        this.date = date;
        this.isLike = isLike;
        this.like = like;
        this.toggleObjects = toggleObjects;
        this.answerObjects = answerObjects;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public List<CommentToggleObject> getToggleObjects() {
        return toggleObjects;
    }

    public void setToggleObjects(List<CommentToggleObject> toggleObjects) {
        this.toggleObjects = toggleObjects;
    }

    public List<CommentAnswerObject> getAnswerObjects() {
        return answerObjects;
    }

    public void setAnswerObjects(List<CommentAnswerObject> answerObjects) {
        this.answerObjects = answerObjects;
    }
}
