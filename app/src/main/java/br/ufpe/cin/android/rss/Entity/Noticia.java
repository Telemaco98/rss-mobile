package br.ufpe.cin.android.rss.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "noticias")
public class Noticia {
    @PrimaryKey @NonNull
    private String link;
    private String titulo;
    private String descricao;
    private List<String> categorias;
    private String data;

    public Noticia(@NonNull String link, String titulo, String descricao, List<String> categorias, String data) {
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

    public List<String> getCategorias() {
        return categorias;
    }

    public String getData() {
        return data;
    }
}
