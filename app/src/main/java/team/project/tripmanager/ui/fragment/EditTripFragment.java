package team.project.tripmanager.ui.fragment;

import android.app.DatePickerDialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import team.project.tripmanager.R;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.Trip;
import team.project.tripmanager.utils.DateUtils;

public class EditTripFragment extends DialogFragment {

    int FLAG = 0, BtnFlag = 0;
    String startDateString, endDateString;
    AppCompatEditText tripNameEdt, startDateEdt, endDateEdt, selectPlaceEdt;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    AppCompatTextView updateBtn, cancelBtn, deleteBtn;
    TextInputLayout tripNameInput, selectPlaceInput;
    Trip currentTrip;
    private Logger logger = new Logger(getClass());

    public static EditTripFragment newInstance(){ return new EditTripFragment();}

    public void setCurrentTrip(Trip currentTrip) {
        this.currentTrip = currentTrip;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_trip, container, false);

        tripNameEdt = v.findViewById(R.id.tripNameEdt);
        selectPlaceEdt = v.findViewById(R.id.selectPlaceEdt);
        startDateEdt = v.findViewById(R.id.startDateEdt);
        endDateEdt = v.findViewById(R.id.endDateEdt);
        updateBtn = v.findViewById(R.id.updateBtn);
        cancelBtn = v.findViewById(R.id.cancelBtn);
        deleteBtn = v.findViewById(R.id.deleteBtn);
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
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.requireNonNull(tripNameEdt.getText()).length() !=0 && Objects.requireNonNull(selectPlaceEdt.getText()).length() !=0){
                    /*updateTripRequestToServer(tripNameEdt.getText().toString(),
                            selectPlaceEdt.getText().toString(),
                            startDateString,
                            endDateString);*/
                } else {
                    tripNameInput.setError("Field Required");
                    selectPlaceInput.setError("Field Required");
                }
            }
        });
        return v;
    }

    private void updateTripRequestToServer(String tripName, String placeName, String startDate, String endDate){}

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
