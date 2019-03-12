package team.project.tripmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import team.project.tripmanager.core.TMApplication;
import team.project.tripmanager.core.TMEnvironment;
import team.project.tripmanager.logger.Logger;

public class BaseFragment extends Fragment {
    protected TMEnvironment environment;
    private Logger logger = new Logger(getClass());
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            environment = ((TMApplication)getActivity().getApplicationContext()).getEnvironment();
        }
    }

    protected void showSomethingWentWrong() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
        } else {
            logger.warn("getActivity() is null");
        }
    }
}
