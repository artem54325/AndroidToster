package ru.toster.toster.fragmentTab;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import ru.toster.toster.NewsActivity;
import ru.toster.toster.Presenter;
import ru.toster.toster.R;
import ru.toster.toster.http.HttpCleint;
import ru.toster.toster.http.ParsingPage;

public class PostPresenter implements Presenter{
    private PostAppCompat fragment;
    private String url;

    public PostPresenter(PostAppCompat fragment, String url) {
        this.fragment = fragment;
        this.url = url;
    }

    @Override
    public void getHttp() {
        HttpCleint cleint = new HttpCleint(fragment.getApplication(), this);
        cleint.execute(url);
    }

    @Override
    public void viewsPresent(final String html) {
        fragment.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment.views(ParsingPage.getQuestPage(html));
            }
        });
    }
}
