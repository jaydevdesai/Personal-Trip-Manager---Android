package team.project.tripmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team.project.tripmanager.R;
import team.project.tripmanager.viewholder.ExploreViewHolder;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreViewHolder> {

    @NonNull
    @Override
    public ExploreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.explore_cards_list_row, viewGroup, false);
        return new ExploreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreViewHolder exploreViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
