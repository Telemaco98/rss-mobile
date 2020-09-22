package br.ufpe.cin.android.rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prof.rssparser.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiaHolder> {
    List<Article> noticias;
    Context context;

    public NoticiaAdapter (Context context, List<Article> noticias) {
        this.context = context;
        this.noticias = noticias;
    }

    @NonNull
    @Override
    public NoticiaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.linha, parent, false);
        NoticiaHolder holder = new NoticiaHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaHolder holder, int position) {
        Article noticia = noticias.get(position);
        holder.title.setText(noticia.getTitle());
        holder.pubDate.setText(noticia.getPubDate());

        if (noticia.getImage() == null || noticia.getImage().trim().equals("")) {
            holder.image.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
            holder.frameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, 0, (float) 0.0));
        } else {
            Picasso.get().load(noticia.getImage()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    public class NoticiaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView pubDate;
        private ImageView image;
        private FrameLayout frameLayout;

        public NoticiaHolder(View noticiaView) {
            super(noticiaView);
            title = noticiaView.findViewById(R.id.titulo);
            pubDate = noticiaView.findViewById(R.id.dataPublicacao);
            image = noticiaView.findViewById(R.id.imagem);
            frameLayout = noticiaView.findViewById(R.id.frameImg);
            noticiaView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //TODO
        }
    }
}
