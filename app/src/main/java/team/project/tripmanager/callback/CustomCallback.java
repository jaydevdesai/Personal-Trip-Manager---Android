package team.project.tripmanager.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.logger.Logger;

public class CustomCallback<T> implements Callback<T> {
    private Logger logger = new Logger(getClass());
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!response.isSuccessful()) {
            logger.debug("Unsuccessful response");
            logger.warn("URL: " + call.request().url().toString());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        logger.error(t);
    }
}
