package ru.toster.toster.fragmentTab;

import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.toster.toster.R;
import ru.toster.toster.objects.CommentToggleObject;


public class CommentFragment extends DialogFragment {
    private LinearLayout layout;
    private LayoutInflater inflater;
    private List<CommentToggleObject> listCommentToggle;

    public CommentFragment(List<CommentToggleObject> listCommentToggle) {
        this.listCommentToggle = listCommentToggle;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        this.inflater = inflater;
        this.layout = (LinearLayout) view.findViewById(R.id.comment_layout);
        getDialog().setTitle("Комментарии");
        views();
        return view;
    }
    private void views(){
        for (int i=0;i<listCommentToggle.size();i++){
            final View item = inflater.inflate(R.layout.el_comm_list, layout, false);
            ((TextView)item.findViewById(R.id.el_name)).setText(listCommentToggle.get(i).getName() + " " + listCommentToggle.get(i).getDogName());
            ((TextView)item.findViewById(R.id.el_text)).setText(listCommentToggle.get(i).getText(), TextView.BufferType.SPANNABLE);
            ((TextView)item.findViewById(R.id.el_like)).setText("Нравится ("+listCommentToggle.get(i).getLike()+")");//el_date
            ((TextView)item.findViewById(R.id.el_date)).setText(listCommentToggle.get(i).getDate());
            ((TextView)item.findViewById(R.id.el_comm)).setText("Ответить");//el_number_answer
            ((TextView)item.findViewById(R.id.el_comm)).setTextColor(Color.parseColor("#2d72d9"));//el_number_answer
            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


            layout.addView(item);
        }
    }
}
