package br.ufpe.cin.android.rss.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity(tableName = "canais")
public class Canal {
    @PrimaryKey @NonNull
    private String urlFeed;
    private String titulo;
    private String descricao;
    private String linkSite;
    private String imagemURL;
    private int imagemLargura;
    private int imagemAltura;
    public List<Noticia> noticias;

    public Canal(String urlFeed, String titulo, String descricao, String linkSite, String imagemURL, int imagemLargura, int imagemAltura, List<Noticia> noticias) {
        this.urlFeed = urlFeed;
        this.titulo = titulo;
        this.descricao = descricao;
        this.linkSite = linkSite;
        this.imagemURL = imagemURL;
        this.imagemLargura = imagemLargura;
        this.imagemAltura = imagemAltura;
        this.noticias = noticias;
    }

    @NonNull
    public String getUrlFeed() {
        return urlFeed;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLinkSite() {
        return linkSite;
    }

    public String getImagemURL() {
        return imagemURL;
    }

    public int getImagemLargura() {
        return imagemLargura;
    }

    public int getImagemAltura() {
        return imagemAltura;
    }

    @Override
    public String toString() {
        return titulo + " => " + linkSite;
    }

//    public class DataConverter {
//        Gson gson = new Gson();
//
//        @TypeConverter
//        public List<String> stringToNoticiaList(String data) {
//            if (data == null) return Collections.emptyList();
//
//            Type listType = new TypeToken<List<String>>() {}.getType();
//
//            return gson.fromJson(data, listType);
//        }
//
//        @TypeConverter
//        public String noticeListToString(List<String> noticias) {
//            if (noticias == null) return (null);
//            return gson.toJson(noticias);
//        }
//    }
}
