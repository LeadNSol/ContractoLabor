package com.leadn.contractolabor.ui.contracts;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.contracts.adapter.AttendanceAdapter;
import com.leadn.contractolabor.ui.contracts.adapter.ContractAdapter;
import com.leadn.contractolabor.ui.contracts.adapter.ContractWorkersAdapter;
import com.leadn.contractolabor.ui.contracts.expenses.ExpensesFragment;
import com.leadn.contractolabor.ui.contracts.model.ContractResponse;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.UtilClass;
import com.leadn.contractolabor.utils.web_apis.ContractServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContractFragment extends Fragment implements ContractAdapter.OnContractClickListener {


    public ContractFragment() {
        // Required empty public constructor
    }

    private View view;
    private AppCompatActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contract, container, false);
        initViews();
        return view;
    }

    private RecyclerView mRVContract;

    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        if (mActivity != null && mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().setTitle("Contract");
        }
        mRVContract = view.findViewById(R.id.rv_contract);
        mRVContract.setLayoutManager(new LinearLayoutManager(mActivity));

        contractServices = RetrofitHelper.getInstance().getContractClient();

        Button btnAddNewContract = view.findViewById(R.id.btn_add_new_project);
        btnAddNewContract.setOnClickListener(view -> {
            UtilClass.pushFragment(new NewContractFragment(), mActivity, R.id.main_frame_layout, true);
        });
        getContracts();

    }


    private ContractServices contractServices;
    private ContractAdapter mContractAdapter;
    private ContractAdapter.OnContractClickListener mOnContractClickListener;

    private void getContracts() {
        mOnContractClickListener = this;

        contractServices.getContracts(UtilClass.getCurrentUserId()).enqueue(new Callback<ContractResponse>() {
            @Override
            public void onResponse(Call<ContractResponse> call, Response<ContractResponse> response) {
                if (response.isSuccessful()) {
                    ContractResponse contractResponse = response.body();
                    if (contractResponse != null) {
                        List<ContractResponse.Contract> contractList = contractResponse.getContracts();
                        if (contractList.size() > 0) {
                            mContractAdapter = new ContractAdapter(mActivity, contractList, mOnContractClickListener);
                            mRVContract.setAdapter(mContractAdapter);
                            mContractAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ContractResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onWorkerClick(ContractResponse.Contract contract) {
        showContractWorkers(contract);
    }

    private Dialog mAssignedWorkerDialog;

    private void showContractWorkers(ContractResponse.Contract contract) {
        if (mAssignedWorkerDialog == null) {
            mAssignedWorkerDialog = new Dialog(mActivity);
            mAssignedWorkerDialog.setCanceledOnTouchOutside(false);
            mAssignedWorkerDialog.setCancelable(false);
            mAssignedWorkerDialog.setContentView(R.layout.dialog_contract_workers);

            int width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.8);
            if (mAssignedWorkerDialog.getWindow() != null)
                mAssignedWorkerDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            RecyclerView recyclerView = mAssignedWorkerDialog.findViewById(R.id.rv_contract_workers);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

            ContractWorkersAdapter workersAdapter = new ContractWorkersAdapter(mActivity, contract.getWorkers());
            recyclerView.setAdapter(workersAdapter);
            workersAdapter.notifyDataSetChanged();



            Button btnAssignWorkers = mAssignedWorkerDialog.findViewById(R.id.btn_assign_workers);
            btnAssignWorkers.setOnClickListener(view -> {
                //assignWorkersToContract(contract.getSeqId());
                UtilClass.pushFragment(new AssignWorkersToContractFragment(contract.getSeqId()), mActivity, R.id.main_frame_layout, true);
                mAssignedWorkerDialog.dismiss();
                mAssignedWorkerDialog = null;
            });

            TextView txtClose = mAssignedWorkerDialog.findViewById(R.id.txt_close);
            txtClose.setOnClickListener(view -> {
                mAssignedWorkerDialog.dismiss();
                mAssignedWorkerDialog = null;
            });


            mAssignedWorkerDialog.show();
        } else {
            mAssignedWorkerDialog.dismiss();
            mAssignedWorkerDialog = null;
        }

    }

    @Override
    public void onExpensesClick(ContractResponse.Contract contract) {
        UtilClass.pushFragment(new ExpensesFragment(contract), mActivity, R.id.main_frame_layout, true);
    }

    @Override
    public void onAttendanceClick(ContractResponse.Contract contract) {
        showAttendanceDialog(contract);
    }

    private Dialog mAttendanceDialog;

    private void showAttendanceDialog(ContractResponse.Contract contract) {
        if (mAttendanceDialog == null) {
            mAttendanceDialog = new Dialog(mActivity);
            mAttendanceDialog.setCancelable(false);
            mAttendanceDialog.setCanceledOnTouchOutside(false);
            mAttendanceDialog.setContentView(R.layout.dialog_worker_attendance);

            if (mAttendanceDialog.getWindow() != null)
                mAttendanceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            RecyclerView recyclerView = mAttendanceDialog.findViewById(R.id.rv_workers_attendance);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

            AttendanceAdapter attendanceAdapter = new AttendanceAdapter(mActivity, contract.getWorkers());
            recyclerView.setAdapter(attendanceAdapter);
            attendanceAdapter.notifyDataSetChanged();

            TextView txtClose = mAttendanceDialog.findViewById(R.id.txt_close);
            txtClose.setOnClickListener(view -> {
                mAttendanceDialog.dismiss();
                mAttendanceDialog = null;
            });

            mAttendanceDialog.show();
        } else {
            mAttendanceDialog.dismiss();
            mAttendanceDialog = null;
        }
    }


    @Override
    public void onMoreClick(ContractResponse.Contract contract) {

    }
}