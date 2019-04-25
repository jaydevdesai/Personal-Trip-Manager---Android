package team.project.tripmanager.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;
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

public class AddExpenseFragment extends BaseDialogFragment {

    AppCompatEditText nameEdt, dateEdt, amountEdt;
    AppCompatTextView cancelBtn, addBtn;
    TextInputLayout nameInput, amountInput, dateInput;
    RadioGroup radioGroup;
    AppCompatRadioButton cashRd, cardRd;
    String dateString;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    Integer tripId, cash=1;

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public static AddExpenseFragment newInstance() {
        return new AddExpenseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_expense, container, false);

        nameEdt = v.findViewById(R.id.nameEdt);
        amountEdt = v.findViewById(R.id.amountEdt);
        dateEdt = v.findViewById(R.id.dateEdt);
        nameInput = v.findViewById(R.id.nameInput);
        amountInput = v.findViewById(R.id.amountInput);
        dateInput = v.findViewById(R.id.dateInput);
        cancelBtn = v.findViewById(R.id.cancelBtn);
        addBtn = v.findViewById(R.id.addBtn);
        radioGroup = v.findViewById(R.id.cashRadio);
        cashRd = v.findViewById(R.id.cashRd);
        cardRd = v.findViewById(R.id.cardRd);

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
        dateEdt.setText(new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(myCalendar.getTime()));
        dateEdt.setOnClickListener(new View.OnClickListener() {
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
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameEdt.getText().length() != 0 && amountEdt.getText().length() != 0){
                    if(radioGroup.getCheckedRadioButtonId() == R.id.cardRd){
                        cash = 0;
                    } else {
                        cash = 1;
                    }
                    addExpenseToServer();
                } else {
                    nameInput.setError("Required");
                    amountInput.setError("Required");
                }
            }
        });

        return v;
    }

    private void updateLabel() {
        dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(myCalendar.getTime());
        dateEdt.setText(new SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(myCalendar.getTime()));
    }

    private void showDate() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), onDateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void addExpenseToServer(){
        environment.getAPIService().addExpense(nameEdt.getText().toString(), tripId, amountEdt.getText().toString(), cash, dateString).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.body() != null && response.isSuccessful()){
                    Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
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

}
