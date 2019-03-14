package team.project.tripmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.List;

import team.project.tripmanager.R;
import team.project.tripmanager.model.Query;
import team.project.tripmanager.module.prefs.MainPrefs;
import team.project.tripmanager.utils.DateUtils;
import team.project.tripmanager.viewholder.QueryViewHolder;

public class QueryAdapter extends RecyclerView.Adapter<QueryViewHolder> {
    private List<Query> queryList;
    private MainPrefs mainPrefs;
    public QueryAdapter(List<Query> queryList, MainPrefs mainPrefs) {
        this.queryList = queryList;
        this.mainPrefs = mainPrefs;
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

        String name = query.getName();
        if(query.getEmail().equals(mainPrefs.getEmail())) name = "You";
        queryViewHolder.setName(name);

        queryViewHolder.setQueryText(query.getQueryText());
        queryViewHolder.initializeClickListener(query, mainPrefs);

        try {
            String formattedDate = null;
            formattedDate = DateUtils.getFormattedDate(query.getCreationTime(), "dd MMM");
            queryViewHolder.setTime(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return queryList.size();
    }
}
