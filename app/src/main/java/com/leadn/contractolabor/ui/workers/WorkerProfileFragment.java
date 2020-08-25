package com.leadn.contractolabor.ui.workers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.workers.adapters.AttendanceHistoryAdapter;
import com.leadn.contractolabor.ui.workers.model.AttendanceResponse;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.web_apis.WorkerServices;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WorkerProfileFragment extends Fragment {


    public WorkerProfileFragment() {
        // Required empty public constructor
    }

    private WorkerResponse.Worker worker;

    public WorkerProfileFragment(WorkerResponse.Worker worker) {
        this.worker = worker;
    }

    private View view;
    private AppCompatActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_worker_profile, container, false);
        initViews();
        return view;
    }

    private TextView txtName, txtPhone, txtAddress, txtType, txtTotalPresent, txtTotalAbsent;
    private RecyclerView rvAttendance, rvHistory;
    private CircleImageView imgWorker;
    private WorkerServices mWorkerServices;
    private TextView txtNoAttendanceIsMark, txtNoHistoryIsFound;

    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        if (mActivity != null && mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().setTitle("Worker Profile");
        }

        txtName = view.findViewById(R.id.txt_worker_name);
        txtPhone = view.findViewById(R.id.txt_worker_phone);
        txtAddress = view.findViewById(R.id.txt_worker_address);
        txtType = view.findViewById(R.id.txt_worker_type);
        txtTotalPresent = view.findViewById(R.id.txt_total_present);
        txtTotalAbsent = view.findViewById(R.id.txt_total_absent);

        imgWorker = view.findViewById(R.id.profile_image);

        txtNoAttendanceIsMark = view.findViewById(R.id.txt_no_attendance_found);
        // txtNoHistoryIsFound = view.findViewById(R.id.txt_no_history_found);

        rvAttendance = view.findViewById(R.id.rv_attendance);
        rvAttendance.setLayoutManager(new LinearLayoutManager(mActivity));

        //rvHistory = view.findViewById(R.id.rv_history);
//        rvHistory.setLayoutManager(new LinearLayoutManager(mActivity));

        mWorkerServices = RetrofitHelper.getInstance().getWorkersClient();

        setBasicInfo();
        loadAttendance();
    }

    private void loadAttendance() {
        mWorkerServices.getWorkerAttendance(worker.getSeqId())
                .enqueue(new Callback<AttendanceResponse>() {
                    @Override
                    public void onResponse(Call<AttendanceResponse> call, Response<AttendanceResponse> response) {
                        if (response.isSuccessful()) {
                            AttendanceResponse attendanceResponse = response.body();
                            if (attendanceResponse != null) {
                                List<AttendanceResponse.Attendance> attendanceList = attendanceResponse.getWorkerAttendance();
                                if (attendanceList.size() > 0) {
                                    rvAttendance.setVisibility(View.VISIBLE);
                                    txtNoAttendanceIsMark.setVisibility(View.GONE);
                                    AttendanceHistoryAdapter attendanceHistoryAdapter = new AttendanceHistoryAdapter(mActivity, attendanceList,txtTotalPresent,txtTotalAbsent);
                                    rvAttendance.setAdapter(attendanceHistoryAdapter);
                                    attendanceHistoryAdapter.notifyDataSetChanged();

                                } else {
                                    rvAttendance.setVisibility(View.GONE);
                                    txtNoAttendanceIsMark.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AttendanceResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void setBasicInfo() {
        txtName.setText(worker.getName());
        txtPhone.setText(worker.getPhone());
        txtAddress.setText(worker.getAddress());
        txtType.setText(worker.getType());

        Glide.with(mActivity).load(worker.getImageUrl())
                .dontAnimate()
                .error(R.drawable.profile)
                .into(imgWorker);
    }
}