package team.project.tripmanager.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import team.project.tripmanager.R;

public class QueryViewHolder extends RecyclerView.ViewHolder {
    private TextView userNameTv, queryTextTv, dateTv;
    public QueryViewHolder(@NonNull View itemView) {
        super(itemView);
        userNameTv = itemView.findViewById(R.id.userName);
        queryTextTv = itemView.findViewById(R.id.queryText);
        dateTv = itemView.findViewById(R.id.queryDate);
    }

    public void setName(String name) {
        userNameTv.setText(name);
    }

    public void setQueryText(String queryText) {
        queryTextTv.setText(queryText);
    }

    public void setTime(String creationTime) {
        dateTv.setText(creationTime);
    }
}
