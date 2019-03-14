package team.project.tripmanager.viewholder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import team.project.tripmanager.R;
import team.project.tripmanager.model.Query;
import team.project.tripmanager.module.prefs.MainPrefs;
import team.project.tripmanager.ui.activity.QueryDetailsActivity;

public class QueryViewHolder extends RecyclerView.ViewHolder {

    private TextView userNameTv, queryTextTv, dateTv;
    private CardView queryCard;
    public QueryViewHolder(@NonNull View itemView) {
        super(itemView);
        queryCard = itemView.findViewById(R.id.queryCard);
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

    public void initializeClickListener(Query query, MainPrefs mainPrefs){
        queryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryDetailsActivity.setMainPrefs(mainPrefs);
                Intent intent = new Intent(queryCard.getContext(), QueryDetailsActivity.class);
                intent.putExtra("QueryDetails", query);
                queryCard.getContext().startActivity(intent);
            }
        });
    }
}
