package team.project.tripmanager.ui.activity;

import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.adapter.ExpenseAdapter;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.Expense;
import team.project.tripmanager.ui.fragment.AddExpenseFragment;

public class ExpenseManagerActivity extends BaseActivity {

    AppCompatTextView totalMoneySpent, spentCashMoney, spentCardMoney, leftCashMoney;
    ProgressBar leftCashBar;
    RecyclerView recyclerView;
    Integer tripId;
    ConstraintLayout loadLayout;
    ProgressBar progressBar;
    private boolean fromExplore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_manager);
        fromExplore = getIntent().getBooleanExtra("fromExplore", false);
        tripId = getIntent().getIntExtra("tripId",0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Expenses");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        totalMoneySpent = findViewById(R.id.totalMoneySpent);
        spentCardMoney = findViewById(R.id.spentCardMoney);
        spentCashMoney = findViewById(R.id.spentCashMoney);
        leftCashMoney = findViewById(R.id.leftMoney);
        leftCashBar = findViewById(R.id.leftBar);
        loadLayout = findViewById(R.id.loadLayout);
        progressBar = findViewById(R.id.contentLoad);
        recyclerView = findViewById(R.id.expensesListView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchCashBalanceFromServer();
    }

    void getCashBalanceAlert() {
        LayoutInflater li = LayoutInflater.from(this);
        View view = li.inflate(R.layout.dialog_get_cash, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);

        final AppCompatEditText cashAmountEdt =  view.findViewById(R.id.cashAmountEdt);
        final TextInputLayout cashAmountInput =  view.findViewById(R.id.cashAmountInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(cashAmountEdt.length() == 0){
                            cashAmountInput.setError("Required");
                        } else {
                            Double cash = Double.valueOf(cashAmountEdt.getText().toString());
                            addCashToServer(cash);
                            dialog.cancel();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!fromExplore) {
            getMenuInflater().inflate(R.menu.add_new, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.addBtn:
                AddExpenseFragment addExpenseFragment = AddExpenseFragment.newInstance();
                addExpenseFragment.setTripId(tripId);
                addExpenseFragment.show(getSupportFragmentManager(),"addExpense");
                getSupportFragmentManager().executePendingTransactions();
                addExpenseFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadLayout.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().remove(addExpenseFragment).commit();
                        fetchCashBalanceFromServer();
                    }
                });

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addCashToServer(Double cash){
        environment.getAPIService().addCash(cash, tripId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.body() != null && response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    fetchCashBalanceFromServer();
                } else {
                    showSomethingWentWrong();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

    private void fetchCashBalanceFromServer(){
        environment.getAPIService().getExpense(tripId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.body() != null && response.isSuccessful()){
                    loadLayout.setVisibility(View.GONE);
                    if(response.body().getCash_balance() == -1){
                        if(!fromExplore){
                            getCashBalanceAlert();
                        }
                    } else {
                        List<Expense> expenses = response.body().getExpenses();
                        countCash(expenses, response.body().getCash_balance());
                        recyclerView.setAdapter(new ExpenseAdapter(expenses));
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                showSomethingWentWrong();
                logger.error(t);
            }
        });
    }

    private void countCash(List<Expense> expenses, Double cashBalance){
        Double cashSpent = 0.0, cardSpent = 0.0;
        for(int i =0; i<expenses.size();i++){
            if(expenses.get(i).getCash() == 1){
                cashSpent += expenses.get(i).getPrice();
            } else {
                cardSpent += expenses.get(i).getPrice();
            }
        }
        String tms = "Total Money Spent : "+ String.valueOf(cardSpent+cashSpent);
        String scm = "Cash Spent: "+ String.valueOf(cashSpent);
        String scdm = "Card Spent: "+ String.valueOf(cardSpent);
        String lcm = "Cash Left: "+ String.valueOf(cashBalance-cashSpent);

        totalMoneySpent.setText(tms);
        spentCashMoney.setText(scm);
        spentCardMoney.setText(scdm);
        leftCashMoney.setText(lcm);
        leftCashBar.setMax(100);
        double lcb = cashSpent/(cashBalance)*100;
        leftCashBar.setProgress((int) lcb);
    }


}
