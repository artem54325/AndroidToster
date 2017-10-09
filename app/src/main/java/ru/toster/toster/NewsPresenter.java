package ru.toster.toster;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import ru.toster.toster.fragmentTab.QuestionFragment;
import ru.toster.toster.fragmentTab.alltags.AllTagsFragment;
import ru.toster.toster.fragmentTab.users.UsersFragment;

public class NewsPresenter implements NavigationView.OnNavigationItemSelectedListener{
    private final NewsActivity activity;

    public NewsPresenter(NewsActivity activity) {
        this.activity = activity;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id;
        if (item==null){
            id = R.id.all_query;
        }else{
            id = item.getItemId();
        }

        ViewPagerAdapter adapter=new ViewPagerAdapter(activity.getSupportFragmentManager());

        int number = 0;
        String title = null;

        switch (id) {
            case R.id.all_query:
                title = "Все вопросы";//(QuestionFragment.EnumQuestion.QuestionsLatest  QuestionFragment.EnumQuestion.QuestionsInteresting  QuestionFragment.EnumQuestion.QuestionsWithoutAnswer
                adapter.addFragment(getFragment(new QuestionFragment(), "https://toster.ru/questions/latest"), "НОВЫЕ ВОПРОСЫ");
                adapter.addFragment(getFragment(new QuestionFragment(), "https://toster.ru/questions/interesting"), "ИНТЕРЕСНЫЕ");
                adapter.addFragment(getFragment(new QuestionFragment(), "https://toster.ru/questions/without_answer"), "БЕЗ ОТВЕТА");
                break;
            case R.id.all_tegy:
                title = "Все теги";
                adapter.addFragment(getFragment(new AllTagsFragment(), "https://toster.ru/tags/by_watchers"), "ПО ПОДПИСЧИКАМ");
                adapter.addFragment(getFragment(new AllTagsFragment(), "https://toster.ru/tags/by_questions"), "ПО ВОПРОСОМ");
                number = 1;
                break;
            case R.id.users:
                title = "Пользователи";//getFragment(new AllTagsFragment(), "")
                adapter.addFragment(getFragment(new UsersFragment(), "https://toster.ru/users"), "");//QuestionFragment.EnumQuestion.Users
                break;
            case R.id.settings:
                title = "Настройки";
                break;
        }

        activity.setViewPager(title, adapter, number, id);

        return activity.onOptionsItemSelected(item);
    }

    public static Fragment getFragment(Fragment fragment, String url){
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }
}
