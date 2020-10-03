package br.ufpe.cin.android.rss.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import br.ufpe.cin.android.rss.Entity.Noticia;
import br.ufpe.cin.android.rss.R;

/**
 * The class Noticia adapter serves to organize a list of items in the screen through RecyclerView.
 */
public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiaHolder> {
    List<Noticia> noticias; /** A list of news **/
    Context context; /** The context **/

    /**
     * Parametric constructor of NoticiaAdapter class
     * @param context
     * @param noticias
     */
    public NoticiaAdapter (Context context, List<Noticia> noticias) {
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
        Noticia noticia = noticias.get(position);
        holder.currentLink = noticia.getLink();
        holder.title.setText(noticia.getTitulo());
        holder.pubDate.setText(noticia.getData());

        if (noticia.getImg() == null || noticia.getImg().trim().equals("")) {
            holder.image.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
            holder.frameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, 0, (float) 0.0));
        } else {
            Picasso.get().load(noticia.getImg()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    /**
     * NoticiaHolder class determines and displays the components of a list item
     */
    public class NoticiaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView pubDate;
        private ImageView image;
        private FrameLayout frameLayout;
        private String currentLink;

        public NoticiaHolder(View noticiaView) {
            super(noticiaView);
            title = noticiaView.findViewById(R.id.titulo);
            pubDate = noticiaView.findViewById(R.id.dataPublicacao);
            image = noticiaView.findViewById(R.id.imagem);
            frameLayout = noticiaView.findViewById(R.id.frameImg);
            noticiaView.setOnClickListener(this);
            currentLink = "";
        }

        @Override
        public void onClick(View view) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentLink));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }
    }
}
