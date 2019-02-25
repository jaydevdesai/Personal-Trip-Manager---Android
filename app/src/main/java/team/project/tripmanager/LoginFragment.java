package team.project.tripmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class LoginFragment extends Fragment {

    AppCompatTextView forgetPassword;
    AppCompatEditText emailEdt, passwordEdt;
    AppCompatButton logInBtn, signUpBtn;
    SharedPreferences sharedPreferences;
    TextInputLayout emailInput, passwordInput;

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
                        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
                        new CheckLogin().execute("");
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

    private class CheckLogin extends AsyncTask<String, Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String aUrl = Objects.requireNonNull(getActivity()).getString(R.string.WS_Url) +"UserLogIn.php";
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
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putBoolean("loggedIn",true);
                    edit.putString("email", Objects.requireNonNull(emailEdt.getText()).toString());
                    edit.apply();
                    Fragment homeFragment = new HomeFragment();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, homeFragment).commit();
                    Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT).show();
                    break;
                case "false":
                    Toast.makeText(getActivity(), "Incorrect Email or Password.", Toast.LENGTH_SHORT).show();
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
