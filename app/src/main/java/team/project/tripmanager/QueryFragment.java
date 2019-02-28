package team.project.tripmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class QueryFragment extends Fragment {

    RecyclerView queryListView;
    QueryListHolder queryListHolder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_query, container,false);

        queryListView = v.findViewById(R.id.queryList);
        queryListHolder = new QueryListHolder();
        RecyclerView.LayoutManager layoutManager =  new GridLayoutManager(getActivity(),1);

        queryListView.setLayoutManager(layoutManager);
        queryListView.setItemAnimator(new DefaultItemAnimator());
        queryListView.setAdapter(queryListHolder);

        return v;
    }

    public class QueryListHolder extends RecyclerView.Adapter<QueryListHolder.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder {
            MyViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

        @NonNull
        @Override
        public QueryListHolder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.query_cards_list_row, viewGroup, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull QueryListHolder.MyViewHolder myViewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}
