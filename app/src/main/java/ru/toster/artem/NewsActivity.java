package ru.toster.artem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.toster.toster.R;


public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.viewpager) @Nullable
    ViewPager viewPager;

    @BindView(R.id.drawer_layout) @Nullable
    DrawerLayout drawer;

//    @BindView(R.id.toolbar) @Nullable //Не работает setTitle если включить это!!
    Toolbar toolbar;

    private NewsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        presenter = new NewsPresenter(this);

        setSupportActionBar(toolbar);

        ButterKnife.bind(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);

        toggle.syncState();

        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(presenter);

        ((TabLayout) findViewById(R.id.tabs)).setupWithViewPager(viewPager);

        presenter.onNavigationItemSelected(null);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    public void setViewPager(String title, ViewPagerAdapter adapter, int currentItem, int id){
        LinearLayout layout;

        if ((layout = (LinearLayout) findViewById(R.id.viewLinear))!=null)
            layout.removeAllViews();

        drawer.closeDrawers();
        if (title!=null)
            setTitle(title);
        if (currentItem!=0)
            viewPager.setCurrentItem(currentItem);
        viewPager.setAdapter(adapter);

        if (id==R.id.all_query || id==R.id.all_tegy) {
            findViewById(R.id.appBarLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.viewpager).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.appBarLayout).setVisibility(View.GONE);
        }
    }
}
