package team.project.tripmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.ErrorResponse;

public class ChangePasswordFragment extends BaseDialogFragment{

    AppCompatEditText oldPasswordEdt, newPasswordEdt, confirmPasswordEdt;
    AppCompatTextView changeBtn, cancelBtn;
    TextInputLayout oldPasswordInput, newPasswordInput, confirmPasswordInput;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        getDialog().setCanceledOnTouchOutside(false);
        oldPasswordEdt = v.findViewById(R.id.oldPasswordEdt);
        newPasswordEdt = v.findViewById(R.id.newPasswordEdt);
        confirmPasswordEdt = v.findViewById(R.id.confirmPasswordEdt);
        oldPasswordInput = v.findViewById(R.id.oldPasswordInput);
        newPasswordInput = v.findViewById(R.id.newPasswordInput);
        confirmPasswordInput = v.findViewById(R.id.confirmPasswordInput);
        cancelBtn = v.findViewById(R.id.cancelBtn);
        changeBtn = v.findViewById(R.id.changeBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPasswordEdt.getText().length() !=0 && newPasswordEdt.getText().length() != 0 && confirmPasswordEdt.getText().length() != 0){
                    if(!newPasswordEdt.getText().toString().equals(confirmPasswordEdt.getText().toString())){
                        confirmPasswordInput.setError("Confirm New Password doesn't match New Password.");
                    } else{
                        updatePasswordToServer(oldPasswordEdt.getText().toString(), newPasswordEdt.getText().toString());
                    }
                } else {
                    oldPasswordInput.setError("Required");
                    newPasswordInput.setError("Required");
                    confirmPasswordInput.setError("Required");
                }
            }
        });
        return  v;
    }

    private void updatePasswordToServer(String oldP, String newP){
        environment.getAPIService().changePassword(oldP, newP).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.body() != null && response.isSuccessful()){
                    Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dismiss();
                } else if(response.errorBody() != null) {
                    try {
                        errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(getActivity(),errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    showSomethingWentWrong();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }
}
