package team.project.tripmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.List;

import team.project.tripmanager.R;
import team.project.tripmanager.model.Expense;
import team.project.tripmanager.viewholder.ExpenseViewHolder;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {

    private List<Expense> expenses;

    public ExpenseAdapter(List<Expense> expenses) {
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.expense_cards_list_row, viewGroup, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder expenseViewHolder, int i) {
        Expense expense = expenses.get(i);
        expenseViewHolder.setCashCard(expense.getCash());
        try {
            expenseViewHolder.setItemDate(expense.getPurchase_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expenseViewHolder.setItemName(expense.getName());
        expenseViewHolder.setItemPrice(expense.getPrice());
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}
