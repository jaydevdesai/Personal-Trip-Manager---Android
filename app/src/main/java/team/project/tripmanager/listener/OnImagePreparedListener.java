package team.project.tripmanager.listener;

import okhttp3.RequestBody;

public interface OnImagePreparedListener {
    void onImagePrepared(RequestBody imageRequetBody);
}
