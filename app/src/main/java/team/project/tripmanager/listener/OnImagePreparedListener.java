package team.project.tripmanager.listener;

import okhttp3.RequestBody;
import team.project.tripmanager.utils.ProgressRequestBody;

public interface OnImagePreparedListener {
    void onImagePrepared(ProgressRequestBody imageRequestBody);
}
