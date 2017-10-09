package ru.toster.artem.fragmentTab.textview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class HtmlImageGet implements Html.ImageGetter{
    static final Map<String, WeakReference<Drawable>> mDrawableCache = Collections.synchronizedMap(new WeakHashMap<String, WeakReference<Drawable>>());

    private String text;
    private TextView view;
    private AppCompatActivity activity;

    public HtmlImageGet(String text, TextView view, AppCompatActivity activity) {
        this.text = text;
        this.view = view;
        this.activity = activity;
    }

    Html.ImageGetter igCached = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            //Просто возвращаем наш рисунок из кеша
            if (mDrawableCache.containsKey(source))
                return mDrawableCache.get(source).get();
            return null;
        }
    };

    @Override
    public Drawable getDrawable(String source) {
        if (mDrawableCache.containsKey(source))
            return mDrawableCache.get(source).get();
        //В противном случае, скачиваем его из сети
        new ImageDownloadAsyncTask(source, text, view).execute();
        //Пока он скачивается устанавливаем пустой рисунок
        return new BitmapDrawable(activity.getResources());
    }

    class ImageDownloadAsyncTask extends AsyncTask<Void, Void, Void> {
        private String source;
        private String message;
        private TextView textView;

        public ImageDownloadAsyncTask(String source, String message,
                                      TextView textView) {
            this.source = source;
            this.message = message;
            this.textView = textView;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (!mDrawableCache.containsKey(source)) {
                try {
                    //Скачиваем картинку в наш кэш
                    URL url = new URL(source);
                    URLConnection connection = url.openConnection();
                    InputStream is = connection.getInputStream();

//                    Drawable drawable = Drawable.createFromStream(is, "src");

                    // Если нужно, чтобы рисунки не масштабировались,
                    // закомментируйте строчку выше и расскомментируйте код
                    // ниже.

                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    DisplayMetrics dm =
                            activity.getResources().getDisplayMetrics();
                    bmp.setDensity(dm.densityDpi); Drawable drawable=new
                            BitmapDrawable(activity.getResources(),bmp);

                    is.close();

                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight());
                    mDrawableCache.put(source, new WeakReference<Drawable>(
                            drawable));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Переустанавливаем содержимое нашего поля
            textView.setText(Html.fromHtml(message, igCached, new HtmlTextTag()));
        }
    }
}
