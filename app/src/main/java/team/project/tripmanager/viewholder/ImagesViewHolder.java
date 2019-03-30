package team.project.tripmanager.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import team.project.tripmanager.R;

public class ImagesViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView imageTextView;
    public ImagesViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.resvImage);
        imageTextView = itemView.findViewById(R.id.resvImageText);
    }

    public void setImageText(String text) {
        imageTextView.setText(text);
    }

    public void setImage(String imageLink) {
        Glide.with(imageView).load(imageLink).into(imageView);
    }
}
