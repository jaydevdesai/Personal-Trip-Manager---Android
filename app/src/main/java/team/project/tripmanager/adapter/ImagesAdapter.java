package team.project.tripmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import team.project.tripmanager.R;
import team.project.tripmanager.model.Image;
import team.project.tripmanager.viewholder.ImagesViewHolder;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesViewHolder> {
    private List<Image> imageList;

    public ImagesAdapter(List<Image> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.images_cards_list_row, viewGroup, false);
        return new ImagesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder imagesViewHolder, int i) {
        Image image = imageList.get(i);
        imagesViewHolder.setImage(image.getImageLink());
        imagesViewHolder.setImageText(image.getName());
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
