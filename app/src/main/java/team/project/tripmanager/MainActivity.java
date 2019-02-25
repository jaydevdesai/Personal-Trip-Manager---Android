package team.project.tripmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    SharedPreferences sharedPreferences;
    Fragment nextFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("loginDetails", MODE_PRIVATE);
        Places.initialize(this,getResources().getString(R.string.Place_API));
        setContentView(R.layout.activity_main);
        /*GoogleApiClient googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();*/



        if(sharedPreferences.contains("loggedIn") && sharedPreferences.getBoolean("loggedIn",false)){
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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
