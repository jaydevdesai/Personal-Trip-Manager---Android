package team.project.tripmanager.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import team.project.tripmanager.core.TMApplication;
import team.project.tripmanager.core.TMEnvironment;

public class BaseActivity extends AppCompatActivity {
    protected TMEnvironment environment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        environment = ((TMApplication)getApplicationContext()).getEnvironment();
    }
}
