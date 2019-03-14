package team.project.tripmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.AuthResponse;
import team.project.tripmanager.model.ErrorResponse;

public class LoginFragment extends BaseFragment {

    AppCompatTextView forgetPassword;
    AppCompatEditText emailEdt, passwordEdt;
    AppCompatButton logInBtn, signUpBtn;
    TextInputLayout emailInput, passwordInput;
    private Logger logger = new Logger(getClass());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login,container,false);

        logInBtn = v.findViewById(R.id.loginBtn);
        signUpBtn = v.findViewById(R.id.signupBtn);
        forgetPassword = v.findViewById(R.id.forgetPwd);
        emailEdt = v.findViewById(R.id.email);
        passwordEdt= v.findViewById(R.id.password);
        emailInput = v.findViewById(R.id.emailInput);
        passwordInput = v.findViewById(R.id.passwordInput);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailEdt.getText().length() != 0 && passwordEdt.getText().length() != 0){
                    if(!Patterns.EMAIL_ADDRESS.matcher(emailEdt.getText()).matches()){
                        emailInput.setError("Invalid Email Address");
                        //Toast.makeText(getActivity(),"Invalid Email Address",Toast.LENGTH_SHORT).show();
                    } else{
                        //sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
                        checkLoginToServer(emailEdt.getText().toString(), passwordEdt.getText().toString());
                    }
                } else {
                    //Toast.makeText(getActivity(),"Enter All Fields",Toast.LENGTH_SHORT).show();
                    emailInput.setError("Field Required");
                    passwordInput.setError("Field Required");
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment signupFragment = new SignupFragment();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,signupFragment)
                        .addToBackStack("")
                        .commit();
            }
        });
        return v;
    }

    private void checkLoginToServer(String email, String password) {
        environment.getAPIService().login(email, password).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.body() != null) {
                    environment.getPrefs().storeAuthTokenResponse(response.body());
                    HomeFragment homeFragment = new HomeFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT).show();
                } else if(response.errorBody() != null){
                    try {
                        errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    logger.debug("response.body() is null");
                } else {
                    Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    logger.debug("respnse.errorBody() is null");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                logger.error(t);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
