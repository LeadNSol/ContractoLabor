package com.leadn.contractolabor.ui.workers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;

import java.util.List;

public class WorkersAdapter extends RecyclerView.Adapter<WorkersAdapter.WorkersViewHolder> {

    private Context mContext;
    private List<WorkerResponse.Worker> mWorkerList;

    private OnWorkerClickListener mOnWorkerClickListener;

    public WorkersAdapter(Context mContext, List<WorkerResponse.Worker> mWorkerList, OnWorkerClickListener mOnWorkerClickListener) {
        this.mContext = mContext;
        this.mWorkerList = mWorkerList;
        this.mOnWorkerClickListener = mOnWorkerClickListener;
    }

    @NonNull
    @Override
    public WorkersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_workers_list_items, parent, false);
        return new WorkersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkersViewHolder holder, int position) {
        WorkerResponse.Worker worker = mWorkerList.get(position);
        holder.setData(worker);
    }

    @Override
    public int getItemCount() {
        return mWorkerList.size();
    }

    public class WorkersViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgWorker;
        private TextView txtWorkerName, txtPhone, txtDayOfWork, txtDailyWage, txtWorkAt, txtWorkerType, txtPaymentStatus;

        public WorkersViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWorker = itemView.findViewById(R.id.img_worker);
            txtWorkerName = itemView.findViewById(R.id.txt_worker_name);
            txtPhone = itemView.findViewById(R.id.txt_worker_phone);
            txtDayOfWork = itemView.findViewById(R.id.txt_worker_days_of_work);
            txtDailyWage = itemView.findViewById(R.id.txt_worker_wage);
            txtWorkAt = itemView.findViewById(R.id.txt_is_free);
            txtWorkerType = itemView.findViewById(R.id.txt_worker_type);
            //txtPaymentStatus = itemView.findViewById(R.id.txt_payment_status);


        }

        public void setData(WorkerResponse.Worker worker) {

            txtWorkerName.setText(worker.getName());
            txtPhone.setText(worker.getPhone());
            txtDayOfWork.setText(worker.getDaysOfWork());
            txtDailyWage.setText(worker.getDailyWage());
            if (worker.getIsFree().equalsIgnoreCase("0"))
                txtWorkAt.setText(worker.getDaysOfWork());
            else
                txtWorkAt.setText(mContext.getResources().getString(R.string.is_free));

            txtWorkerType.setText(worker.getType());
            itemView.setOnClickListener(view -> {
                mOnWorkerClickListener.onWorkerClick(worker);
            });
            //txtWorkerType.setText(worker.getPaymentStatus());
        }
    }

    public interface OnWorkerClickListener {
        void onWorkerClick(WorkerResponse.Worker worker);
    }
}
