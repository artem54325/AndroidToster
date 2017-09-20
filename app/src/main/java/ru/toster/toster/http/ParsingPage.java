package ru.toster.toster.http;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.toster.toster.objects.CardObject;
import ru.toster.toster.objects.CommentAnswerObject;
import ru.toster.toster.objects.CommentToggleObject;
import ru.toster.toster.objects.NameAndTagFullInfoObject;
import ru.toster.toster.objects.QuestionObject;
import ru.toster.toster.objects.QuestionPageObject;

public class ParsingPage {

    public static List<CardObject> parsAllUsers(String html){
        List<CardObject> cardsList = new ArrayList<>();
        String[] listString = html.split("class=\"content-list__item\"");//.split("class=\"content-list content-list_cards-users\"")[1].split("class=\"paginator\"")[0]
        for (int i=1;i<listString.length;i++){
            CardObject object = new CardObject();
            if (listString[i].contains("<img src=\""))
                object.setUrlImage(listString[i].split("<img src=\"")[1].split("\"")[0]);
            else
                object.setUrlImage(null);
            if (!listString[i].contains("<meta itemprop=\"name\" content=\""))
                continue;
            object.setTag(listString[i].split("<meta itemprop=\"name\" content=\"")[1].split("\">")[0]);//Имя
            if (listString[i].split("<meta itemprop=\"interactionCount\" content=").length==3)
                object.setQuestion(listString[i].split("<meta itemprop=\"interactionCount\" content=")[1].split("\">")[1].split("</a>")[0].replaceAll("  ","").replaceAll("\n","") + " * " +
                    listString[i].split("<meta itemprop=\"interactionCount\" content=")[2].split("\">")[1].split("</a>")[0].replaceAll("  ","").replaceAll("\n",""));//Вопросы и ответы
            else{
                object.setQuestion(listString[i].split("<meta itemprop=\"interactionCount\" content=")[1].split("\">")[1].split("</a>")[0].replaceAll("  ","").replaceAll("\n","") + " * 0 вопросов");
            }
            object.setSubscribeNumber(listString[i].split("<strong>")[1].split("<")[0]+" Общий вклад");//Общий вклад   <strong>
            object.setProgressBar(Double.parseDouble(listString[i].split("data-progress=\"")[1].split("\"")[0]));//
            object.setHref(listString[i].split("card__head-image card__head-image_user\" href=\"")[1].split("\">")[0]);


            cardsList.add(object);
        }
        return cardsList;
    }

    public static NameAndTagFullInfoObject parsFullTag(String html){
        NameAndTagFullInfoObject full = new NameAndTagFullInfoObject();

        full.setName(html.split("<title>Информация по тегу «")[1].split("».")[0]);

        full.setUrlPhoto(html.split("<meta property=\"og:image\" content=\"")[1].split("\">")[0].replaceAll("\n","").replaceAll("  ", ""));//

        if (html.contains("<div class=\"page-header__subtitle\" itemprop=\"jobTitle\">"))
            full.setDopName(html.split("<div class=\"page-header__subtitle\" itemprop=\"jobTitle\">")[1].split("</div>")[0].replaceAll("\n","").replaceAll("  ", ""));
        if (html.contains("<div class=\"mini-counter__count mini-counter__count-contribution\">"))
            full.setContribution(html.split("<div class=\"mini-counter__count mini-counter__count-contribution\">")[1].split("</div>")[0] + "\n" +
                html.split("<div class=\"mini-counter__value\">")[1].split("</div>")[0]);//<div class="mini-counter__value">

        full.setQuestion(html.split("<div class=\"mini-counter__count\">")[1].split("</div>")[0].replaceAll("\n","").replaceAll("  ", "") + "\n" +
                html.split("<div class=\"mini-counter__value\">")[2].split("</div>")[0].replaceAll("\n","").replaceAll("  ", ""));

        full.setAnswer(html.split("<div class=\"mini-counter__count\"")[2].split("</div>")[0].replaceAll("\n","").replaceAll("  ", "") + "\n" +
                html.split("<div class=\"mini-counter__value\">")[3].split("</div>")[0].replaceAll("\n","").replaceAll("  ", ""));

        full.setDecisions(html.split("<div class=\"mini-counter__count mini-counter__count-solutions\">")[1].split("</div>")[0].replaceAll("\n","").replaceAll("  ", "") + "\n" +
                html.split("<div class=\"mini-counter__value\">")[4].split("</div>")[0].replaceAll("\n","").replaceAll("  ", ""));

        full.setTextInfo(html.split("<div class=\"page__body\">")[1].split("<section class=\"section section_profile\">")[0].replaceAll("\n","").replaceAll("  ", "") + "\n");


        return full;
    }

