package br.ufpe.cin.android.rss.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.ufpe.cin.android.rss.Dao.NoticiaDao;
import br.ufpe.cin.android.rss.Db.AppDatabase;
import br.ufpe.cin.android.rss.Entity.Noticia;
import br.ufpe.cin.android.rss.R;


/**
 * The type RSSService is responsible to the data from  .
 */
public class RSSService extends JobIntentService {
    private static final int JOB_ID = 1;
    private List<Article> articles;

    /**
     * Enqueue work.
     *
     * @param context the context
     * @param intent  the intent
     */
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, RSSService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String linkPreference = prefs.getString( getString(R.string.link_prefs_name), getString(R.string.feed_padrao));

        try { linkPreference = getRssFeed(linkPreference); }
        catch (IOException e) {
            linkPreference = getString(R.string.link_prefs_name);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.link_prefs_name), getString(R.string.feed_padrao)); // If something wrong happens it will save the default value again as preference
            editor.apply();
        }

        Parser p = new Parser.Builder().build();
        p.onFinish(
            new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(Channel channel) {
                    articles = channel.getArticles();

                    AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "rss").build(); // Get the instance of database
                    NoticiaDao noticiaDao = db.noticiaDao();

                    List<Noticia> noticias = Noticia.fromArticlesToNoticias(articles);
                    noticiaDao.insertAll(noticias); // save all news from the link source in database
                }

                @Override
                public void onError(Exception e) {
                    Log.e("RSS_APP", e.getMessage());
                }
            }
        );
        p.execute(linkPreference);
    }

    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed  = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }
}
