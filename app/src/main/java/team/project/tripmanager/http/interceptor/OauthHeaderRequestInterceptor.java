package team.project.tripmanager.http.interceptor;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import team.project.tripmanager.core.TMApplication;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.module.prefs.MainPrefs;

/**
 * Injects OAuth token - if present - into Authorization header
 **/
public final class OauthHeaderRequestInterceptor implements Interceptor {
    protected final Logger logger = new Logger(getClass().getName());

    @NonNull
    private final MainPrefs mainPrefs;

    public OauthHeaderRequestInterceptor(@NonNull Context context) {
        mainPrefs = ((TMApplication)context.getApplicationContext()).getEnvironment().getPrefs();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
        final String token = mainPrefs.getAuthorizationHeader();
        if (token != null) {
            builder.addHeader("Authorization", token);
        }
        return chain.proceed(builder.build());
    }

}
