package team.project.tripmanager.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import team.project.tripmanager.ui.fragment.HomeFragment;
import team.project.tripmanager.ui.fragment.LoginFragment;
import team.project.tripmanager.R;
import team.project.tripmanager.model.AuthResponse;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AuthResponse currentAuth = environment.getPrefs().getCurrentAuth();
        Fragment nextFragment;
        if (currentAuth != null && currentAuth.isSuccess()) {
            nextFragment = new HomeFragment();
        } else {
            nextFragment = new LoginFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container,nextFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
