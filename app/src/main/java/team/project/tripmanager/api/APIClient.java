package team.project.tripmanager.api;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import team.project.tripmanager.Constants;
import team.project.tripmanager.http.interceptor.OauthHeaderRequestInterceptor;

public class APIClient {
    private static APIClient instance;

    private Retrofit retrofit;

    private APIClient(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new OauthHeaderRequestInterceptor(context))
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }

    public static APIClient getInstance(Context context) {
        if (instance == null) {
            instance = new APIClient(context);
        }
        return instance;
    }

    public Retrofit getClient() {
        return retrofit;
    }
}
