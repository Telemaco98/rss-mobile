package br.ufpe.cin.android.rss.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.ufpe.cin.android.rss.Adapter.NoticiaAdapter;
import br.ufpe.cin.android.rss.Dao.NoticiaDao;
import br.ufpe.cin.android.rss.Db.AppDatabase;
import br.ufpe.cin.android.rss.Entity.Noticia;
import br.ufpe.cin.android.rss.R;
import br.ufpe.cin.android.rss.Service.RSSService;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {
    private List<Noticia> noticias;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.content_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration did = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL); // It divides the items from RecyclerView
        recyclerView.addItemDecoration(did);

        onStartJobIntentService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onStartJobIntentService(); // call the service
        updateNews(); // update the data of news through the link preferences source
    }

    /**
     * Method that request the data from link source and save them in the local database
     */
    private void updateNews() {
        new Thread(
            () -> {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "rss").build();
                NoticiaDao noticiaDao = db.noticiaDao();

                noticias = noticiaDao.getAll();
                noticiaDao.insertAll(noticias);

                runOnUiThread(
                    () -> {
                        NoticiaAdapter adapter = new NoticiaAdapter(getApplicationContext(), noticias);
                        recyclerView.setAdapter(adapter);
                    }
                );
            }
        ).start();
    }

    /**
     * This method serves to add a functionality when the user click in the preference intem menu
     *
     * @param item the item
     */
    public void onclick(MenuItem item) {
        if (item.getItemId() == R.id.action_preferences) {
            Intent intent = new Intent(this, PreferenciasActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Method call the service to it start.
     */
    public void onStartJobIntentService () {
        Intent intent = new Intent(this, RSSService.class);
        RSSService.enqueueWork(this, intent);
    }
}