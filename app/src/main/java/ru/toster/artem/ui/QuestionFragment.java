package ru.toster.artem.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.toster.artem.objects.QuestionObject;
import ru.toster.toster.R;


public class QuestionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ViewTreeObserver.OnScrollChangedListener{//
    private QuestionPresenter presenter;

    private boolean dowlandPage=false;
    @BindView(R.id.scroll_question_fragment)
    ScrollView scrollView;
    @BindView(R.id.scroll_container)
    LinearLayout layout;
    private LayoutInflater inflater;
//    @BindView(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        ButterKnife.bind(this, view);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout) ;

        mSwipeRefreshLayout.setOnRefreshListener(this);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        String url = null;

        Bundle bundle = this.getArguments();

        if (bundle!=null) url = bundle.getString("url", null);

        presenter = new QuestionPresenter(this, url);

        dowlandPage = true;

        presenter.getHttp();

        return view;
    }

    public void views(final List<QuestionObject> list) {
        for (final QuestionObject questionObject:list){
            final View item = inflater.inflate(R.layout.element_list_news, layout, false);
            ((TextView)item.findViewById(R.id.el_tag)).setText(questionObject.getTag());
            ((TextView)item.findViewById(R.id.el_answer)).setText("Ответов:");
            ((TextView)item.findViewById(R.id.el_number_answer)).setText(String.valueOf(questionObject.getAnswer()));
            if (questionObject.isAnswer()){
                ((TextView)item.findViewById(R.id.el_number_answer)).setTextColor(Color.parseColor("#65c178"));
            }
            ((TextView)item.findViewById(R.id.el_date)).setText(questionObject.getDate());
            ((TextView)item.findViewById(R.id.el_question)).setText(questionObject.getQuestion());
            ((TextView)item.findViewById(R.id.el_views)).setText(questionObject.getViews());
//            questionObject.e
            ((TextView)item.findViewById(R.id.el_subscribers)).setText(questionObject.getSubscribers());

            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PostAppCompat.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", questionObject.getHref());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            layout.addView(item);
        }
        dowlandPage = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        if (!dowlandPage) {
            dowlandPage = true;
            presenter.setNumber(1);
            presenter.getHttp();
        }
    }

    @Override
    public void onScrollChanged() {
        if (!dowlandPage) {
            if (layout.getHeight() - (scrollView.getHeight() + scrollView.getScrollY())<=2000&&scrollView.getScrollY()>=500) {
                dowlandPage = true;
                presenter.getHttp();
        }
    }
}}


