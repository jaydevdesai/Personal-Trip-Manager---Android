package team.project.tripmanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import team.project.tripmanager.listener.OnImagePreparedListener;
import team.project.tripmanager.logger.Logger;

public class ImageUploadUtils {
    private static final int GALLERY = 1, CAMERA = 2;
    private Context context;
    private OnImagePreparedListener onImagePreparedListener;
    private Logger logger = new Logger(getClass());
    private Bitmap bitmap;

    public ImageUploadUtils(Context context) {
        this.context = context;
    }

    public void chooseImage() {
        String[] items = {"Select photo from gallery", "Capture photo from camera"};
        AlertDialog alertDialog = new AlertDialog.Builder(context)
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
        ((Activity) context).startActivityForResult(intent, CAMERA);
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        ((Activity) context).startActivityForResult(galleryIntent, GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            switch (requestCode) {
                case CAMERA:
                    Bitmap thumbnail = (Bitmap) (data != null ? data.getExtras().get("data") : null);
                    uploadImageToServer(thumbnail);
                    break;
                case GALLERY:
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                        uploadImageToServer(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e ){
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (NullPointerException e){
            Toast.makeText(context,"Uploading failed.",Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
    private void uploadImageToServer(Bitmap bitmap) {
        this.bitmap = bitmap;
        Toast.makeText(context, "Prepared.", Toast.LENGTH_SHORT).show();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
        byte[] imageData = outputStream.toByteArray();
        ProgressRequestBody documentImageRequestBody = new ProgressRequestBody(imageData,"image");
        //RequestBody documentImageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageData);
        if (onImagePreparedListener != null) {
            onImagePreparedListener.onImagePrepared(documentImageRequestBody);
        } else {
            logger.warn("onImagePreparedListener not initialized");
        }
    }

    public void setOnImagePreparedListener(OnImagePreparedListener onImagePreparedListener) {
        this.onImagePreparedListener = onImagePreparedListener;
    }
}
