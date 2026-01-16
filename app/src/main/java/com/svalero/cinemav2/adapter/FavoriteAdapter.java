package com.svalero.cinemav2.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.cinemav2.R;
import com.svalero.cinemav2.domain.MovieDb;
import com.svalero.cinemav2.view.FavoriteDetailView;

import java.util.List;

public class FavoriteAdapter extends RecyclerView .Adapter<FavoriteAdapter.FavoriteHolder>{
    private List<MovieDb> favoriteList;
    public FavoriteAdapter(List<MovieDb> favoriteList){
        this.favoriteList = favoriteList;
    }


    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favoritesview_item, parent, false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
        holder.movieTitle.setText(favoriteList.get(position).getMovieTitleDb());
        holder.genre.setText(favoriteList.get(position).getGenreDb());
        holder.durationMinutes.setText(String.valueOf(favoriteList.get(position).getDurationMinutesDb()));
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class FavoriteHolder extends RecyclerView.ViewHolder{
        private TextView movieTitle;
        private TextView genre;
        private TextView durationMinutes;
        public FavoriteHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.item_fav_movie_title);
            genre = itemView.findViewById((R.id.item_fav_genre));
            durationMinutes = itemView.findViewById((R.id.item_fav_duration_minutes));
            itemView.setOnClickListener(view -> {
                Long IdBd = favoriteList.get(getAdapterPosition()).getIdDb();
                Intent intent = new Intent(itemView.getContext(), FavoriteDetailView.class);
                intent.putExtra("IdBd", IdBd);
                startActivity(itemView.getContext(), intent, null);
            });

        }
    }
}
