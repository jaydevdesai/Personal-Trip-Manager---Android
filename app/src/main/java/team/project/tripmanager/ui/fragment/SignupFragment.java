package team.project.tripmanager.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.AuthResponse;


public class SignupFragment extends BaseFragment {

    AppCompatButton logInBtn, signUpBtn;
    AppCompatEditText emailEdt, passwordEdt, confirmPasswordEdt;
    Fragment loginFragment, homeFragment;
    TextInputLayout emailInput, passwordInput, confirmPasswordInput;
    SharedPreferences sharedPreferences;
    private Logger logger;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        loginFragment = new LoginFragment();
        homeFragment = new HomeFragment();
        logInBtn = v.findViewById(R.id.loginBtn);
        signUpBtn = v.findViewById(R.id.signupBtn);

        emailEdt = v.findViewById(R.id.email);
        passwordEdt = v.findViewById(R.id.password);
        confirmPasswordEdt = v.findViewById(R.id.confirmPassword);

        emailInput = v.findViewById(R.id.emailInput);
        passwordInput = v.findViewById(R.id.passwordInput);
        confirmPasswordInput = v.findViewById(R.id.confirmPasswordInput);


        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInput.setError(null);
                if(emailEdt.getText().length() != 0 && passwordEdt.getText().length() != 0 && confirmPasswordEdt.getText().length() != 0){
                    if(!Patterns.EMAIL_ADDRESS.matcher(emailEdt.getText()).matches()){
                        emailInput.setError("Invalid Email Address");
                        //Toast.makeText(getActivity(),"Invalid Email Address",Toast.LENGTH_SHORT).show();
                } else if(!passwordEdt.getText().toString().equals(confirmPasswordEdt.getText().toString())) {
                        confirmPasswordInput.setError("Password and Confirm password doesn't match");
                        //Toast.makeText(getActivity(),"Password and Confirm password doesn't match",Toast.LENGTH_SHORT).show();
                    } else{
                        signUpUserToServer(emailEdt.getText().toString(), passwordEdt.getText().toString());
                    }
                } else {
                    Toast.makeText(getActivity(),"Enter All Fields",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;

    }

    private void signUpUserToServer(String email, String password) {
        environment.getAPIService().signUp(email, password).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.body() != null) {
                    environment.getPrefs().storeAuthTokenResponse(response.body());
                    HomeFragment homeFragment = new HomeFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    Toast.makeText(getActivity(), "Sign up Successfull", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Unknown error occured", Toast.LENGTH_SHORT).show();
                    logger.debug("response.body() is null");
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
