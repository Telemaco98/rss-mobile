package DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import br.ufpe.cin.android.rss.Entity.Canal;

@Dao
public interface CanalDAO {
    @Query("SELECT * FROM canais")
    List<Canal> getAll();

    @Insert
    void insertAll(Canal... canais);

    @Delete
    void delete(Canal canal);
}
