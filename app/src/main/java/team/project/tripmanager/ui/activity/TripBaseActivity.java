package team.project.tripmanager.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class TripBaseActivity extends BaseActivity {
    protected Integer tripId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripId = getIntent().getIntExtra("tripId", 0);
    }
}
