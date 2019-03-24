package team.project.tripmanager.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import team.project.tripmanager.R;
import team.project.tripmanager.core.TMApplication;
import team.project.tripmanager.core.TMEnvironment;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.ErrorResponse;

public class BaseDialogFragment extends DialogFragment {

    protected TMEnvironment environment;
    private Logger logger = new Logger(getClass());
    protected ErrorResponse errorResponse;
    protected Gson gson = new GsonBuilder().setLenient().create();


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        if (getActivity() != null) {
            environment = ((TMApplication)getActivity().getApplicationContext()).getEnvironment();
        }
    }
}
