package team.project.tripmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import team.project.tripmanager.R;
import team.project.tripmanager.model.Query;
import team.project.tripmanager.viewholder.QueryViewHolder;

public class QueryAdapter extends RecyclerView.Adapter<QueryViewHolder> {
    private List<Query> queryList;

    public QueryAdapter(List<Query> queryList) {
        this.queryList = queryList;
    }

    @NonNull
    @Override
    public QueryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.query_cards_list_row, viewGroup, false);
        return new QueryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QueryViewHolder queryViewHolder, int i) {
        Query query = queryList.get(i);
        queryViewHolder.setName(query.getName());
        queryViewHolder.setQueryText(query.getQueryText());
        queryViewHolder.setTime(query.getCreationTime());
    }

    @Override
    public int getItemCount() {
        return queryList.size();
    }
}
