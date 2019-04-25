package team.project.tripmanager.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import team.project.tripmanager.R;
import team.project.tripmanager.module.prefs.MainPrefs;
import team.project.tripmanager.ui.activity.DocumentsActivity;
import team.project.tripmanager.ui.activity.MainActivity;
import team.project.tripmanager.ui.activity.UserQueryActivity;
import team.project.tripmanager.ui.fragment.ChangePasswordFragment;
import team.project.tripmanager.ui.fragment.EditProfileFragment;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private AppCompatTextView nameTV;
    private ConstraintLayout constraintLayout;
    private MainPrefs mainPrefs;
    private Context context;

    public UserViewHolder(@NonNull View itemView, MainPrefs mainPrefs, Context context) {
        super(itemView);
        this.mainPrefs = mainPrefs;
        this.context = context;
        nameTV = itemView.findViewById(R.id.listText);
        constraintLayout = itemView.findViewById(R.id.conView);
    }

    public void setName(String name){
        nameTV.setText(name);
    }

    public void initializeClickListener(String choice) {
        constraintLayout.setOnClickListener(view -> {
            Intent intent;
            switch(choice){
                case "My Documents":
                    intent = new Intent(constraintLayout.getContext(), DocumentsActivity.class);
                    constraintLayout.getContext().startActivity(intent);
                    break;
                case "Log Out":
                    mainPrefs.deletePref();
                    intent = new Intent(constraintLayout.getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    constraintLayout.getContext().startActivity(intent);
                    break;
                case "My Queries":
                    intent = new Intent(constraintLayout.getContext(), UserQueryActivity.class);
                    constraintLayout.getContext().startActivity(intent);
                    break;
                case "Edit Profile":
                    EditProfileFragment editProfileFragment = EditProfileFragment.newInstance();
                    editProfileFragment.show(((FragmentActivity) context).getSupportFragmentManager(),"editProfile");
                    ((FragmentActivity) context).getSupportFragmentManager().executePendingTransactions();
                    editProfileFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().remove(editProfileFragment).commit();

                        }
                    });
                    break;
                case "Change Password":
                    ChangePasswordFragment changePasswordFragment = ChangePasswordFragment.newInstance();
                    changePasswordFragment.show(((FragmentActivity) context).getSupportFragmentManager(),"changePassword");
                    ((FragmentActivity) context).getSupportFragmentManager().executePendingTransactions();
                    changePasswordFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().remove(changePasswordFragment).commit();

                        }
                    });
                    break;

            }
        });
    }
}
