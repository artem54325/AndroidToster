package ru.toster.toster.http;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.toster.toster.Presenter;
import ru.toster.toster.objects.QuestionPageObject;

public class HttpCleint extends AsyncTask<String, Void, String> {
    private Context context;
    private int number = 1;
    private final Presenter presenter;


    public HttpCleint(Context context, Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    private static OkHttpClient client= new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url, cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url);
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .build();
    private static Request request = null;

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    protected String doInBackground(String... urlHttp) {
        String url = urlHttp[0];
        if (url==null)
            return null;
        if (number!=1)
            url = url + "?page=" + number;
        request = new Request.Builder()
                .url(url)
                .build();
        try {
            return client.newCall(request).execute().body().string();
        } catch (IOException e) {
            Toast.makeText(context, "Нет подключения к интернету, проверти сеть",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        presenter.viewsPresent(s);
        super.onPostExecute(s);
    }
}