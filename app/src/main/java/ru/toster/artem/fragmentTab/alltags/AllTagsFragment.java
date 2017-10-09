package ru.toster.artem.fragmentTab.alltags;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.toster.artem.fragmentTab.userandtag.UserAndTagActivity;
import ru.toster.artem.http.DowlandImage;
import ru.toster.artem.objects.CardObject;
import ru.toster.toster.R;


public class AllTagsFragment extends Fragment implements ViewTreeObserver.OnScrollChangedListener, SwipeRefreshLayout.OnRefreshListener {

    private AllTagsPresenter presenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean dowlandPage = false;

    @BindView(R.id.scroll_question_fragment)
    ScrollView scrollView;

    @BindView(R.id.scroll_container)
    LinearLayout layout;

    LayoutInflater inflater;

    @Override
    public void onScrollChanged() {
        if (!dowlandPage) {
            if (layout.getHeight() - (scrollView.getHeight() + scrollView.getScrollY())<=1500&&scrollView.getScrollY()>=500) {
                dowlandPage = true;
                presenter.getHttp();
            }
        }
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, view);

        String url = null;
        Bundle bundle = this.getArguments();
        if (bundle!=null)
            url = bundle.getString("url", null);

        presenter = new AllTagsPresenter(url, this);

        this.inflater = inflater;

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        presenter.getHttp();

        return view;
    }

    public void views(final List<CardObject> list) {
        LinearLayout layoutVert = null;
        LinearLayout.LayoutParams layoutParams = null;
            for (int i = 0; i < list.size(); i++) {
                if (i % 2 == 0) {
                    layoutVert = new LinearLayout(getContext());
                    layoutVert.setOrientation(LinearLayout.HORIZONTAL);
                    layoutParams = new LinearLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
                }

                final View item = inflater.inflate(R.layout.el_card, this.layout, false);
                new DowlandImage((ImageView) item.findViewById(R.id.tag_url_image)).execute(list.get(i).getUrlImage());//((ImageView)item.findViewById(R.id.tag_url_image)
                ((TextView) item.findViewById(R.id.tag_name)).setText(list.get(i).getTag());
                ((TextView) item.findViewById(R.id.tag_question)).setText(list.get(i).getQuestion());
                ((TextView) item.findViewById(R.id.tag_subscriptions)).setText("Подписаться " + list.get(i).getSubscribeNumber());
                ((LinearLayout) item.findViewById(R.id.users_layout)).setVisibility(View.GONE);

                Display display = getActivity().getWindowManager().getDefaultDisplay();

                final int finalI = i;
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), UserAndTagActivity.class);
                        intent.putExtra("url", list.get(finalI).getHref());
                        intent.putExtra("tag_and_user", true);
                        startActivity(intent);
                    }
                });

                item.getLayoutParams().width = (int) ((display.getWidth() / 2) - 2.5);

                layoutVert.addView(item);
                if (i % 2 == 1) {
                    layout.addView(layoutVert, layoutParams);
                }
            }
        dowlandPage = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
