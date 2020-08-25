package com.leadn.contractolabor.ui.contracts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leadn.contractolabor.R;
import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.ui.contracts.adapter.AssignWorkerToContractAdapter;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.web_apis.ContractServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AssignWorkersToContractFragment extends Fragment {

    public AssignWorkersToContractFragment() {

    }

    private View view;
    private AppCompatActivity mActivity;
    private String contractSeqId;

    public AssignWorkersToContractFragment(String contractSeqId) {
        this.contractSeqId = contractSeqId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_assigns_workers_to_contract, container, false);
        intiViews();
        return view;
    }

    private RecyclerView mRecyclerView;
    private ContractServices mContractServices;

    private void intiViews() {
        setHasOptionsMenu(true);
        mActivity = (AppCompatActivity) getActivity();

        if (mActivity != null && mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().setTitle("Workers Assignment");
        }

        mContractServices = RetrofitHelper.getInstance().getContractClient();
        mRecyclerView = view.findViewById(R.id.rv_assign_workers_to_contract);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        loadWorkerForAssignment();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.assign_worker_contract_menu, menu);
        menu.findItem(R.id.nav_assing_worker_to_contract).setVisible(true);
        menu.findItem(R.id.nav_item_count);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_assing_worker_to_contract) {
            // showContractsDialog();
            assignToContract();
        }

        return super.onOptionsItemSelected(item);

    }

    private void assignToContract() {
        JSONObject jsonObject = new JSONObject();
        if (AssignWorkerToContractAdapter.mSelectedWorkerList.size() > 0) {

            try {
                jsonObject.put("id", contractSeqId);
                jsonObject.put("workers", AssignWorkerToContractAdapter.mSelectedWorkerList);

                mContractServices.assignWorkers(jsonObject)
                        .enqueue(new Callback<StatusResponse>() {
                            @Override
                            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                if (response.isSuccessful()) {
                                    StatusResponse statusResponse = response.body();
                                    if (statusResponse != null) {
                                        Toast.makeText(mActivity, statusResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<StatusResponse> call, Throwable t) {

                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mActivity, "Plz select any worker first!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadWorkerForAssignment() {
        mContractServices.getFreeWorkers().enqueue(new Callback<WorkerResponse>() {
            @Override
            public void onResponse(Call<WorkerResponse> call, Response<WorkerResponse> response) {
                if (response.isSuccessful()) {
                    WorkerResponse workerResponse = response.body();
                    if (workerResponse != null) {
                        List<WorkerResponse.Worker> workersList = workerResponse.getWorkersList();
                        if (workersList.size() > 0) {
                            AssignWorkerToContractAdapter assignWorkerToContractAdapter
                                    = new AssignWorkerToContractAdapter(mActivity, workersList);
                            mRecyclerView.setAdapter(assignWorkerToContractAdapter);
                            assignWorkerToContractAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<WorkerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}