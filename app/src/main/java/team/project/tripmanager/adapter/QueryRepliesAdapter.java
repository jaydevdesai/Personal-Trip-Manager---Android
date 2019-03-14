package team.project.tripmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.List;

import team.project.tripmanager.R;
import team.project.tripmanager.model.QueryReply;
import team.project.tripmanager.module.prefs.MainPrefs;
import team.project.tripmanager.utils.DateUtils;
import team.project.tripmanager.viewholder.QueryRepliesViewHolder;

public class QueryRepliesAdapter extends RecyclerView.Adapter<QueryRepliesViewHolder> {

    private List<QueryReply> queryReplies;
    private MainPrefs mainPrefs;
    public QueryRepliesAdapter(List<QueryReply> queryReplies, MainPrefs mainPrefs) {
        this.queryReplies = queryReplies;
        this.mainPrefs = mainPrefs;
    }

    @NonNull
    @Override
    public QueryRepliesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.query_replies_cards_list_row, viewGroup, false);
        return new QueryRepliesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QueryRepliesViewHolder queryRepliesViewHolder, int i) {
        QueryReply queryReply = queryReplies.get(i);
        String name = queryReply.getName();
        if(queryReply.getEmail() != null && queryReply.getEmail().equals(mainPrefs.getEmail())) name = "You";
        queryRepliesViewHolder.setName(name);
        queryRepliesViewHolder.setReplyText(queryReply.getReply_text());
        try {
            queryRepliesViewHolder.setDate(DateUtils.getFormattedDate(queryReply.getCreation_time(),"dd MMM"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return queryReplies.size();
    }
}
