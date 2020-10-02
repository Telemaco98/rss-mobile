package br.ufpe.cin.android.rss.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.prof.rssparser.Article;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "noticias")
public class Noticia {
    @PrimaryKey @NonNull
    private String link;
    private String titulo;
    private String descricao;
    @TypeConverters(DataConverter.class)
    public List<String> categorias;
    private String data;

    public Noticia(@NonNull String link, String titulo, String descricao, @TypeConverters(DataConverter.class) List<String> categorias, String data) {
        this.link = link;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categorias = categorias;
        this.data = data;
    }

    @NonNull
    public String getLink() {
        return link;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    @TypeConverters(DataConverter.class)
    public List<String> getCategorias() {
        return categorias;
    }

    public String getData() {
        return data;
    }

    public static Noticia fromArticleToNoticia (Article article) {
        return new Noticia(Objects.requireNonNull(article.getLink()), article.getTitle(), article.getDescription(), article.getCategories(), article.getPubDate());
    }

    public static List<Noticia> fromArticlesToNoticias (List<Article> articles) {
        List<Noticia> noticias = new ArrayList<>();
        for (Article a : articles)
            noticias.add(fromArticleToNoticia(a));

        return noticias;
    }

    public static class DataConverter {

        @TypeConverter
        public String fromCountryLangList(List<String> categorias) {
            if (categorias == null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {}.getType();
            String json = gson.toJson(categorias, type);
            return json;
        }

        @TypeConverter
        public List<String> toCountryLangList(String categoriasLangString) {
            if (categoriasLangString == null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {}.getType();
            List<String> categorias = gson.fromJson(categoriasLangString, type);
            return categorias;
        }
    }
}
