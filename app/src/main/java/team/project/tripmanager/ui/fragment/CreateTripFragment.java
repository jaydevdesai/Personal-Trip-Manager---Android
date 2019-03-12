package team.project.tripmanager.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.core.TMApplication;
import team.project.tripmanager.core.TMEnvironment;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;

import static android.app.Activity.RESULT_CANCELED;

public class CreateTripFragment extends DialogFragment {

    int AUTOCOMPLETE_REQUEST_CODE = 1, FLAG = 0, BtnFlag = 0;
    String startDateString, endDateString;
    AppCompatEditText tripNameEdt, startDateEdt, endDateEdt, selectPlaceEdt;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    AppCompatTextView createBtn, cancelBtn;
    TextInputLayout tripNameInput, selectPlaceInput;
    private Logger logger = new Logger(getClass());

    public static CreateTripFragment newInstance(){
        return new CreateTripFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_trip, container, false);

        tripNameEdt = v.findViewById(R.id.tripNameEdt);
        selectPlaceEdt = v.findViewById(R.id.selectPlaceEdt);
        startDateEdt = v.findViewById(R.id.startDateEdt);
        endDateEdt = v.findViewById(R.id.endDateEdt);
        createBtn = v.findViewById(R.id.createBtn);
        cancelBtn = v.findViewById(R.id.cancelBtn);
        tripNameInput = v.findViewById(R.id.tripNameInput);
        selectPlaceInput = v.findViewById(R.id.selectPlaceInput);
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
        startDateString = endDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(myCalendar.getTime());
        startDateEdt.setText(new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(myCalendar.getTime()));
        endDateEdt.setText(new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(myCalendar.getTime()));

        startDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
                FLAG = 1;
            }
        });

        endDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
                FLAG = 2;
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tripNameEdt.getText().length() !=0 && selectPlaceEdt.getText().length() !=0){
                    createTripRequestToServer(tripNameEdt.getText().toString(),
                            selectPlaceEdt.getText().toString(),
                            startDateString,
                            endDateString);
                } else {
                    tripNameInput.setError("Field Required");
                    selectPlaceInput.setError("Field Required");
                }
            }
        });
        return v;
    }

    private void createTripRequestToServer(String tripName, String placeName, String startDate, String endDate) {
        TMEnvironment environment = ((TMApplication) getActivity().getApplicationContext()).getEnvironment();
        environment.getAPIService().createTrip(tripName, placeName, startDate, endDate).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                } else {
                    logger.debug("Unsuccessful response");
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                logger.error(t);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDate() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), onDateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() -1000);
        datePickerDialog.show();

    }

    private void updateLabel(){
        switch (FLAG){
            case 1 : startDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(myCalendar.getTime());
                startDateEdt.setText(new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(myCalendar.getTime()));
                        break;
            case 2 : endDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(myCalendar.getTime());
                endDateEdt.setText(new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(myCalendar.getTime()));
                        break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AUTOCOMPLETE_REQUEST_CODE && data != null){
            try{
                Place place = Autocomplete.getPlaceFromIntent(data);
                Toast.makeText(getActivity(),place.getName(), Toast.LENGTH_LONG).show();
            } catch (IllegalArgumentException e){}
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

            Status status = Autocomplete.getStatusFromIntent(data);
        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }


}