    public static NameAndTagFullInfoObject parsFullName(String html){
        NameAndTagFullInfoObject full = new NameAndTagFullInfoObject();

        full.setName(html.split("<meta property=\"og:title\" content=\"Пользователь ")[1].split("\">")[0].replaceAll("\n","").replaceAll("  ", ""));
        full.setUrlPhoto(html.split("<meta property=\"og:image\" content=\"")[1].split("\">")[0].replaceAll("\n","").replaceAll("  ", ""));//

        if (html.contains("<div class=\"page-header__subtitle\" itemprop=\"jobTitle\">"))
            full.setDopName(html.split("<div class=\"page-header__subtitle\" itemprop=\"jobTitle\">")[1].split("</div>")[0].replaceAll("\n","").replaceAll("  ", ""));
        full.setContribution(html.split("<div class=\"mini-counter__count mini-counter__count-contribution\">")[1].split("</div>")[0] + "\n" +
                html.split("<div class=\"mini-counter__value\">")[1].split("</div>")[0]);//<div class="mini-counter__value">

        full.setQuestion(html.split("<div class=\"mini-counter__count\">")[1].split("</div>")[0].replaceAll("\n","").replaceAll("  ", "") + "\n" +
                html.split("<div class=\"mini-counter__value\">")[2].split("</div>")[0].replaceAll("\n","").replaceAll("  ", ""));

        full.setAnswer(html.split("<div class=\"mini-counter__count\"")[2].split("</div>")[0].replaceAll("\n","").replaceAll("  ", "") + "\n" +
                html.split("<div class=\"mini-counter__value\">")[3].split("</div>")[0].replaceAll("\n","").replaceAll("  ", ""));

        full.setDecisions(html.split("<div class=\"mini-counter__count mini-counter__count-solutions\">")[1].split("</div>")[0].replaceAll("\n","").replaceAll("  ", "") + "\n" +
                html.split("<div class=\"mini-counter__value\">")[4].split("</div>")[0].replaceAll("\n","").replaceAll("  ", ""));
        
        full.setTextInfo(html.split("<div class=\"page__body\">")[1].split("<section class=\"section section_profile\">")[0].replaceAll("\n","").replaceAll("  ", "") + "\n");
        return full;
    }

    public static List<CardObject> parsAlltag(String html){
        List<CardObject> cardList = new ArrayList<>();

        String[] list = html.split("class=\"content-list__item\"");//.split("class=\"content-list content-list_cards-tags\"")[1].split("class=\"paginator\"")[0]
        for (int i=1;i<list.length;i++){
            try {
                CardObject cardObject = new CardObject();
                try {
                    cardObject.setUrlImage(list[i].split("<img class=\"tag__image tag__image_bg\" src=\"")[1].split("\"")[0]);
                }catch (Exception e){
                    cardObject.setUrlImage(null);
                    e.printStackTrace();
                }
                cardObject.setQuestion(list[i].split("<meta itemprop=\"interactionCount\" content=\"")[1].split("\">")[1].split("</a>")[0].replaceAll("  ", "").replaceAll("\n",""));
//            cardObject.setQuestionNumber(Integer.parseInt(list[i].split("")[1].split("")[0]));
                cardObject.setSubscribe(false);
                cardObject.setTag(list[i].split("<meta itemprop=\"name\" content=\"")[1].split("\">")[0]);
                cardObject.setSubscribeNumber(list[i].split("<span class=\"btn__counter\" role=\"subscribers_count\" title=\"")[1].split("\">")[2].split("</span>")[0].replaceAll("  ", "").replaceAll("\n",""));
                cardObject.setHref(list[i].split("card__head-image card__head-image_tag\" href=\"")[1].split("\">")[0]);//card__head-image card__head-image_tag" href=" ">

                cardList.add(cardObject);
            }catch (java.lang.ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                break;
            }
        }
        return cardList;
    }

