package team.project.tripmanager.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import team.project.tripmanager.R;
import team.project.tripmanager.listener.OnImagePreparedListener;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.utils.ImageUploadUtils;
import team.project.tripmanager.utils.ProgressRequestBody;

public class SetProfileFragment extends BaseFragment {

    AppCompatEditText nameEdt, birthDateEdt;
    AppCompatImageView profilePic;
    AppCompatImageButton changeProfileBtn;
    AppCompatButton completeBtn;
    String dateString;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    private static final int GALLERY = 1, CAMERA = 2;
    private Logger logger = new Logger(getClass());
    RequestBody progressRequestBody;
    int FLAG = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_set_profile,container,false);
        nameEdt = v.findViewById(R.id.nameEdt);
        birthDateEdt = v.findViewById(R.id.birthDateEdt);
        profilePic = v.findViewById(R.id.profilePic);
        changeProfileBtn = v.findViewById(R.id.changeProfile);

        changeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        completeBtn = v.findViewById(R.id.completeBtn);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FLAG == 0){
                    uploadImageToServer(getBitmapFromVectorDrawable(getActivity(),R.drawable.ic_profile));
                }
                setProfileToServer();
            }
        });

        myCalendar = Calendar.getInstance();

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(myCalendar.getTime());
        birthDateEdt.setText(new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(myCalendar.getTime()));
        birthDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });
        return  v;
    }

    private void setProfileToServer(){
        MultipartBody.Part multiPart = MultipartBody.Part.createFormData("profile_image", "upload" + new Random().nextInt() + ".jpg", progressRequestBody);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), nameEdt.getText().toString());
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("text/plain"), dateString);
        environment.getAPIService().setProfile(requestBody,requestBody2,multiPart).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (!response.isSuccessful()) {
                    showSomethingWentWrong();
                    logger.debug("Unsuccessful response");
                } else {
                    environment.getPrefs().setProfile(true);
                    HomeFragment homeFragment = new HomeFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    Toast.makeText(getActivity(), "Profile Completed.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });

    }

    private void updateLabel() {
        dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(myCalendar.getTime());
        birthDateEdt.setText(new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(myCalendar.getTime()));
    }

    private void showDate() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), onDateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void chooseImage() {
        String[] items = {"Select photo from gallery", "Capture photo from camera"};
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Select Action")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                })
                .create();
        alertDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode,resultCode,data);
        try {
            switch (requestCode) {
                case CAMERA:
                    Bitmap thumbnail = (Bitmap) (data != null ? data.getExtras().get("data") : null);
                    uploadImageToServer(thumbnail);
                    break;
                case GALLERY:
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                        uploadImageToServer(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e ){
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (NullPointerException e){
            Toast.makeText(getActivity(),"Uploading failed.",Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToServer(Bitmap bitmap) {
       profilePic.setImageBitmap(bitmap);
       FLAG =1;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
        byte[] imageData = outputStream.toByteArray();
        //RequestBody = new ProgressRequestBody(imageData,"image");
        progressRequestBody = RequestBody.create(MediaType.parse("image/*"), imageData);

    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
