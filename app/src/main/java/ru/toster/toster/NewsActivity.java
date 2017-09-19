package ru.toster.toster;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.toster.toster.fragmentTab.PostFragment;
import ru.toster.toster.fragmentTab.QuestionFragment;
import ru.toster.toster.fragmentTab.allTags.AllTagsFragment;
import ru.toster.toster.fragmentTab.users.UsersFragment;
import ru.toster.toster.http.HTTP;
import ru.toster.toster.http.ParsingPage;
import ru.toster.toster.objects.QuestionObject;


public class NewsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private ViewPagerAdapter adapter=null;
    private UsersFragment users = null;
    //Сделать нормальное открывание таба

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setTitle("Все вопросы");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPagerQuery();

        ((TabLayout) findViewById(R.id.tabs)).setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        //Убирать фрагмент в viewLinear
        //Вариант 1
//        if (getFragmentManager().findFragmentByTag(UsersFragment.TAG)!=null){
//            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//            fragmentTransaction.remove(users);
//            fragmentTransaction.commit();
//        }
        //Вариант 2
        LinearLayout layout;
        if ((layout = (LinearLayout) findViewById(R.id.viewLinear))!=null)
            layout.removeAllViews();

        switch (id) {
            case R.id.all_query:
                setupViewPagerQuery();
                break;
            case R.id.all_tegy:
                setupViewPagerTag();
                break;
            case R.id.users:
                setupViewUsers();
                break;
            case R.id.settings:
                setTitle("Настройки");
                break;
        }
//        getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (id==R.id.all_query||id==R.id.all_tegy) {
            findViewById(R.id.appBarLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.viewpager).setVisibility(View.VISIBLE);
        }
        drawer.closeDrawers();

        return super.onOptionsItemSelected(item);
    }

    private void setupViewUsers(){
        setTitle("Пользователи");
        findViewById(R.id.appBarLayout).setVisibility(View.GONE);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        users = new UsersFragment(QuestionFragment.EnumQuestion.Users);


        adapter.addFragment(users, "ПО ВОПРОСОМ");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPagerTag() {//viewPager.removeAllViews();
        setTitle("Все теги");
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllTagsFragment(QuestionFragment.EnumQuestion.TagsBySubscribers), "ПО ПОДПИСЧИКАМ");
        adapter.addFragment(new AllTagsFragment(QuestionFragment.EnumQuestion.TagsByQuestion), "ПО ВОПРОСОМ");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    private void setupViewPagerQuery() {
        setTitle("Все вопросы");
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new QuestionFragment(QuestionFragment.EnumQuestion.QuestionsLatest), "НОВЫЕ ВОПРОСЫ");//HTTP.getQuestion(HTTP.EnumQuestion.QuestionsLatest)
        adapter.addFragment(new QuestionFragment(QuestionFragment.EnumQuestion.QuestionsInteresting), "ИНТЕРЕСНЫЕ");//HTTP.getQuestion(HTTP.EnumQuestion.QuestionsInteresting)
        adapter.addFragment(new QuestionFragment(QuestionFragment.EnumQuestion.QuestionsWithoutAnswer), "БЕЗ ОТВЕТА");//HTTP.getQuestion(HTTP.EnumQuestion.QuestionsWithoutAnswer)
        viewPager.setAdapter(adapter);
    }
}
