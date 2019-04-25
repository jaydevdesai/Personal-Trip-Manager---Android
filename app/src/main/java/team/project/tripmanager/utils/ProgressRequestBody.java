package team.project.tripmanager.utils;

import android.os.Handler;
import android.os.Looper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {

    private byte[] bytes;
    private String content_type;
    private UploadCallbacks mListener;

    public ProgressRequestBody(byte[] bytes, String content_type) {
        this.bytes = bytes;
        this.content_type = content_type;
    }

    public void setmListener(UploadCallbacks mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse(content_type+"/*");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = bytes.length;
        byte[] buffer = new byte[1024];

        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
            long uploaded = 0;
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            mListener.uploadStart();
            while ((read = in.read(buffer)) != -1) {

                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded, fileLength));

                uploaded += read;
                sink.write(buffer, 0, read);
            }
            mListener.onFinish();
        }
    }

    public interface UploadCallbacks {
        void onProgressUpdate(int percentage);

        void onError();

        void onFinish();

        void uploadStart();
    }

    class ProgressUpdater implements Runnable {

        private long mUploaded;
        private long mTotal;

        ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            mListener.onProgressUpdate((int)(100 * mUploaded / mTotal));
        }
    }
}
