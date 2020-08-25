package com.leadn.contractolabor.ui.contracts.expenses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.contracts.expenses.adapter.ExpensesAdapter;
import com.leadn.contractolabor.ui.contracts.expenses.model.ExpenseResponse;
import com.leadn.contractolabor.ui.contracts.model.ContractResponse;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.web_apis.ExpensesServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExpensesFragment extends Fragment {


    private View view;
    private AppCompatActivity mActivity;

    public ExpensesFragment() {
        // Required empty public constructor
    }

    ContractResponse.Contract mContract;

    public ExpensesFragment(ContractResponse.Contract contract) {
        this.mContract = contract;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_expenses, container, false);
        initViews();
        return view;
    }

    private TextView txtDataNotFound;
    private RecyclerView rvExpense;
    private ExpensesServices mExpensesServices;

    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        if (mActivity != null && mActivity.getSupportActionBar() != null)
            mActivity.getSupportActionBar().setTitle("Expenses");

        txtDataNotFound = view.findViewById(R.id.txt_no_data_found);

        TextView txtContractName = view.findViewById(R.id.txt_contract_name);
        txtContractName.setText(mContract.getContractName());

        Button btnAddExpense = view.findViewById(R.id.btn_add_new_expenses);
        btnAddExpense.setOnClickListener(view -> {

        });

        mExpensesServices = RetrofitHelper.getInstance().getExpensesClient();

        rvExpense = view.findViewById(R.id.rv_expenses);
        rvExpense.setLayoutManager(new LinearLayoutManager(mActivity));
        loadExpensesData();
    }

    private void loadExpensesData() {
        mExpensesServices.getContractExpenses(mContract.getSeqId())
                .enqueue(new Callback<ExpenseResponse>() {
                    @Override
                    public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                        if (response.isSuccessful()) {
                            ExpenseResponse expenseResponse = response.body();
                            if (expenseResponse != null) {
                                List<ExpenseResponse.Expense> expenseList = expenseResponse.getExpenses();
                                if (expenseList.size() > 0) {
                                    txtDataNotFound.setVisibility(View.GONE);
                                    rvExpense.setVisibility(View.VISIBLE);

                                    ExpensesAdapter expensesAdapter = new ExpensesAdapter(mActivity, expenseList);
                                    rvExpense.setAdapter(expensesAdapter);
                                    expensesAdapter.notifyDataSetChanged();

                                } else {
                                    txtDataNotFound.setVisibility(View.VISIBLE);
                                    rvExpense.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ExpenseResponse> call, Throwable t) {

                    }
                });

    }
}