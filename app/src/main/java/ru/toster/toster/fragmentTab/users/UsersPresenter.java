package ru.toster.toster.fragmentTab.users;

import java.util.ArrayList;
import java.util.List;

import ru.toster.toster.Presenter;
import ru.toster.toster.http.HTTPCleint;
import ru.toster.toster.http.ParsingPage;
import ru.toster.toster.objects.CardObject;
import ru.toster.toster.objects.QuestionObject;


public class UsersPresenter implements Presenter {
    private final UsersFragment fragment;
    private List<CardObject> listCard = new ArrayList<>();
    private String url;
    private int number=1;//Номер страницы сбора данных

    public UsersPresenter(UsersFragment fragment, String url) {
        this.fragment = fragment;
        this.url = url;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public void getHttp() {
        if (url==null)
            return;
        HTTPCleint cleint = new HTTPCleint(fragment.getContext(), this);
        cleint.setNumber(number);
        cleint.execute(url);
    }

    @Override
    public void viewsPresent(final String html) {
        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<CardObject> list = ParsingPage.parsAllUsers(html);
                if (listCard.size()!=0){
                    list = compareCards(list);
                }else{
                    listCard = list;
                }
                fragment.views(list);
                number++;
            }
        });
    }

    private List<CardObject> compareCards(List<CardObject> listCards){
        List<CardObject> list = new ArrayList<CardObject>();
        for (int i=0;i<listCards.size();i++){
            boolean replay=false;
            for (int a=0;a<listCard.size();a++){
                if (listCards.get(i).getQuestion().equals(listCard.get(a).getQuestion())){
                    replay = true;
                    break;
                }
            }
            if (!replay) {
                list.add(listCards.get(i));
                listCard.add(listCards.get(i));
            }
        }
        return list;
    }
}
