package team.project.tripmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team.project.tripmanager.R;
import team.project.tripmanager.viewholder.ImagesViewHolder;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesViewHolder> {
    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.images_cards_list_row, viewGroup, false);
        return new ImagesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder imagesViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
