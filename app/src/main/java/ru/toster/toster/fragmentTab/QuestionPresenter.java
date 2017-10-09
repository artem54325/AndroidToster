package ru.toster.toster.fragmentTab;

import java.util.ArrayList;
import java.util.List;

import ru.toster.toster.Presenter;
import ru.toster.toster.http.HttpCleint;
import ru.toster.toster.http.ParsingPage;
import ru.toster.toster.objects.QuestionObject;


public class QuestionPresenter implements Presenter {
    private final QuestionFragment fragment;
    private List<QuestionObject> listQuestion = new ArrayList<>();
    private final String url;
    private int number = 1;

    public QuestionPresenter(QuestionFragment fragment, String url) {
        this.fragment = fragment;
        this.url = url;
    }

    @Override
    public void getHttp() {
        if (url==null)
            return;
        HttpCleint cleint = new HttpCleint(fragment.getContext(), this);
        cleint.setNumber(number);
        cleint.execute(url);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public void viewsPresent(final String html) {
        if (fragment.getActivity()==null)
            return;
        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    List<QuestionObject> list = ParsingPage.parsQuestion(html);
                    if (listQuestion.size()!=0){
                        list = compareQuest(list);
                    }else{
                        listQuestion = list;
                    }
                    fragment.views(list);
                number++;
            }
        });
    }


    private List<QuestionObject> compareQuest(List<QuestionObject> listQues){
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
        return list;
    }
}
