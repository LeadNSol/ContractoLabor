package com.leadn.contractolabor.ui.contracts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.UtilClass;
import com.leadn.contractolabor.utils.web_apis.ContractServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {
    private Context mContext;
    private List<WorkerResponse.Worker> mWorkersList;

    public AttendanceAdapter(Context mContext, List<WorkerResponse.Worker> mWorkersList) {
        this.mContext = mContext;
        this.mWorkersList = mWorkersList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.single_attendance_dialog_list_items, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        WorkerResponse.Worker worker = mWorkersList.get(position);
        holder.setData(worker);
    }

    @Override
    public int getItemCount() {
        return mWorkersList.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        private TextView txtWorkerName, txtDailyWage, txtDayOfWork, txtWorkAt, txtWorkerType;
        private ImageView imgWorker;
        private TextView btnPresent, btnAbsent;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWorkerName = itemView.findViewById(R.id.txt_worker_name);
            txtDailyWage = itemView.findViewById(R.id.txt_worker_daily_wage);
            txtDayOfWork = itemView.findViewById(R.id.txt_worker_days_of_work);
            txtWorkAt = itemView.findViewById(R.id.txt_worker_at);
            txtWorkerType = itemView.findViewById(R.id.txt_worker_type);

            imgWorker = itemView.findViewById(R.id.img_worker);
            btnAbsent = itemView.findViewById(R.id.txt_absent);
            btnPresent = itemView.findViewById(R.id.txt_present);
        }

        public void setData(WorkerResponse.Worker worker) {

            txtWorkerType.setText(worker.getType());
            txtWorkerName.setText(worker.getName());
            txtWorkAt.setText(worker.getContractId());
            txtDailyWage.setText(worker.getDailyWage());
            txtDayOfWork.setText(worker.getDaysOfWork());

            Glide.with(mContext)
                    .load(worker.getImageUrl())
                    .error(R.drawable.ic_baseline_supervisor_account)
                    .into(imgWorker);

            btnPresent.setOnClickListener(view -> {
                markAttendance("Present", worker);
            });
            btnAbsent.setOnClickListener(view -> {
                markAttendance("Absent", worker);
            });
        }


        int daysOfAbsent = 0, daysOfWork = 0;
        private void markAttendance(String type, WorkerResponse.Worker worker) {
            daysOfWork = Integer.parseInt(worker.getDaysOfWork());
            daysOfAbsent = Integer.parseInt(worker.getDaysOfAbsence());
            int present = 0, absent = 0;
            if (type.equalsIgnoreCase("Present")) {
                present = 1;
                daysOfWork += 1;
            } else {
                absent = 1;
                daysOfAbsent += 1;
            }


            ContractServices contractServices = RetrofitHelper.getInstance().getContractClient();
            contractServices
                    .postAttendance(present, absent, daysOfWork, daysOfAbsent,
                            worker.getContractId(), UtilClass.getCurrentUserId(), worker.getSeqId())
                    .enqueue(new Callback<StatusResponse>() {
                        @Override
                        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                            if (response.isSuccessful()) {
                                StatusResponse statusResponse = response.body();
                                if (statusResponse != null) {
                                    Toast.makeText(mContext, statusResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<StatusResponse> call, Throwable t) {

                        }
                    });

        }
    }

}
