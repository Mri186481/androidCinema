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
import com.svalero.cinemav2.domain.Screening;
import com.svalero.cinemav2.view.ScreeningDetailView;

import java.util.List;

public class ScreeningAdapter extends RecyclerView.Adapter<ScreeningAdapter.ScreeningHolder> {
    private List<Screening> screeningList;

    public ScreeningAdapter(List<Screening> screeningList) {
        this.screeningList = screeningList;
    }

    @NonNull
    @Override
    public ScreeningHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.screeningsview_item, parent, false);
        return new ScreeningHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScreeningHolder holder, int position) {
        holder.screeningMovieTitle.setText(screeningList.get(position).getMovieTitle());
        holder.screeningTime.setText(screeningList.get(position).getScreeningTime());
        holder.screeningRoomName.setText(screeningList.get(position).getRoomName());

    }

    @Override
    public int getItemCount() {
        return screeningList.size();
    }

    public class ScreeningHolder extends RecyclerView.ViewHolder {
        private TextView screeningMovieTitle;
        private TextView screeningTime;
        private TextView screeningRoomName;

        public ScreeningHolder(@NonNull View itemView) {
            super(itemView);
            screeningMovieTitle = itemView.findViewById(R.id.item_screening_movie_title);
            screeningTime = itemView.findViewById((R.id.item_screening_time));
            screeningRoomName = itemView.findViewById((R.id.item_screening_room_name));

            itemView.setOnClickListener(view -> {
                Long screeningId = screeningList.get(getAdapterPosition()).getId();
                Intent intent = new Intent(itemView.getContext(), ScreeningDetailView.class);
                intent.putExtra("screeningId", screeningId);
                startActivity(itemView.getContext(), intent, null);

            });
        }
    }
}
