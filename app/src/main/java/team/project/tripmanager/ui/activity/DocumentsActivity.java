package team.project.tripmanager.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.listener.OnImagePreparedListener;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.Document;
import team.project.tripmanager.ui.fragment.ImagesListFragment;
import team.project.tripmanager.ui.fragment.UploadDocumentFragment;
import team.project.tripmanager.utils.ImageUploadUtils;
import team.project.tripmanager.utils.ProgressRequestBody;

public class DocumentsActivity extends BaseActivity {

    Toolbar toolbar;
    String page;
    private Logger logger = new Logger(getClass());
    private ImageUploadUtils imageUploadUtils;
    UploadDocumentFragment uploadDocumentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Documents ");

        imageUploadUtils = new ImageUploadUtils(this);
        imageUploadUtils.setOnImagePreparedListener(new OnImagePreparedListener() {
            @Override
            public void onImagePrepared(ProgressRequestBody imageRequestBody) {
                UploadDocumentFragment uploadDocumentFragment = UploadDocumentFragment.newInstance();
                uploadDocumentFragment.setImageUploadUtils(imageUploadUtils);
                uploadDocumentFragment.setProgressRequestBody(imageRequestBody);
                uploadDocumentFragment.show(getSupportFragmentManager(),"progressBar");
                getSupportFragmentManager().executePendingTransactions();
                uploadDocumentFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getSupportFragmentManager().beginTransaction().remove(uploadDocumentFragment).commit();
                        fetchDocumentsFromServer();
                    }
                });

            }
        });
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fetchDocumentsFromServer();
    }

    private void fetchDocumentsFromServer() {
        environment.getAPIService().getDocuments().enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (!response.isSuccessful()) {
                    showSomethingWentWrong();
                    logger.debug("response not successfull");
                    return;
                }
                if (response.body() != null && response.body().getDocuments() != null) {
                    List<Document> documents = response.body().getDocuments();
                    ImagesListFragment imagesListFragment = new ImagesListFragment();
                    imagesListFragment.setImages(documents);
                    getSupportFragmentManager().beginTransaction().replace(R.id.reserveContainer, imagesListFragment).commitAllowingStateLoss();
                } else {
                    logger.debug("getDocuments null");
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
        getMenuInflater().inflate(R.menu.add_new, menu);
        return true;
    }

}
