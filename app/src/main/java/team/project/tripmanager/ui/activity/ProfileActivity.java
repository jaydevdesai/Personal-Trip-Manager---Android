package team.project.tripmanager.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.adapter.UserAdapter;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.Note;
import team.project.tripmanager.model.Profile;
import team.project.tripmanager.utils.DateUtils;

public class ProfileActivity extends BaseActivity {

    AppCompatImageView profilePic;
    AppCompatImageButton changeProfile;
    AppCompatTextView userName, birthDate;
    RecyclerView recyclerView;
    Profile profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profilePic = findViewById(R.id.profilePic);
        changeProfile = findViewById(R.id.changeProfile);
        userName = findViewById(R.id.userName);
        birthDate = findViewById(R.id.birthDate);
        recyclerView = findViewById(R.id.userListView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        UserAdapter userAdapter = new UserAdapter(environment.getPrefs(), this);
        fetchProfileFromServer();
        recyclerView.setAdapter(userAdapter);

    }

    private void fetchProfileFromServer(){
        environment.getAPIService().getProfile().enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Profile profile = response.body().getProfile();
                    userName.setText(profile.getName());
                    try {
                        String date = DateUtils.getFormattedDate(profile.getDob(),"dd MMM yy");
                        birthDate.setText(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String link = profile.getProfile_image();
                    Log.d("link",link);
                    Glide.with(profilePic).load(link).into(profilePic);

                } else {
                    logger.warn("response.body() is null");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }


}
