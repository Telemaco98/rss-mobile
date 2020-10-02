package br.ufpe.cin.android.rss.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.ufpe.cin.android.rss.Entity.Noticia;

@Dao
public interface NoticiaDao {
    @Query("SELECT * FROM noticias")
    List<Noticia> getAll();

    @Insert
    void insertAll(Noticia... noticias);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Noticia> noticias);

    @Delete
    void delete(Noticia noticia);
}
