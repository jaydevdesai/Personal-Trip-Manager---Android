package team.project.tripmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;


public class SignupFragment extends Fragment {

    AppCompatButton logInBtn, signUpBtn;
    AppCompatEditText emailEdt, passwordEdt, confirmPasswordEdt;
    Fragment loginFragment, homeFragment;
    TextInputLayout emailInput, passwordInput, confirmPasswordInput;
    SharedPreferences sharedPreferences;

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
                        new SignUp().execute("");
                    }
                } else {
                    Toast.makeText(getActivity(),"Enter All Fields",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;

    }

    private class SignUp extends AsyncTask<String, Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String aUrl = Objects.requireNonNull(getActivity()).getString(R.string.WS_Url) +"UserSignUp.php";
            String result = null;
            try {
                WebService webService = new WebService(aUrl,createJson());
                result = webService.PostJSon();
                JSONObject jsonObject = new JSONObject(result);
                result = jsonObject.getString("result");

            } catch (JSONException | IOException e) {
                result = "conn";
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            switch (result) {
                case "true":
                    sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putBoolean("loggedIn",true);
                    edit.putString("email", Objects.requireNonNull(emailEdt.getText()).toString());
                    edit.apply();
                    Fragment homeFragment = new HomeFragment();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, homeFragment).commit();
                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    Toast.makeText(getActivity(), "Signed Up", Toast.LENGTH_SHORT).show();
                    break;
                case "already":
                    Toast.makeText(getActivity(), "Email Address already registered", Toast.LENGTH_SHORT).show();
                    break;
                case "conn":
                    Toast.makeText(getActivity(), "Check Internet Connection.", Toast.LENGTH_SHORT).show();
                default:
                    Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private JSONObject createJson() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("email", Objects.requireNonNull(emailEdt.getText()).toString());
        jsonObject.accumulate("password", Objects.requireNonNull(passwordEdt.getText()).toString());
        return jsonObject;
    }



}