    public static List<QuestionObject> parsQuestion(String html){
        List<QuestionObject> questionObjectList = new ArrayList<>();
//        Set<QuestionObject> questionObjectList = new HashSet<>();

            String[] list = html.split("<li class=\"content-list__item\" role=\"content-list_item\">");
            for (int i=1;i<list.length;i++){
                try {
                questionObjectList.add(new QuestionObject(
                        list[i].split("https://toster.ru/tag/")[1].split("\">")[0],
                        list[i].split("<a class=\"question__title-link question__title-link_list\" href=\"")[1].split("\" itemprop=\"url\">")[0],
                        list[i].split("\" itemprop=\"url\">")[1].split(" </a>")[0].replaceAll("  ", "").replaceAll("\n",""),//сам вопрос
                        list[i].split(" subscribers\">")[1].split("</span>")[0].replaceAll("  ", "").replaceAll("\n", ""),//кол-во Подписчики
                        list[i].split("<time class=\"question__date question__date_publish\" pubdate=\"\" title=\"")[1].split("</time>")[0].split("\">")[1].replaceAll("  ", "").replaceAll("\n",""),//Время
//                        Integer.parseInt(list[i].split("<meta itemprop=\"interactionCount\" content=\"")[2].split(" views\">")[0].replaceAll(" ", "")),//Просмотры   views">
                        list[i].split(" views\">")[1].split("</span>")[0].replaceAll("  ", "").replaceAll("\n",""),
                        list[i].contains("class=\"icon_svg icon_check\""),
                        Integer.parseInt(list[i].split("<span itemprop=\"answerCount\">")[1].split("</span>")[0].replaceAll(" ", "").replaceAll("\n", ""))));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        return questionObjectList;
    }
    public static QuestionPageObject getQuestPage(String html){
        QuestionPageObject page = new QuestionPageObject();
        page.setName(html.split("<meta itemprop=\"name\" content=\"")[1].split("\">")[0]);
        page.setDogName("@" + html.split("<meta itemprop=\"alternateName\" content=\"")[1].split("\">")[0]);
        page.setTag(html.split("<meta property=\"og:keywords\" content=\"")[1].split("\">")[0]);
        page.setTextName(html.split("<meta name=\"twitter:title\" content=\"")[1].split("\">")[0]);
        page.setText(html.split("<div class=\"question__text\" itemprop=\"text description\">")[1].replace("    ", "").split("</div>")[0].replaceAll("<br/>", "\n"));
        page.setDate(html.split("<time class=\"question__date question__date_publish\" pubdate=\"\" title=\"")[1].split("\" itemprop")[0] + "\nПросмотры (" + html.split("<meta itemprop=\"interactionCount\" content=\"")[1].split(" views\">")[0]+")");
        page.setLike(Integer.parseInt(html.split("<meta itemprop=\"interactionCount\" content=\"")[2].split(" ")[0]));

        if (html.contains("<ul class=\"content-list content-list_comments \" role=\"question_comments_list\""))
            page.setToggleObjects(getToggleObject(html.split("<ul class=\"content-list content-list_comments \" role=\"question_comments_list\"")[1].split("</ul>")[0]));
//<div class="section section_solutions


        html = html.split("<div class=\"section section_solutions")[1];
        String[] listComment= html.split("<ul class=\"content-list content-list_answers\" id=\"answers_list\">")[0].split("<li class=\"content-list__item\" role=\"answer_item \" id=\"");
        List<CommentAnswerObject> answerList = new ArrayList<>();
        for (int i=1;i<listComment.length;i++){
            CommentAnswerObject answerObject = new CommentAnswerObject();
            answerObject.setToggleObjects(getToggleObject(listComment[i]));
            answerObject.setUrlImage(null);
            answerObject.setText(listComment[i].split("<div class=\"answer__text\" itemprop=\"text\">")[1].replace("\n        ", "").split("</")[0].replaceAll("<br/>","\n"));
            answerObject.setName(listComment[i].split("<meta itemprop=\"name\" content=\"")[1].split("\">")[0]);
            answerObject.setDogName("@" + listComment[i].split("<meta itemprop=\"alternateName\" content=\"")[1].split("\">")[0]);
            answerObject.setDate(listComment[i].split("title=\"Дата публикации: ")[1].split("\">")[0]);
            answerObject.setNumberUserComments(answerObject.getToggleObjects().size());

            answerList.add(answerObject);
        }
        page.setAnswerObjectsRight(answerList);

        html = html.split("<ul class=\"content-list content-list_answers\" id=\"answers_list\">")[1];
        listComment= html.split("<li class=\"content-list__item\" role=\"answer_item \" id=\"");
        answerList = new ArrayList<>();
        for (int i=1;i<listComment.length;i++){
            CommentAnswerObject answerObject = new CommentAnswerObject();
            answerObject.setToggleObjects(getToggleObject(listComment[i]));
            answerObject.setUrlImage(null);
            answerObject.setText(listComment[i].split("<div class=\"answer__text\" itemprop=\"text\">")[1].replace("\n        ", "").split("</")[0].replaceAll("<br/>","\n"));
            answerObject.setName(listComment[i].split("<meta itemprop=\"name\" content=\"")[1].split("\">")[0]);
            answerObject.setDogName("@" + listComment[i].split("<meta itemprop=\"alternateName\" content=\"")[1].split("\">")[0]);
            answerObject.setDate(listComment[i].split("title=\"Дата публикации: ")[1].split("\">")[0]);
            answerObject.setNumberUserComments(answerObject.getToggleObjects().size());

            answerList.add(answerObject);
        }
        page.setAnswerObjects(answerList);
        page.setLike(false);
        return page;
    }


    private static List<CommentToggleObject> getToggleObject(String html){

        List<CommentToggleObject> list = new ArrayList<>();
        String[] listHTML = html.split("<li class=\"content-list__item\" role=\"comments_item\">");
        for (int i=1;i<listHTML.length;i++){
            CommentToggleObject toggleObject = new CommentToggleObject();
            toggleObject.setLike("0");
            toggleObject.setLike(false);
            toggleObject.setDate(listHTML[i].split("<time title=\"Дата публикации: ")[1].split("\" ")[0]);
            toggleObject.setDogName("@" + listHTML[i].split("<meta itemprop=\"alternateName\" content=\"")[1].split("\">")[0]);
            toggleObject.setName(listHTML[i].split("<meta itemprop=\"name\" content=\"")[1].split("\">")[0].replaceAll("<br/>", "\n"));
            if (listHTML[i].contains("<div class=\"comment__text\" itemprop=\"about text\">")){
                toggleObject.setText(listHTML[i].split("<div class=\"comment__text\" itemprop=\"about text\">\n            ")[1].split("</div>")[0].replaceAll("<br/>", "\n"));
            }else{
                toggleObject.setText(listHTML[i].split("<div class=\"comment__text\" itemprop=\"commentText\">\n            ")[1].split("</div>")[0].replaceAll("<br/>", "\n"));
            }
            toggleObject.setUrlImage(null);

            list.add(toggleObject);
        }
        return list;
    }
}
