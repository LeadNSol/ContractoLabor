package com.leadn.contractolabor.ui.contracts.expenses.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.contracts.expenses.model.ExpenseResponse;
import com.leadn.contractolabor.utils.AppConstant;

import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder> {

    private Context mContext;
    private List<ExpenseResponse.Expense> mExpenseList;


    public ExpensesAdapter(Context mContext, List<ExpenseResponse.Expense> mExpenseList) {
        this.mContext = mContext;
        this.mExpenseList = mExpenseList;
    }

    @NonNull
    @Override
    public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_workers_list_items, parent, false);
        return new ExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesViewHolder holder, int position) {
        ExpenseResponse.Expense expense = mExpenseList.get(position);
        holder.setData(expense);
    }

    @Override
    public int getItemCount() {
        return mExpenseList.size();
    }

    public class ExpensesViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtExpendedAmount, txtDate, txtPaymentStatus;

        public ExpensesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_expense_name);
            txtExpendedAmount = itemView.findViewById(R.id.txt_expended_amount);
            txtDate = itemView.findViewById(R.id.txt_expense_date);
            txtPaymentStatus = itemView.findViewById(R.id.txt_payment_status);

        }

        public void setData(ExpenseResponse.Expense expense) {

            txtName.setText(expense.getName());
            txtExpendedAmount.setText(expense.getExpendedAmount().concat(" Rs"));
            txtDate.setText(expense.getDate());

            if (expense.getPaymentStatus().equalsIgnoreCase(AppConstant.PAYMENT_PAID)) {
                txtPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
            } else if (expense.getPaymentStatus().equalsIgnoreCase(AppConstant.PAYMENT_PARTIAL)) {
                txtPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorLightBlue));
            } else {
                txtPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorRed));
            }
            txtPaymentStatus.setText(expense.getPaymentStatus());
        }
    }

}
