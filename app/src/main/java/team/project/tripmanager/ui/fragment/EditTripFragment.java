package team.project.tripmanager.ui.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.ErrorResponse;
import team.project.tripmanager.model.Trip;
import team.project.tripmanager.utils.DateUtils;

public class EditTripFragment extends BaseDialogFragment {

    int FLAG = 0, UdtBtnFlag = 0;
    String startDateString, endDateString;
    AppCompatEditText tripNameEdt, startDateEdt, endDateEdt, selectPlaceEdt;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    AppCompatTextView updateBtn, cancelBtn;
    TextInputLayout tripNameInput, selectPlaceInput;
    Trip currentTrip;
    private Logger logger = new Logger(getClass());
    Boolean isDelete = false;

    public Boolean getDelete() {
        return isDelete;
    }

    public static EditTripFragment newInstance(){ return new EditTripFragment();}

    public void setCurrentTrip(Trip currentTrip) {
        this.currentTrip = currentTrip;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_trip, container, false);
        getDialog().setCanceledOnTouchOutside(false);
        tripNameEdt = v.findViewById(R.id.tripNameEdt);
        selectPlaceEdt = v.findViewById(R.id.selectPlaceEdt);
        startDateEdt = v.findViewById(R.id.startDateEdt);
        endDateEdt = v.findViewById(R.id.endDateEdt);
        updateBtn = v.findViewById(R.id.updateBtn);
        cancelBtn = v.findViewById(R.id.cancelBtn);

        tripNameInput = v.findViewById(R.id.tripNameInput);
        selectPlaceInput = v.findViewById(R.id.selectPlaceInput);
        myCalendar = Calendar.getInstance();

        tripNameEdt.setText(currentTrip.getTripName());
        selectPlaceEdt.setText(currentTrip.getPlaceName());

        try {
            startDateEdt.setText(DateUtils.getFormattedDate(currentTrip.getStartDate(),"dd MMM yy"));
            endDateEdt.setText(DateUtils.getFormattedDate(currentTrip.getEndDate(),"dd MMM yy"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        onDateSetListener = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        try {
            startDateString = DateUtils.getFormattedDate(currentTrip.getStartDate(),"dd MMM yy");
            endDateString = DateUtils.getFormattedDate(currentTrip.getEndDate(),"dd MMM yy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startDateEdt.setText(startDateString);
        endDateEdt.setText(endDateString);

        startDateEdt.setOnClickListener(v1 -> {
            showDate();
            FLAG = 1;
        });

        endDateEdt.setOnClickListener(v12 -> {
            showDate();
            FLAG = 2;
        });
        cancelBtn.setOnClickListener(v13 -> getDialog().dismiss());

        updateBtn.setOnClickListener(v14 -> {
            if(Objects.requireNonNull(tripNameEdt.getText()).length() !=0 && Objects.requireNonNull(selectPlaceEdt.getText()).length() !=0){
                if(UdtBtnFlag == 0){
                    updateTripRequestToServer(currentTrip.getId(),tripNameEdt.getText().toString(),
                            selectPlaceEdt.getText().toString(),
                            startDateString,
                            endDateString);
                    updateBtn.setTextColor(getResources().getColor(R.color.buttonDisableColor));
                    UdtBtnFlag = 1;
                }
            } else {
                tripNameInput.setError("Field Required");
                selectPlaceInput.setError("Field Required");
            }
        });

        return v;
    }

    private void updateTripRequestToServer(Integer tripId, String tripName, String placeName, String startDate, String endDate) {

        environment.getAPIService().editTrip(tripId, tripName, placeName, startDate, endDate).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                } else if (response.errorBody() != null) {
                    try {
                        errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.debug("Unsuccessful response");
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                updateBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                UdtBtnFlag = 0;
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                logger.error(t);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                updateBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                UdtBtnFlag = 0;
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

}
