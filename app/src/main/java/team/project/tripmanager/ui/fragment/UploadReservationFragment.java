package team.project.tripmanager.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.ui.activity.ReservationActivity;
import team.project.tripmanager.utils.ImageUploadUtils;
import team.project.tripmanager.utils.ProgressRequestBody;

public class UploadReservationFragment extends BaseDialogFragment implements ProgressRequestBody.UploadCallbacks{

    ProgressBar progressBar;
    AppCompatTextView uploadTV;
    AppCompatImageView imageView;
    ImageUploadUtils imageUploadUtils;
    ProgressRequestBody progressRequestBody;
    AppCompatTextView uploadBtn, cancelBtn;
    AppCompatEditText nameEdt;
    private Integer tripId;

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public void setProgressRequestBody(ProgressRequestBody progressRequestBody) {
        this.progressRequestBody = progressRequestBody;
    }

    public void setImageUploadUtils(ImageUploadUtils imageUploadUtils) {
        this.imageUploadUtils = imageUploadUtils;
    }

    public static UploadReservationFragment newInstance(){
        return new UploadReservationFragment();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload_image, container, false);
        progressRequestBody.setmListener(this);
        getDialog().setCanceledOnTouchOutside(false);
        imageView = v.findViewById(R.id.imageView);
        uploadBtn = v.findViewById(R.id.uploadBtn);
        cancelBtn = v.findViewById(R.id.cancelBtn);
        nameEdt = v.findViewById(R.id.nameEdt);

        Bitmap btmp = imageUploadUtils.getBitmap();
        imageView.setImageBitmap(btmp);
        ConstraintSet constraintSet = new ConstraintSet();
        ConstraintLayout constraintLayout = v.findViewById(R.id.constraintLayout);
        String ratio = String.format("%d:%d", btmp.getWidth(),btmp.getHeight());
        constraintSet.clone(constraintLayout);
        constraintSet.setDimensionRatio(imageView.getId(),ratio);
        constraintSet.applyTo(constraintLayout);

        progressBar = v.findViewById(R.id.progressBar);
        uploadTV = v.findViewById(R.id.uploadTV);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadReservationToServer(progressRequestBody);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressBar.setProgress(percentage);
        String up = "Uploading "+String.valueOf(percentage)+"%";
        uploadTV.setText(up);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadStart() {
        progressBar.setProgress(0);
    }

    private void uploadReservationToServer(RequestBody imageRequetBody) {
        MultipartBody.Part reservationImageMutlipartBody = MultipartBody.Part.createFormData("reservation_image", "upload" + new Random().nextInt() + ".jpg", imageRequetBody);
        RequestBody reservationNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), nameEdt.getText().toString());
        RequestBody tripIdRequestBody = RequestBody.create(MediaType.parse("text/plain"), tripId.toString());

        environment.getAPIService().uploadReservation(tripIdRequestBody, reservationNameRequestBody, reservationImageMutlipartBody).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (!response.isSuccessful()) {
                    showSomethingWentWrong();
                    logger.debug("unsuccessful response");
                    return;
                } else {
                    Toast.makeText(getActivity(), "Reservation uploaded", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                showSomethingWentWrong();
                logger.error(t);
            }
        });
    }
}
