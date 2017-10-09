package ru.toster.toster.fragmentTab.userandtag;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.toster.toster.NewsActivity;
import ru.toster.toster.R;
import ru.toster.toster.fragmentTab.NavigationItem;
import ru.toster.toster.fragmentTab.PostPresenter;
import ru.toster.toster.http.DowlandImage;
import ru.toster.toster.objects.NameAndTagFullInfoObject;

public class UserAndTagActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {//

    private UserAndTagPresenter presenter;

    @BindView(R.id.materialup_tabs) TabLayout tabLayout;
    public static final String TAG = "UserAndTagActivity";
    @BindView(R.id.materialup_viewpager) ViewPager viewPager;
    private boolean tagAndUser;


    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private Toolbar toolbar;

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    private int mMaxScrollSize;

    @Override
    public void onClick(View view) {
        onBackPressed();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsing_toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.materialup_toolbar);

        ButterKnife.bind(this);

        tagAndUser = getIntent().getBooleanExtra("tag_and_user", true);

        presenter = new UserAndTagPresenter(this, getIntent().getStringExtra("url"), tagAndUser);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(new NavigationItem(this));

        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);

        setSupportActionBar(toolbar);

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter.getHttp();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange()-toolbar.getHeight();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {//onNavigationItemSelected
        Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
        intent.putExtra("id", item.getItemId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    public void views(UserAndTagActivity.TabsAdapter adapter, NameAndTagFullInfoObject fullName){
        if (tagAndUser){
            setViewTage(fullName);
        }else{
            setViewUser(fullName);
        }
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setViewUser(NameAndTagFullInfoObject fullName) {
        new DowlandImage((ImageView)findViewById(R.id.user_tag_image)).execute(fullName.getUrlPhoto());
        ((TextView)findViewById(R.id.user_tag_name)).setText(fullName.getName());
        if (fullName.getDopName()!=null){
            ((TextView)findViewById(R.id.user_tag_job_title)).setText(fullName.getDopName());
        }//  user_tag_subscribe
        ((TextView)findViewById(R.id.user_tag_contribution)).setText(fullName.getContribution());
        ((TextView)findViewById(R.id.user_tag_question)).setText(fullName.getQuestion());
        ((TextView)findViewById(R.id.user_tag_answer)).setText(fullName.getAnswer());
        ((TextView)findViewById(R.id.user_tag_solutions)).setText(fullName.getDecisions());
    }
    private void setViewTage(NameAndTagFullInfoObject fullName) {
        new DowlandImage((ImageView)findViewById(R.id.user_tag_image)).execute(fullName.getUrlPhoto());
        ((TextView)findViewById(R.id.user_tag_name)).setText(fullName.getName());
        if (fullName.getDopName()!=null){
            ((TextView)findViewById(R.id.user_tag_job_title)).setText(fullName.getDopName());
        }//  user_tag_subscribe
        ((View)findViewById(R.id.user_tag_view)).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.user_tag_contribution)).setText("");
        ((TextView)findViewById(R.id.user_tag_question)).setText(fullName.getQuestion());
        ((TextView)findViewById(R.id.user_tag_answer)).setText(fullName.getAnswer());
        ((TextView)findViewById(R.id.user_tag_solutions)).setText(fullName.getDecisions());
    }



    public static class TabsAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override

        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public static class InfoFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.info_fragment, container, false);//fragment_all_tags
            //bundle дописать
            String textInfo=null;
            Bundle bundle = getArguments();
            if (bundle!=null) textInfo = bundle.getString("url");//Чтобы не писать другой метод пусть, в этом же слоту присылает))

            if (textInfo==null){
                ((TextView)view.findViewById(R.id.text_view)).setText("Пользователь пока ничего не рассказал о себе");
            }else{
                ((TextView)view.findViewById(R.id.text_view)).setText(textInfo);
            }
            return view;
        }
    }
}
