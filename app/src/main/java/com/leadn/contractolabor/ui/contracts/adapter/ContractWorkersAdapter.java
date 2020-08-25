package com.leadn.contractolabor.ui.contracts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;
import com.leadn.contractolabor.utils.AppConstant;

import java.util.List;

public class ContractWorkersAdapter extends RecyclerView.Adapter<ContractWorkersAdapter.ContractWorkersViewHolder> {
    private Context mContext;
    private List<WorkerResponse.Worker> mWorkersList;

    public ContractWorkersAdapter(Context mContext, List<WorkerResponse.Worker> mWorkersList) {
        this.mContext = mContext;
        this.mWorkersList = mWorkersList;
    }

    @NonNull
    @Override
    public ContractWorkersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.single_contract_workers_dialog_list_items, parent, false);
        return new ContractWorkersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractWorkersViewHolder holder, int position) {
        WorkerResponse.Worker worker = mWorkersList.get(position);
        holder.setData(worker);
    }

    @Override
    public int getItemCount() {
        return mWorkersList.size();
    }

    public class ContractWorkersViewHolder extends RecyclerView.ViewHolder {

        private TextView txtWorkerName, txtDailyWage, txtDayOfWork, txtWorkAt, txtWorkerType;
        private ImageView imgWorker;
        private TextView txtTotalWage, txtStatus;

        public ContractWorkersViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWorkerName = itemView.findViewById(R.id.txt_worker_name);
            txtDailyWage = itemView.findViewById(R.id.txt_worker_daily_wage);
            txtDayOfWork = itemView.findViewById(R.id.txt_worker_days_of_work);
            txtWorkAt = itemView.findViewById(R.id.txt_worker_at);
            txtWorkerType = itemView.findViewById(R.id.txt_worker_type);

            imgWorker = itemView.findViewById(R.id.img_worker);
            txtTotalWage = itemView.findViewById(R.id.txt_total_wages);
            txtStatus = itemView.findViewById(R.id.txt_payment_status);
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
            double totalWage = Double.parseDouble(worker.getDailyWage()) * Double.parseDouble(worker.getDaysOfWork());
            String rupee = "Rs ";
            txtTotalWage.setText(rupee.concat(String.valueOf(totalWage)));

            if (worker.getPaymentStatus().equalsIgnoreCase(AppConstant.PAYMENT_PAID)) {
                txtStatus.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
                txtStatus.setText(worker.getPaymentStatus());
            } else if (worker.getPaymentStatus().equalsIgnoreCase(AppConstant.PAYMENT_PARTIAL)) {
                txtStatus.setTextColor(mContext.getResources().getColor(R.color.colorLightBlue));
                txtStatus.setText(worker.getPaymentStatus());
            } else {
                txtStatus.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                txtStatus.setText(worker.getPaymentStatus());
            }
            itemView.setOnClickListener(view -> {

            });
        }
    }
}
