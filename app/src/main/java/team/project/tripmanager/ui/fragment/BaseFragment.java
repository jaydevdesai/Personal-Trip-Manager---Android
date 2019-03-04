package team.project.tripmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import team.project.tripmanager.core.TMApplication;
import team.project.tripmanager.core.TMEnvironment;

public class BaseFragment extends Fragment {
    protected TMEnvironment environment;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            environment = ((TMApplication)getActivity().getApplicationContext()).getEnvironment();
        }
    }
}
