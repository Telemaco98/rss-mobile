package br.ufpe.cin.android.rss.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
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

import br.ufpe.cin.android.rss.NoticiaAdapter;
import br.ufpe.cin.android.rss.R;

public class MainActivity extends AppCompatActivity {
    private final String RSS_FEED = "https://g1.globo.com/dynamo/rss2.xml";

    //ListView conteudoRSS;
    List<Article> noticias;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.content_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration did = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(did);

        //conteudoRSS = findViewById(R.id.conteudoRSS);
    }

    @Override
    protected void onStart() {
        super.onStart();

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
        p.execute(RSS_FEED);
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
}