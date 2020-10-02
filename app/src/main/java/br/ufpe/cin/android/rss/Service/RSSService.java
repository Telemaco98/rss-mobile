package br.ufpe.cin.android.rss.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.util.List;

import br.ufpe.cin.android.rss.Adapter.NoticiaAdapter;
import br.ufpe.cin.android.rss.Dao.NoticiaDao;
import br.ufpe.cin.android.rss.Db.AppDatabase;
import br.ufpe.cin.android.rss.Entity.Noticia;
import br.ufpe.cin.android.rss.R;


public class RSSService extends JobIntentService {
    private static final int JOB_ID = 1;
    private List<Article> articles;
    private RecyclerView recyclerView;
    Handler handler;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, RSSService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i("INFO","Estou rodando o service"); //FIXME

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String link_preference = prefs.getString( getString(R.string.link_prefs_name), getString(R.string.feed_padrao));

        Parser p = new Parser.Builder().build();
        p.onFinish(
            new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(Channel channel) {
                    articles = channel.getArticles();

                    AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "rss").build();
                    NoticiaDao noticiaDao = db.noticiaDao();

                    List<Noticia> noticias = Noticia.fromArticlesToNoticias(articles);
                    noticiaDao.insertAll(noticias);

                    runOnUiThread(() -> {
                        NoticiaAdapter adapter = new NoticiaAdapter(getApplicationContext(), articles);
                        recyclerView.setAdapter(adapter);
                    });
                }

                @Override
                public void onError(Exception e) {
                    Log.e("RSS_APP",e.getMessage());
                }
            }
        );
        p.execute(link_preference);
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();

        recyclerView = findViewById(R.id.content_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration did = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(did);

//
//        recyclerView = findViewById(R.id.content_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        DividerItemDecoration did = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(did);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(RSSService.this, "Job Execution Finished", Toast.LENGTH_SHORT).show();
    }
}
