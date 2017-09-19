package ru.toster.toster.objects;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.toster.toster.R;

public class QuestionObject {//Сделать контейнера чтобы норм было!!
    private String tag;//Тэг Вопроса
    private String href;//Ссылка
    private String question;//Сам вопрос
    private String subscribers;//кол-во Подписчики
    private String date;//Дата публикации
    private String views;//кол-воПросмотры
    private boolean isAnswer;//Введен ли правилиьно ответ
    private int answer;//Кол-во ответов

    public QuestionObject(String tag, String href, String question, String subscribers, String date, String views, boolean isAnswer, int answer) {
        this.tag = tag;
        this.href = href;
        this.question = question;
        this.subscribers = subscribers;
        this.date = date;
        this.views = views;
        this.isAnswer = isAnswer;
        this.answer = answer;
    }

    public QuestionObject() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(String subscribers) {
        this.subscribers = subscribers;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
