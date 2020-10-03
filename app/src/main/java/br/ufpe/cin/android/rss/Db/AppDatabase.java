package br.ufpe.cin.android.rss.Db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.ufpe.cin.android.rss.Dao.NoticiaDao;
import br.ufpe.cin.android.rss.Entity.Noticia;

/**
 * The class that serves as database instance.
 */
@Database(entities = {Noticia.class}, version = 1, exportSchema = false)
@TypeConverters({Noticia.DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "rss.db";
    public abstract NoticiaDao noticiaDao();

    private static volatile AppDatabase INSTANCE;
    public synchronized static AppDatabase getInstance(Context c) {
        if (INSTANCE == null) INSTANCE = Room.databaseBuilder(c, AppDatabase.class, DB_NAME).build();
        return INSTANCE;
    }
}