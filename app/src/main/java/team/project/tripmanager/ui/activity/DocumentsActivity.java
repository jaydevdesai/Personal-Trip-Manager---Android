package team.project.tripmanager.ui.activity;

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
import team.project.tripmanager.model.Document;
import team.project.tripmanager.ui.fragment.ImagesListFragment;
import team.project.tripmanager.utils.ImageUploadUtils;

public class DocumentsActivity extends BaseActivity {

    Toolbar toolbar;
    String page;
    private Logger logger = new Logger(getClass());
    private ImageUploadUtils imageUploadUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        page = getIntent().getStringExtra("page");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your " + page);
        imageUploadUtils = new ImageUploadUtils(this);
        imageUploadUtils.setOnImagePreparedListener(new OnImagePreparedListener() {
            @Override
            public void onImagePrepared(RequestBody imageRequetBody) {
                uploadDocumentToServer(imageRequetBody);
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.reserveContainer, new ImagesListFragment(documents)).commitAllowingStateLoss();
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


    private void uploadDocumentToServer(RequestBody imageRequetBody) {
        MultipartBody.Part documentImageMutlipartBody = MultipartBody.Part.createFormData("document_image", "upload" + new Random().nextInt() + ".jpg", imageRequetBody);
        RequestBody documentNameRequestBody = RequestBody.create(MediaType.parse("text/plain"), "Test Document");

        environment.getAPIService().uploadDocument(documentNameRequestBody, documentImageMutlipartBody).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (!response.isSuccessful()) {
                    showSomethingWentWrong();
                    logger.debug("unsuccessfull response");
                    return;
                } else {
                    Toast.makeText(DocumentsActivity.this, "Document uploaded", Toast.LENGTH_SHORT).show();
                    fetchDocumentsFromServer();
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
