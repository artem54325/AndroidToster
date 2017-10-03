package ru.toster.toster.fragmentTab;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.toster.toster.NewsActivity;
import ru.toster.toster.R;
import ru.toster.toster.fragmentTab.userAndTag.UserAndTagActivity;
import ru.toster.toster.http.HTTPCleint;
import ru.toster.toster.http.ParsingPage;
import ru.toster.toster.objects.CommentAnswerObject;
import ru.toster.toster.objects.QuestionPageObject;


public class PostAppCompat extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {//, AppBarLayout.OnOffsetChangedListener
    private PostPresenter postPresenter;
    private QuestionPageObject page;

    @BindView(R.id.fragment_post_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;//fragment_post_swipe_refresh_layout

    public static final String TAG = "PostAppCompat";

//    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_post);
        ButterKnife.bind(this);

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.materialup_toolbar);

        String url = null;

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null) url = bundle.getString("url", null);

        System.out.println(url + " HTTP");

        postPresenter = new PostPresenter(this, url);

        postPresenter.getHttp();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(postPresenter);

//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fragment_post_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);


        toolbar.setNavigationOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefresh() {
        postPresenter.getHttp();
    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }

    @BindView(R.id.layout_koments)LinearLayout layoutKomments;

    @BindView(R.id.linear_get_url)LinearLayout layoutGetUrl;

    @OnClick(R.id.linear_get_url)
    public void clickLayout(){
        if (page==null)
            return;
        Intent intent = new Intent(getApplicationContext(), UserAndTagActivity.class);
        intent.putExtra("url", "https://toster.ru/user/"+page.getDogName().replace("@",""));
        intent.putExtra("tag_and_user", false);
        startActivity(intent);
    }

    @OnClick(R.id.komm_number)
    public void clickTogleObject(){
        if (page.getToggleObjects().size()>0) {
            CommentFragment fragment = new CommentFragment(page.getToggleObjects());//android:background="@drawable/border
            fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_MinWidth );
            fragment.show(getSupportFragmentManager().beginTransaction(), "fragment_edit_name");
        }
    }

    public void views(final QuestionPageObject page) {//Дописать этот метод и postPresenter переделать!
        this.page = page;
        ((TextView) findViewById(R.id.komm_name)).setText(page.getName());
        ((TextView) findViewById(R.id.komm_dog_name)).setText(page.getDogName());
        ((TextView) findViewById(R.id.komm_text)).setText(Html.fromHtml(page.getText()));
        ((TextView) findViewById(R.id.komm_text)).setLinksClickable(true);
        ((TextView) findViewById(R.id.komm_time_and_views)).setText(page.getDate());
        ((TextView) findViewById(R.id.komm_question_tags)).setText(page.getTag());
        ((TextView) findViewById(R.id.komm_text_name)).setText(page.getTextName());
        ((TextView) findViewById(R.id.komm_but_subscribe)).setText("Подписаны " + String.valueOf(page.getLike()));
        ((TextView) findViewById(R.id.komm_number)).setText("Комментариев (" + page.getToggleObjects().size() + ")");

        LinearLayout linearRight = (LinearLayout) findViewById(R.id.layout_koments_right);
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());

        if (page.getAnswerObjectsRight().size()!=0){
            ((TextView) findViewById(R.id.number_answer_right)).setText("РЕШЕНИЯ ВОПРОСА (" + page.getAnswerObjectsRight().size() + ")");

            for (final CommentAnswerObject answerObject: page.getAnswerObjectsRight()){
                final View item = inflater.inflate(R.layout.el_comm_list, linearRight, false);
                ((TextView)item.findViewById(R.id.el_name)).setText(answerObject.getName() + " " + answerObject.getDogName());
                ((TextView)item.findViewById(R.id.el_name)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), UserAndTagActivity.class);
                        intent.putExtra("url", "https://toster.ru/user/"+answerObject.getDogName().replace("@",""));
                        intent.putExtra("tag_and_user", true);
                        startActivity(intent);
                    }
                });
                ((TextView)item.findViewById(R.id.el_text)).setText(answerObject.getText());
                ((TextView)item.findViewById(R.id.el_like)).setText("Нравится ("+answerObject.getNumberUserComments()+")");//el_date
                ((TextView)item.findViewById(R.id.el_comm)).setText(answerObject.getToggleObjects().size()+" комментариев");
                if (answerObject.getToggleObjects().size()>0) {
                    ((TextView) item.findViewById(R.id.el_comm)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CommentFragment fragment = new CommentFragment(answerObject.getToggleObjects());
                            fragment.show(getSupportFragmentManager().beginTransaction(), "fragment_edit_name");// getFragmentManager()
                        }
                    });
                }
                try {
                    ((TextView)item.findViewById(R.id.el_date)).setText(answerObject.getDate());
                }catch (Exception e){
                    e.printStackTrace();
                }
                item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

                linearRight.addView(item);
            }
        }else{
            ((TextView) findViewById(R.id.number_answer_right)).setVisibility(View.GONE);
            linearRight.setVisibility(View.GONE);
        }

        ((TextView) findViewById(R.id.number_answer)).setText("ОТВЕТЫ НА ВОПРОС (" + page.getAnswerObjects().size() + ")");
        for (final CommentAnswerObject answerObject: page.getAnswerObjects()){
            final View item = inflater.inflate(R.layout.el_comm_list, layoutKomments, false);
            ((TextView)item.findViewById(R.id.el_name)).setText(answerObject.getName() + " " + answerObject.getDogName());
            ((TextView)item.findViewById(R.id.el_text)).setText(answerObject.getText());
            ((TextView)item.findViewById(R.id.el_like)).setText("Нравится ("+answerObject.getNumberUserComments()+")");
            ((TextView)item.findViewById(R.id.el_comm)).setText(answerObject.getToggleObjects().size()+" комментариев");
            if (answerObject.getToggleObjects().size()>0) {
                ((TextView) item.findViewById(R.id.el_comm)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommentFragment fragment = new CommentFragment(answerObject.getToggleObjects());
                        fragment.show(getSupportFragmentManager().beginTransaction(), "fragment_edit_name");// getFragmentManager()
                    }
                });
            }
            ((TextView)item.findViewById(R.id.el_date)).setText(answerObject.getDate());
            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

            layoutKomments.addView(item);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

//    private void viewsPost(final QuestionPageObject page) {
}