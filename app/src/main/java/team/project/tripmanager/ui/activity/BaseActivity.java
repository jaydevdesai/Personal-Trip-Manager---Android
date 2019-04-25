package team.project.tripmanager.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import team.project.tripmanager.core.TMApplication;
import team.project.tripmanager.core.TMEnvironment;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.ErrorResponse;

public class BaseActivity extends AppCompatActivity {
    protected TMEnvironment environment;
    protected ErrorResponse errorResponse;
    protected Gson gson = new GsonBuilder().setLenient().create();
    protected Logger logger = new Logger(getClass());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        environment = ((TMApplication) getApplicationContext()).getEnvironment();
    }

    protected void showSomethingWentWrong() {
        showToast("Something went wrong!");
    }

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
