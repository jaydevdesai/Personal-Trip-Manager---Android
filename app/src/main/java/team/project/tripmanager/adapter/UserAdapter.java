package team.project.tripmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team.project.tripmanager.R;
import team.project.tripmanager.module.prefs.MainPrefs;
import team.project.tripmanager.viewholder.UserViewHolder;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private List<String> userList = new ArrayList<String>(Arrays.asList("Edit Profile","My Documents", "My Queries", "Change Password", "Log Out"));
    private MainPrefs mainPrefs;
    private Context context;

    public UserAdapter(MainPrefs mainPrefs, Context context){
        this.mainPrefs = mainPrefs;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_profile_list_row, viewGroup, false);
        return new UserViewHolder(itemView,mainPrefs, context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        userViewHolder.setName(userList.get(i));
        userViewHolder.initializeClickListener(userList.get(i));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
