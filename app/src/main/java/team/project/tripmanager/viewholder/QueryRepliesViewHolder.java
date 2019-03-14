package team.project.tripmanager.viewholder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import team.project.tripmanager.R;

public class QueryRepliesViewHolder extends RecyclerView.ViewHolder {

    private TextView userNameTv, replyTextTV, dateTv;
    private ConstraintLayout queryReplyLayout;

    public QueryRepliesViewHolder(@NonNull View itemView) {
        super(itemView);
        queryReplyLayout = itemView.findViewById(R.id.queryReplyLayout);
        userNameTv = itemView.findViewById(R.id.userName);
        replyTextTV = itemView.findViewById(R.id.replyText);
        dateTv = itemView.findViewById(R.id.replyDate);
    }

    public void setName(String name) {
        userNameTv.setText(name);
    }

    public void setReplyText(String replyText) {
        replyTextTV.setText(replyText);
    }

    public  void setDate(String date){
        dateTv.setText(date);
    }


}
