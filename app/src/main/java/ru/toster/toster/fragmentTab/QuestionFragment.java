package ru.toster.toster.fragmentTab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.toster.toster.R;
import ru.toster.toster.fragmentTab.userAndTag.UserAndTagActivity;
import ru.toster.toster.http.HTTP;
import ru.toster.toster.http.ParsingPage;
import ru.toster.toster.objects.QuestionObject;



public class QuestionFragment extends Fragment {
    public enum EnumQuestion{
        MyFeedLatest, MyFeedInteresting, MyFeedWithoutAnswer,QuestionsInteresting, QuestionsWithoutAnswer, QuestionsLatest, Toster, TagsBySubscribers, TagsByQuestion, Users, QUESTION,
        IQUESTION, InterestQuestions, QuestionsWoAnswer
    }

    private boolean dowlandPage=false;
    private List<QuestionObject>listQuestion = new ArrayList<>();//список данных отображенных на View
    private ScrollView scrollView;
    private EnumQuestion enumQuestion = null;
    private LinearLayout layout;
    private LayoutInflater inflater;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static Request request;
    private static OkHttpClient client = HTTP.getClient();
    private int number=1;//Номер страницы сбора данных
    private FragmentTransaction fragmentTransaction;
    private PostFragment postFragment = null;
    private String user = null;


    public QuestionFragment(EnumQuestion enumQuestion) {
        this.enumQuestion = enumQuestion;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttp(enumQuestion);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public QuestionFragment(String user, EnumQuestion enumQuestion) {
        this.user = user;
        this.enumQuestion = enumQuestion;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        scrollView = (ScrollView)view.findViewById(R.id.scroll_question_fragment);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHttp(enumQuestion);
            }
        });

         layout = (LinearLayout) view.findViewById(R.id.scroll_container);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                if (!dowlandPage) {
                    if (layout.getHeight() - (scrollView.getHeight() + scrollView.getScrollY())<=2000&&scrollView.getScrollY()>=500) {
                        dowlandPage = true;
                        number++;
                        getHttp(enumQuestion, number);
                    }
                }
            }
        });

        views(listQuestion);
        return view;
    }

    public List<QuestionObject> getListQuestion() {
        return listQuestion;
    }

    public void views(final List<QuestionObject> list) {
        for (int i=0;i<list.size();i++){
            final View item = inflater.inflate(R.layout.element_list_news, layout, false);
            ((TextView)item.findViewById(R.id.el_tag)).setText(list.get(i).getTag());
            ((TextView)item.findViewById(R.id.el_answer)).setText("Ответов:");
            ((TextView)item.findViewById(R.id.el_number_answer)).setText(String.valueOf(list.get(i).getAnswer()));
            if (list.get(i).isAnswer()){
                ((TextView)item.findViewById(R.id.el_number_answer)).setTextColor(Color.parseColor("#65c178"));
            }
            ((TextView)item.findViewById(R.id.el_date)).setText(list.get(i).getDate());
            ((TextView)item.findViewById(R.id.el_question)).setText(list.get(i).getQuestion());
            ((TextView)item.findViewById(R.id.el_views)).setText(list.get(i).getViews());
            ((TextView)item.findViewById(R.id.el_subscribers)).setText(list.get(i).getSubscribers());

            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

            final int finalI = i;
            final QuestionFragment fragment = this;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PostFragment.class);
                    intent.putExtra("url", list.get(finalI).getHref());
                    startActivity(intent);
                }
            });
            layout.addView(item);
        }
        dowlandPage = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private synchronized void getHttp(QuestionFragment.EnumQuestion enumQuestion){
        String url = null;
        switch (enumQuestion){
            case MyFeedLatest:
                url = "https://toster.ru/my/feed_latest";
                break;
            case MyFeedInteresting:
                url = "https://toster.ru/my/feed_interesting";
                break;
            case MyFeedWithoutAnswer:
                url = "https://toster.ru/my/feed_without_answer";
                break;
            case QuestionsInteresting:
                url = "https://toster.ru/questions/interesting";
                break;
            case QuestionsWithoutAnswer:
                url = "https://toster.ru/questions/without_answer";
                break;
            case QuestionsLatest:
                url = "https://toster.ru/questions/latest";
                break;
            case Toster:
                url = "https://toster.ru";
                break;
            case QUESTION:
                url = this.user + "/questions";//https://toster.ru/user/opium/questions
                break;
            case IQUESTION:
                url = this.user + "/iquestions";
                break;
            case InterestQuestions:
                url = this.user + "/interest_questions";
                break;
            case QuestionsWoAnswer:
                url = this.user + "/questions_wo_answer";
                break;
            }

        request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listQuestion = (ParsingPage.parsQuestion(response.body().string()));
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            layout.removeAllViews();
                            views(listQuestion);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private synchronized void getHttp(QuestionFragment.EnumQuestion enumQuestion, int number){
        String url = null;
        switch (enumQuestion){
            case MyFeedLatest:
                url = "https://toster.ru/my/feed_latest";
                break;
            case MyFeedInteresting:
                url = "https://toster.ru/my/feed_interesting";
                break;
            case MyFeedWithoutAnswer:
                url = "https://toster.ru/my/feed_without_answer";
                break;
            case QuestionsInteresting:
                url = "https://toster.ru/questions/interesting";
                break;
            case QuestionsWithoutAnswer:
                url = "https://toster.ru/questions/without_answer";
                break;
            case QuestionsLatest:
                url = "https://toster.ru/questions/latest";
                break;
            case Toster:
                url = "https://toster.ru";
                break;
            case QUESTION:
                url = this.user + "/questions";//https://toster.ru/user/opium/questions
                break;
            case IQUESTION:
                url = this.user + "/iquestions";
                break;
        }

        request = new Request.Builder()
                .url(url+"?page="+number)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final List<QuestionObject> listQues = (ParsingPage.parsQuestion(response.body().string()));
                final List<QuestionObject> list = new ArrayList<QuestionObject>();
                for (int i=0;i<listQues.size();i++){
                    boolean replay=false;
                    for (int a=0;a<listQuestion.size();a++){
                        if (listQues.get(i).getQuestion().equals(listQuestion.get(a).getQuestion())){
                            replay = true;
                            break;
                        }
                    }
                    if (!replay) {
                        list.add(listQues.get(i));
                        listQuestion.add(listQues.get(i));
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        views(list);
                    }
                });
            }
        });
    }
}


