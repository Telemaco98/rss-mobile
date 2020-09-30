package DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import br.ufpe.cin.android.rss.Entity.Noticia;

@Dao
public interface NoticiaDAO {
    @Query("SELECT * FROM noticias")
    List<Noticia> getAll();

    @Insert
    void insertAll(Noticia... noticias);

    @Delete
    void delete(Noticia noticia);
}
