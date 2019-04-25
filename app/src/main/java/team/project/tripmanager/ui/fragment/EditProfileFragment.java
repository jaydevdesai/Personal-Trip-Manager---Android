package team.project.tripmanager.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.model.CommonResponse;

public class EditProfileFragment extends BaseDialogFragment{

    AppCompatEditText nameEdt, birthDateEdt;
    AppCompatTextView updateBtn, cancelBtn;
    String dateString;
    Calendar myCalendar;
    TextInputLayout nameInput, birthDateInput;
    DatePickerDialog.OnDateSetListener onDateSetListener;

    public static EditProfileFragment newInstance(){
        return new EditProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        getDialog().setCanceledOnTouchOutside(false);
        nameEdt = v.findViewById(R.id.nameEdt);
        birthDateEdt = v.findViewById(R.id.birthDateEdt);
        nameInput = v.findViewById(R.id.nameInput);
        birthDateInput = v.findViewById(R.id.birthDateInput);
        updateBtn = v.findViewById(R.id.updateBtn);
        cancelBtn = v.findViewById(R.id.cancelBtn);

        myCalendar = Calendar.getInstance();
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(myCalendar.getTime());
        birthDateEdt.setText(new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(myCalendar.getTime()));
        birthDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameEdt.getText().length() != 0){
                    updateProfileToServer();
                } else {
                    nameInput.setError("Required");
                }
            }
        });
        return v;
    }

    private void updateProfileToServer(){
        environment.getAPIService().updateProfile(nameEdt.getText().toString(), dateString).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.body() != null && response.isSuccessful()){
                    Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    showSomethingWentWrong();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    private void updateLabel() {
        dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(myCalendar.getTime());
        birthDateEdt.setText(new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(myCalendar.getTime()));
    }

    private void showDate() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), onDateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
