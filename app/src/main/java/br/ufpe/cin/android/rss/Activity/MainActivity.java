package br.ufpe.cin.android.rss.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import br.ufpe.cin.android.rss.Adapter.NoticiaAdapter;
import br.ufpe.cin.android.rss.R;

public class MainActivity extends AppCompatActivity {
    private String link_preference;
    private List<Article> noticias;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.content_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration did = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(did);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        link_preference = prefs.getString( getString(R.string.link_prefs_name), getString(R.string.feed_padrao));
        Log.i("INFO", link_preference);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        link_preference = prefs.getString( getString(R.string.link_prefs_name), getString(R.string.feed_padrao));

        updateNews();
    }

    private void updateNews() {
        Parser p = new Parser.Builder().build();
        p.onFinish(
                new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Channel channel) {
                        noticias = channel.getArticles();
                        runOnUiThread(
                                () -> {
                                    NoticiaAdapter adapter = new NoticiaAdapter(getApplicationContext(), noticias);
                                    recyclerView.setAdapter(adapter);
                                }
                        );
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("RSS_APP",e.getMessage());
                    }
                }
        );
        p.execute(link_preference);
        /*
        new Thread(
                () -> {
                    try {
                        String conteudo = getRssFeed(RSS_FEED);
                        //precisa rodar de uma UI thread
                        runOnUiThread(
                                () -> conteudoRSS.setText(conteudo)
                        );

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
        */
    }

    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
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

    public void onclick(MenuItem item) {
        if (item.getItemId() == R.id.action_preferences) {
            Intent intent = new Intent(this, PreferenciasActivity.class);
            startActivity(intent);
        }
    }
}