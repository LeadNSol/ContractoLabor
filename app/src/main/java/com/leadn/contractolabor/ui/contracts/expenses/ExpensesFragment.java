package com.leadn.contractolabor.ui.contracts.expenses;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.ui.contracts.expenses.adapter.ExpensesAdapter;
import com.leadn.contractolabor.ui.contracts.expenses.model.ExpenseResponse;
import com.leadn.contractolabor.ui.contracts.model.ContractResponse;
import com.leadn.contractolabor.utils.AppConstant;
import com.leadn.contractolabor.utils.InputValidator;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.UtilClass;
import com.leadn.contractolabor.utils.web_apis.ExpensesServices;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

    private ContractResponse.Contract mContract;

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
    private InputValidator mValidator;

    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        if (mActivity != null && mActivity.getSupportActionBar() != null)
            mActivity.getSupportActionBar().setTitle("Expenses");

        mValidator = new InputValidator(mActivity);
        txtDataNotFound = view.findViewById(R.id.txt_no_data_found);

        TextView txtContractName = view.findViewById(R.id.txt_contract_name);
        txtContractName.setText(mContract.getContractName());

        Button btnAddExpense = view.findViewById(R.id.btn_add_new_expenses);
        btnAddExpense.setOnClickListener(view -> {
            showAddExpensesDialog();
        });

        mExpensesServices = RetrofitHelper.getInstance().getExpensesClient();

        rvExpense = view.findViewById(R.id.rv_expenses);
        rvExpense.setLayoutManager(new LinearLayoutManager(mActivity));
        loadExpensesData();
    }

    private Dialog mExpensesDialog;
    private String paymentStatus;

    private void showAddExpensesDialog() {
        if (mExpensesDialog == null) {
            mExpensesDialog = new Dialog(mActivity);
            mExpensesDialog.setCancelable(false);
            mExpensesDialog.setContentView(R.layout.dialog_add_new_expenses);

            assert mExpensesDialog.getWindow() != null;
            mExpensesDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            EditText etName, etExpendedAmount;
            TextInputLayout inputLayoutName, inputLayoutAmount;
            TextView txtClose, txtChooseDate;
            Button btnAddExpense;
            MaterialSpinner spPaymentStatus;

            etName = mExpensesDialog.findViewById(R.id.et_name);
            etExpendedAmount = mExpensesDialog.findViewById(R.id.et_expended_amount);

            inputLayoutAmount = mExpensesDialog.findViewById(R.id.text_input_layout_amount);
            inputLayoutName = mExpensesDialog.findViewById(R.id.text_input_layout_name);

            spPaymentStatus = mExpensesDialog.findViewById(R.id.sp_payment_status);
            List<String> paymentTypeList = new ArrayList<>();
            paymentTypeList.add(AppConstant.PAYMENT_PAID);
            paymentTypeList.add(AppConstant.PAYMENT_PARTIAL);
            paymentTypeList.add(AppConstant.PAYMENT_PENDING);

            spPaymentStatus.setItems(paymentTypeList);

            spPaymentStatus.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
                paymentStatus = item;
            });
            txtChooseDate = mExpensesDialog.findViewById(R.id.txt_choose_date);
            txtChooseDate.setOnClickListener(view -> {
                UtilClass.initDatePicker(mActivity, txtChooseDate, "current");
            });

            txtClose = mExpensesDialog.findViewById(R.id.txt_close);
            txtClose.setOnClickListener(view -> {
                mExpensesDialog.dismiss();
                mExpensesDialog = null;
            });

            btnAddExpense = mExpensesDialog.findViewById(R.id.btn_add_expense);
            btnAddExpense.setOnClickListener(view -> {
                if (mValidator.isInputEditTextFilled(etName, inputLayoutName, "Expense name is required!")
                        && mValidator.isInputEditTextFilled(etExpendedAmount, inputLayoutAmount, "Amount is required!")) {
                    if (paymentStatus != null) {
                        if (!txtChooseDate.getText().toString().equalsIgnoreCase(mActivity.getString(R.string.choose_starting_date))) {
                            postExpense(etName.getText().toString(), etExpendedAmount.getText().toString(), paymentStatus, txtChooseDate.getText().toString());
                        } else {
                            txtChooseDate.setError("Date is required!");
                        }
                    } else {
                        spPaymentStatus.setError("Payment status is required!");
                    }
                }
            });

            mExpensesDialog.show();
        } else {
            mExpensesDialog.dismiss();
            mExpensesDialog = null;
        }
    }

    private void postExpense(String name, String amount, String paymentStatus, String date) {


        mExpensesServices.postExpenses(name, amount, date, paymentStatus, mContract.getSeqId(), mContract.getUserId())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        if (response.isSuccessful()) {
                            StatusResponse statusResponse = response.body();
                            if (statusResponse != null) {
                                if (statusResponse.getStatus()) {
                                    mExpensesDialog.dismiss();
                                    mExpensesDialog = null;
                                    Toast.makeText(mActivity, statusResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    loadExpensesData();
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void loadExpensesData() {
        mExpensesServices.getContractExpenses(mContract.getSeqId())
                .enqueue(new Callback<ExpenseResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<ExpenseResponse> call, @NotNull Response<ExpenseResponse> response) {
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