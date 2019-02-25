package team.project.tripmanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;

public class CreateTripFragment extends DialogFragment {

    int AUTOCOMPLETE_REQUEST_CODE = 1, FLAG = 0, BtnFlag = 0;
    String startDateString, endDateString;
    AppCompatEditText tripNameEdt, startDateEdt, endDateEdt, selectPlaceEdt;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    AppCompatTextView createBtn, cancelBtn;
    TextInputLayout tripNameInput, selectPlaceInput;

    public interface DialogDismiss{

        public void onDialogDismiss();
    }


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
                    new CreateTrip().execute();
                } else {
                    tripNameInput.setError("Field Required");
                    selectPlaceInput.setError("Field Required");
                }
            }
        });

        /*selectPlaceEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(getActivity());
                startActivityForResult(intent,AUTOCOMPLETE_REQUEST_CODE);
            }
        });*/


        return v;
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

    private class CreateTrip extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            String aUrl = Objects.requireNonNull(getActivity()).getString(R.string.WS_Url) +"CreateTrip.php";
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
                    Toast.makeText(getActivity(), "Trip Created Succesfully.", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                    break;
                case "null":
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    break;
                case "conn":
                    Toast.makeText(getActivity(), "Check Internet Connection.", Toast.LENGTH_SHORT).show();
                default:
                    Toast.makeText(getActivity(), "Error! Try Again.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private JSONObject createJson() throws JSONException{
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("email", sharedPreferences.getString("email",""));
        jsonObject.accumulate("tripName", Objects.requireNonNull(tripNameEdt.getText()).toString());
        jsonObject.accumulate("placeName", Objects.requireNonNull(selectPlaceEdt.getText()).toString());
        jsonObject.accumulate("startDate", startDateString);
        jsonObject.accumulate("endDate", endDateString);
        return jsonObject;
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
