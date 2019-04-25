package team.project.tripmanager.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.listener.OnImagePreparedListener;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.Reservation;
import team.project.tripmanager.ui.fragment.ImagesListFragment;
import team.project.tripmanager.ui.fragment.UploadDocumentFragment;
import team.project.tripmanager.ui.fragment.UploadReservationFragment;
import team.project.tripmanager.utils.ImageUploadUtils;
import team.project.tripmanager.utils.ProgressRequestBody;

public class ReservationActivity extends BaseActivity {

    Toolbar toolbar;
    String page;
    private Logger logger = new Logger(getClass());
    private ImageUploadUtils imageUploadUtils;
    private Integer tripId;
    private boolean fromExplore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        page = getIntent().getStringExtra("page");
        tripId = getIntent().getIntExtra("tripId", 0);
        fromExplore = getIntent().getBooleanExtra("fromExplore", false);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your " + page);
        imageUploadUtils = new ImageUploadUtils(this);
        imageUploadUtils.setOnImagePreparedListener(new OnImagePreparedListener() {
            @Override
            public void onImagePrepared(ProgressRequestBody imageRequestBody) {
                UploadReservationFragment uploadReservationFragment = UploadReservationFragment.newInstance();
                uploadReservationFragment.setImageUploadUtils(imageUploadUtils);
                uploadReservationFragment.setProgressRequestBody(imageRequestBody);
                uploadReservationFragment.setTripId(tripId);
                uploadReservationFragment.show(getSupportFragmentManager(),"uploadReservation");
                getSupportFragmentManager().executePendingTransactions();
                uploadReservationFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getSupportFragmentManager().beginTransaction().remove(uploadReservationFragment).commit();
                        fetchReservationsFromServer();
                    }
                });
            }
        });
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fetchReservationsFromServer();
    }

    private void fetchReservationsFromServer() {
        environment.getAPIService().getReservations(tripId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (!response.isSuccessful()) {
                    showSomethingWentWrong();
                    logger.debug("response not successful");
                    return;
                }
                if (response.body() != null && response.body().getReservations() != null) {
                    List<Reservation> reservations = response.body().getReservations();
                    ImagesListFragment imagesListFragment = new ImagesListFragment();
                    imagesListFragment.setImages(reservations);
                    getSupportFragmentManager().beginTransaction().replace(R.id.reserveContainer, imagesListFragment).commitAllowingStateLoss();
                } else {
                    logger.debug("getReservations null");
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                showSomethingWentWrong();
                logger.error(t);
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.addBtn:
                imageUploadUtils.chooseImage();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUploadUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!fromExplore) {
            getMenuInflater().inflate(R.menu.add_new, menu);
        }
        return true;
    }
}
