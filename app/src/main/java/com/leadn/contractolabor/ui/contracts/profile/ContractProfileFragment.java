package com.leadn.contractolabor.ui.contracts.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.contracts.model.ContractResponse;
import com.leadn.contractolabor.ui.contracts.model.OwnerResponse;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.web_apis.ContractServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContractProfileFragment extends Fragment {

    public ContractProfileFragment() {
        // Required empty public constructor
    }

    private String mContractId;
    private View view;
    private AppCompatActivity mActivity;

    public ContractProfileFragment(String contractId) {
        this.mContractId = contractId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contract_profile, container, false);
        initViews();
        return view;
    }

    private ContractServices mContractServices;
    private TextView txtContractName, txtSqFeetPrice, txtContractStatus, txtAddress, txtStartingDate, txtEstimatedEndDate;
    private TextView txtOwnerName, txtOwnerPhone, txtOwnerAddress;
    private TextView txtTotalWorkers, txtTotalDays, txtEstimatedWage, txtTotalExpenses;

    private TextView txtWorkerNotFound, txtOwnerDataFound;

    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        assert mActivity != null;
        assert mActivity.getSupportActionBar() != null;
        mActivity.getSupportActionBar().hide();

        txtContractName = view.findViewById(R.id.txt_contract_name);
        txtSqFeetPrice = view.findViewById(R.id.txt_sq_feet);
        txtContractStatus = view.findViewById(R.id.txt_status);
        txtAddress = view.findViewById(R.id.txt_contract_address);
        txtStartingDate = view.findViewById(R.id.txt_starting_date);
        txtEstimatedEndDate = view.findViewById(R.id.txt_estimated_end_date);

        txtOwnerName = view.findViewById(R.id.txt_owner_name);
        txtOwnerPhone = view.findViewById(R.id.txt_owner_phone);
        txtOwnerAddress = view.findViewById(R.id.txt_owner_address);

        txtTotalWorkers = view.findViewById(R.id.txt_total_workers);
        txtTotalDays = view.findViewById(R.id.txt_working_days);
        txtEstimatedWage = view.findViewById(R.id.txt_total_wages);

        txtTotalExpenses = view.findViewById(R.id.txt_total_expenses);

        txtWorkerNotFound = view.findViewById(R.id.txt_no_work_data_found);
        txtOwnerDataFound = view.findViewById(R.id.txt_no_owner_data_found);

        mContractServices = RetrofitHelper.getInstance().getContractClient();
        loadProfileData();


    }

    private List<WorkerResponse.Worker> mWorkerList;
    private List<ContractResponse.Contract> mContractList;
    private List<OwnerResponse.ContractOwner> mOwnerList;

    private void loadProfileData() {
        mWorkerList = new ArrayList<>();
        mContractList = new ArrayList<>();
        mOwnerList = new ArrayList<>();
        mContractServices.getContractsById(mContractId)
                .enqueue(new Callback<ContractResponse>() {
                    @Override
                    public void onResponse(Call<ContractResponse> call, Response<ContractResponse> response) {
                        if (response.isSuccessful()) {
                            ContractResponse contractResponse = response.body();
                            if (contractResponse != null) {
                                mContractList = contractResponse.getContracts();
                                if (mContractList.size() > 0) {
                                    mWorkerList = mContractList.get(0).getWorkers();
                                    mOwnerList = mContractList.get(0).getOwner();

                                    showProfile();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ContractResponse> call, Throwable t) {

                    }
                });


    }

    private void showProfile() {
        if (mContractList.size() > 0) {
            ContractResponse.Contract contract = mContractList.get(0);
            txtContractName.setText(contract.getContractName());
            txtContractStatus.setText(contract.getStatus());

            txtAddress.setText(contract.getLocation());
            if (contract.getExpenses() != null)
                txtTotalExpenses.setText(contract.getExpenses());
            else
                txtTotalExpenses.setText("0");
            txtSqFeetPrice.setText(contract.getSqrFeetPrice().concat(" Rs/sqFeet"));
            txtStartingDate.setText(contract.getStartingDate());

            txtEstimatedEndDate.setText("not decide yet!");
        }

        if (mWorkerList.size() > 0) {

           /* txtTotalDays.setVisibility(View.VISIBLE);
            txtEstimatedWage.setVisibility(View.VISIBLE);
            txtTotalWorkers.setVisibility(View.VISIBLE);*/
            txtWorkerNotFound.setVisibility(View.GONE);

            txtTotalWorkers.setText(String.valueOf(mWorkerList.size()));
            int totalDays = 0;
            double totalWage = 0;
            for (WorkerResponse.Worker worker : mWorkerList) {
                totalDays += Integer.parseInt(worker.getDaysOfWork());
                totalWage += Double.parseDouble(worker.getDailyWage());
            }

            txtEstimatedWage.setText(String.valueOf(totalWage).concat(" Rs"));
            txtTotalDays.setText(String.valueOf(totalDays));
        } else {
            txtTotalDays.setVisibility(View.GONE);
            txtEstimatedWage.setVisibility(View.GONE);
            txtTotalWorkers.setVisibility(View.GONE);
            txtWorkerNotFound.setVisibility(View.VISIBLE);
        }

        if (mOwnerList.size() > 0) {
            txtOwnerDataFound.setVisibility(View.GONE);

            txtOwnerAddress.setText(mOwnerList.get(0).getAddress());
            txtOwnerPhone.setText(mOwnerList.get(0).getPhone());
            txtOwnerName.setText(mOwnerList.get(0).getName());

            txtOwnerPhone.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mOwnerList.get(0).getPhone(), null));
                mActivity.startActivity(intent);
            });

        } else {
            txtOwnerPhone.setVisibility(View.GONE);
            txtOwnerName.setVisibility(View.GONE);
            txtOwnerAddress.setVisibility(View.GONE);

            txtOwnerDataFound.setVisibility(View.VISIBLE);
        }


    }
}