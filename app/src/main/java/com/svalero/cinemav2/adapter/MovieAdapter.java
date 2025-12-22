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
import com.svalero.cinemav2.domain.Movie;
import com.svalero.cinemav2.view.MovieDetailView;

import java.util.List;

public class MovieAdapter  extends RecyclerView .Adapter<MovieAdapter.MovieHolder>{
    private List<Movie> movieList;
    public MovieAdapter(List<Movie> movieList){
        this.movieList = movieList;
    }


    @NonNull
    @Override
    public MovieAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moviesview_item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieHolder holder, int position) {
        holder.movieTitle.setText(movieList.get(position).getMovieTitle());
        holder.genre.setText(movieList.get(position).getGenre());
        holder.durationMinutes.setText(String.valueOf(movieList.get(position).getDurationMinutes()));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder{
        private TextView movieTitle;
        private TextView genre;
        private TextView durationMinutes;
        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.item_movie_title);
            genre = itemView.findViewById((R.id.item_genre));
            durationMinutes = itemView.findViewById((R.id.item_duration_minutes));

            itemView.setOnClickListener(view -> {
                Long movieId = movieList.get(getAdapterPosition()).getId();
                Intent intent = new Intent(itemView.getContext(), MovieDetailView.class);
                intent.putExtra("movieId", movieId);
                startActivity(itemView.getContext(), intent, null);
            });

        }
    }
}
