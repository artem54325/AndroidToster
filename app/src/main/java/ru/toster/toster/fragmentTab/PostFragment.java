package ru.toster.toster.fragmentTab;


import android.app.Activity;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.toster.toster.NewsActivity;
import ru.toster.toster.R;
import ru.toster.toster.fragmentTab.userAndTag.UserAndTagActivity;
import ru.toster.toster.http.HTTP;
import ru.toster.toster.http.ParsingPage;
import ru.toster.toster.objects.QuestionPageObject;


public class PostFragment extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {//, AppBarLayout.OnOffsetChangedListener
    private String url;
    private static OkHttpClient client = HTTP.getClient();
    private Request request;
    private LinearLayout layout;
    private LayoutInflater inflater;
    private SwipeRefreshLayout mSwipeRefreshLayout;//fragment_post_swipe_refresh_layout
    public static final String TAG = "PostFragment";


    private Toolbar toolbar;
    private DrawerLayout drawer;


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_post);//перекинуть сюда

        this.url = getIntent().getStringExtra("url");
        getHttp();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fragment_post_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHttp();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.materialup_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void getHttp() {
        request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final QuestionPageObject page = (ParsingPage.getQuestPage(response.body().string()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewsPost(page);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void viewsPost(final QuestionPageObject page) {
        ((LinearLayout) findViewById(R.id.linear_get_url)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserAndTagActivity.class);
                intent.putExtra("url", "https://toster.ru/user/"+page.getDogName().replace("@",""));//list.get(finalI).getHref()
                intent.putExtra("tag_and_user", true);
                startActivity(intent);
            }
        });
        ((TextView) findViewById(R.id.komm_name)).setText(page.getName());
        ((TextView) findViewById(R.id.komm_dog_name)).setText(page.getDogName());
        ((TextView) findViewById(R.id.komm_text)).setText(Html.fromHtml(page.getText()));
        ((TextView) findViewById(R.id.komm_time_and_views)).setText(page.getDate());
        ((TextView) findViewById(R.id.komm_question_tags)).setText(page.getTag());
        ((TextView) findViewById(R.id.komm_text_name)).setText(page.getTextName());
        ((TextView) findViewById(R.id.komm_but_subscribe)).setText("Подписаны " + String.valueOf(page.getLike()));
        ((TextView) findViewById(R.id.komm_number)).setText("Комментариев (" + page.getToggleObjects().size() + ")");
        this.layout = (LinearLayout) findViewById(R.id.layout_koments);
        if (page.getToggleObjects().size()>0) {
            ((TextView) findViewById(R.id.komm_number)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentFragment fragment = new CommentFragment(page.getToggleObjects());//android:background="@drawable/border
                    fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_Dialog_MinWidth );
                    fragment.show(getSupportFragmentManager().beginTransaction(), "fragment_edit_name");
                }
            });
        }
        LinearLayout linearRight = (LinearLayout) findViewById(R.id.layout_koments_right);
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());

        if (page.getAnswerObjectsRight().size()!=0){
            ((TextView) findViewById(R.id.number_answer_right)).setText("РЕШЕНИЯ ВОПРОСА (" + page.getAnswerObjectsRight().size() + ")");
            for (int i = 0; i < page.getAnswerObjectsRight().size(); i++) {
                final View item = inflater.inflate(R.layout.el_comm_list, linearRight, false);
                ((TextView)item.findViewById(R.id.el_name)).setText(page.getAnswerObjectsRight().get(i).getName() + " " + page.getAnswerObjectsRight().get(i).getDogName());
                ((TextView)item.findViewById(R.id.el_text)).setText(page.getAnswerObjectsRight().get(i).getText());
                ((TextView)item.findViewById(R.id.el_like)).setText("Нравится ("+page.getAnswerObjectsRight().get(i).getNumberUserComments()+")");//el_date
                ((TextView)item.findViewById(R.id.el_comm)).setText(page.getAnswerObjectsRight().get(i).getToggleObjects().size()+" комментариев");
                if (page.getAnswerObjectsRight().get(i).getToggleObjects().size()>0) {
                    final int finalI = i;
                    ((TextView) item.findViewById(R.id.el_comm)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CommentFragment fragment = new CommentFragment(page.getAnswerObjectsRight().get(finalI).getToggleObjects());
                            fragment.show(getSupportFragmentManager().beginTransaction(), "fragment_edit_name");// getFragmentManager()
                        }
                    });
                }
                try {
                    ((TextView)item.findViewById(R.id.el_date)).setText(page.getAnswerObjects().get(i).getDate());
                }catch (Exception e){
                    e.printStackTrace();
                }
                item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

                linearRight.addView(item);
            }
        }else{
            ((TextView) findViewById(R.id.number_answer_right)).setVisibility(View.GONE);//layout_koments_right
            ((LinearLayout) findViewById(R.id.layout_koments_right)).setVisibility(View.GONE);
        }

        ((TextView) findViewById(R.id.number_answer)).setText("ОТВЕТЫ НА ВОПРОС (" + page.getAnswerObjects().size() + ")");//number_answer_right

        for (int i = 0; i < page.getAnswerObjects().size(); i++) {
            final View item = inflater.inflate(R.layout.el_comm_list, layout, false);
            ((TextView)item.findViewById(R.id.el_name)).setText(page.getAnswerObjects().get(i).getName() + " " + page.getAnswerObjects().get(i).getDogName());
            ((TextView)item.findViewById(R.id.el_text)).setText(page.getAnswerObjects().get(i).getText());
            ((TextView)item.findViewById(R.id.el_like)).setText("Нравится ("+page.getAnswerObjects().get(i).getNumberUserComments()+")");//el_date
            ((TextView)item.findViewById(R.id.el_comm)).setText(page.getAnswerObjects().get(i).getToggleObjects().size()+" комментариев");
            if (page.getAnswerObjects().get(i).getToggleObjects().size()>0) {
                final int finalI = i;
                ((TextView) item.findViewById(R.id.el_comm)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommentFragment fragment = new CommentFragment(page.getAnswerObjects().get(finalI).getToggleObjects());
                        fragment.show(getSupportFragmentManager().beginTransaction(), "fragment_edit_name");// getFragmentManager()
                    }
                });
            }
            ((TextView)item.findViewById(R.id.el_date)).setText(page.getAnswerObjects().get(i).getDate());
            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


            layout.addView(item);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.all_query){
            onBackPressed();
        }else{
            Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
            intent.putExtra("id", item.getItemId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}