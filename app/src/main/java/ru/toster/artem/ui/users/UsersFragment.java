package ru.toster.artem.ui.users;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.toster.toster.R;
import ru.toster.artem.ui.userandtag.UserAndTagActivity;
import ru.toster.artem.utility.DowlandImage;
import ru.toster.artem.objects.CardObject;

public class UsersFragment extends Fragment implements ViewTreeObserver.OnScrollChangedListener, SwipeRefreshLayout.OnRefreshListener {
    private LayoutInflater inflater;
    private UsersPresenter fragment;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean dowlandPage = false;

    @BindView(R.id.scroll_question_fragment)
    ScrollView scrollView;

    @BindView(R.id.scroll_container)
    LinearLayout layout;

    public static final String TAG = "UsersFragment";


    @Override
    public void onScrollChanged() {
        if (!dowlandPage) {
            if (layout.getHeight() - (scrollView.getHeight() + scrollView.getScrollY())<=1500&&scrollView.getScrollY()>=500) {
                dowlandPage = true;
                fragment.getHttp();
            }
        }
    }

    @Override
    public void onRefresh() {
        if (!dowlandPage) {
            dowlandPage = true;
            fragment.setNumber(1);
            fragment.getHttp();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = null;
        Bundle bundle = getArguments();
        if (bundle!=null)
            url = bundle.getString("url");
        fragment = new UsersPresenter(this, url);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);//fragment_all_tags
        ButterKnife.bind(this, view);

        layout.removeAllViews();

        this.inflater = inflater;

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        fragment.getHttp();

        return view;
    }

    public void views(final List<CardObject> list) {
        LinearLayout layoutVert = null;
        LinearLayout.LayoutParams layoutParams = null;
        for (int i=0;i<list.size();i++){
            if (i%2==0){
                layoutVert = new LinearLayout(getContext());
                layoutVert.setOrientation(LinearLayout.HORIZONTAL);
                layoutParams = new LinearLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
            }

            final View item = inflater.inflate(R.layout.el_card, this.layout, false);
            if (!(list.get(i).getUrlImage() == null))
                new DowlandImage((ImageView)item.findViewById(R.id.tag_url_image)).execute(list.get(i).getUrlImage());//((ImageView)item.findViewById(R.id.tag_url_image)
            ((TextView)item.findViewById(R.id.tag_name)).setText(list.get(i).getTag());
            ((TextView)item.findViewById(R.id.tag_question)).setText(list.get(i).getQuestion());
//            ((TextView)item.findViewById(R.id.tag_subscriptions)).setText("Подписаться "+list.get(i).getSubscribeNumber());
            ((TextView)item.findViewById(R.id.tag_subscriptions)).setVisibility(View.GONE);

            ((TextView)item.findViewById(R.id.users_overall_contribution)).setText(list.get(i).getSubscribeNumber());

            ((ProgressBar)item.findViewById(R.id.progressBar)).setProgress((int) list.get(i).getProgressBar());
            ((ProgressBar)item.findViewById(R.id.progressBar)).setMax(100);

            Display display = getActivity().getWindowManager().getDefaultDisplay();

            item.getLayoutParams().width= (int) ((display.getWidth()/2)-2.5);

            final int finalI = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UserAndTagActivity.class);
                    intent.putExtra("url", list.get(finalI).getHref());
                    startActivity(intent);
                }
            });

            layoutVert.addView(item);
            if (i%2==1){
                layout.addView(layoutVert, layoutParams);
            }
        }
        dowlandPage = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
