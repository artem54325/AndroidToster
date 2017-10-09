package ru.toster.artem.fragmentTab.alltags;

import java.util.ArrayList;
import java.util.List;

import ru.toster.artem.Presenter;
import ru.toster.artem.http.HttpCleint;
import ru.toster.artem.http.ParsingPage;
import ru.toster.artem.objects.CardObject;


public class AllTagsPresenter implements Presenter {
    private final String url;
    private final AllTagsFragment fragment;
    private int number = 1;
    private List<CardObject> listCard = new ArrayList<>();

    public AllTagsPresenter(String url, AllTagsFragment fragment) {
        this.url = url;
        this.fragment = fragment;
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
        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<CardObject> list = ParsingPage.parsAlltag(html);
                if (listCard.size()!=0){
                    list = compare(list);
                }else{
                    listCard = list;
                }
                fragment.views(list);
                number++;
            }
        });
    }

    private List<CardObject> compare(List<CardObject> listCards){
        final List<CardObject> list = new ArrayList<CardObject>();
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
