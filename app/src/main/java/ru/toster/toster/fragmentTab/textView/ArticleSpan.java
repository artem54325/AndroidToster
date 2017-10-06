package ru.toster.toster.fragmentTab.textView;

import android.text.style.ClickableSpan;
import android.view.View;

import ru.toster.toster.fragmentTab.PostPresenter;
import ru.toster.toster.fragmentTab.QuestionFragment;


public class ArticleSpan extends ClickableSpan {
    final PostPresenter activity;
    final String articleId;

    public ArticleSpan(PostPresenter activity, String articleId) {
        super();
        this.activity = activity;
        this.articleId = articleId;
    }

    @Override
    public void onClick(View arg0) {
        activity.setArticle(articleId);
    }

}
