package ru.toster.toster.fragmentTab.userandtag;


import ru.toster.toster.NewsPresenter;
import ru.toster.toster.Presenter;
import ru.toster.toster.fragmentTab.QuestionFragment;
import ru.toster.toster.fragmentTab.alltags.AllTagsFragment;
import ru.toster.toster.fragmentTab.users.UsersFragment;
import ru.toster.toster.http.HttpCleint;
import ru.toster.toster.http.ParsingPage;
import ru.toster.toster.objects.NameAndTagFullInfoObject;

public class UserAndTagPresenter implements Presenter{
    private final UserAndTagActivity fragment;
    private String url;
    private boolean tagAndUser;

    public UserAndTagPresenter(UserAndTagActivity fragment, String url, boolean tagAndUser) {
        this.fragment = fragment;
        this.url = url;
        this.tagAndUser = tagAndUser;
        getHttp();
    }

    @Override
    public void getHttp() {
        HttpCleint cleint = new HttpCleint(fragment, this);
        cleint.execute(url+"/info");
    }

    @Override
    public void viewsPresent(final String html) {
        fragment.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NameAndTagFullInfoObject fullName = null;
                if (tagAndUser){
                    fullName = ParsingPage.parsFullTag(html);
                    viewsTag(fullName);
                }else{
                    fullName = ParsingPage.parsFullName(html);
                    viewsUser(fullName);
                }
            }
        });
    }
    private void viewsTag(NameAndTagFullInfoObject fullName){
        UserAndTagActivity.TabsAdapter adapter = new UserAndTagActivity.TabsAdapter(fragment.getSupportFragmentManager());
//        adapter.addFragment(NewsPresenter.getFragment(new UserAndTagActivity.InfoFragment(), fullName.getTextInfo()), "Информация");//new UserAndTagActivity.InfoFragment(fullName)
        adapter.addFragment(NewsPresenter.getFragment(new QuestionFragment(), url+"/questions"),"Новые вопросы");
        adapter.addFragment(NewsPresenter.getFragment(new QuestionFragment(), url+"/interest_questions"),"Интересные");
        adapter.addFragment(NewsPresenter.getFragment(new QuestionFragment(), url+"/questions_wo_answer"),"Без ответа");
        adapter.addFragment(NewsPresenter.getFragment(new UsersFragment(), url+"/users"),"Подписчики");

        fragment.views(adapter, fullName);
    }
    private void viewsUser(NameAndTagFullInfoObject fullName){
        UserAndTagActivity.TabsAdapter adapter = new UserAndTagActivity.TabsAdapter(fragment.getSupportFragmentManager());
//        adapter.addFragment(NewsPresenter.getFragment(new UserAndTagActivity.InfoFragment(), fullName.getTextInfo()), "Информация");
//        adapter.addFragment(,"Ответы");
        adapter.addFragment(NewsPresenter.getFragment(new QuestionFragment(), url+"/questions"),"Вопросы");//new QuestionFragment()
        adapter.addFragment(NewsPresenter.getFragment(new QuestionFragment(), url+"/iquestions"),"Подписан"); // AllTagsFragment
        adapter.addFragment(NewsPresenter.getFragment(new AllTagsFragment(), url+"/tags"),"Теги");//
//        adapter.addFragment(,"Нравится");
        fragment.views(adapter, fullName);
    }
}
