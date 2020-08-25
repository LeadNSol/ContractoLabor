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

import java.util.ArrayList;
import java.util.List;

public class AssignWorkerToContractAdapter extends RecyclerView.Adapter<AssignWorkerToContractAdapter.AssignWorkerToContractViewHolder> {
    private Context mContext;
    private List<WorkerResponse.Worker> mWorkersList;
     public static List<String> mSelectedWorkerList;
    private boolean isLongPressedOnce = false;

    public AssignWorkerToContractAdapter(Context mContext,
                                         List<WorkerResponse.Worker> mWorkersList) {
        this.mContext = mContext;
        this.mWorkersList = mWorkersList;
        mSelectedWorkerList = new ArrayList<>();
    }

    @NonNull
    @Override
    public AssignWorkerToContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.single_asign_worker_dialog_list_items, parent, false);
        return new AssignWorkerToContractViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignWorkerToContractViewHolder holder, int position) {
        WorkerResponse.Worker worker = mWorkersList.get(position);
        holder.setData(worker);
    }

    @Override
    public int getItemCount() {
        return mWorkersList.size();
    }

    public class AssignWorkerToContractViewHolder extends RecyclerView.ViewHolder {

        private TextView txtWorkerName, txtDailyWage, txtPhone, txtWorkerType;
        private ImageView imgWorker;

        public AssignWorkerToContractViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWorkerName = itemView.findViewById(R.id.txt_worker_name);
            txtDailyWage = itemView.findViewById(R.id.txt_worker_daily_wage);
            txtPhone = itemView.findViewById(R.id.txt_worker_phone);
            txtWorkerType = itemView.findViewById(R.id.txt_worker_type);

            imgWorker = itemView.findViewById(R.id.img_worker);
            isLongPressedOnce = false;

        }

        public void setData(WorkerResponse.Worker worker) {

            txtWorkerType.setText(worker.getType());
            txtWorkerName.setText(worker.getName());
            txtPhone.setText(worker.getPhone());
            txtDailyWage.setText(worker.getDailyWage());

            Glide.with(mContext)
                    .load(worker.getImageUrl())
                    .error(R.drawable.ic_baseline_supervisor_account)
                    .into(imgWorker);

            itemView.setOnLongClickListener(view -> {
                //mOnWorkerClickListener.onWorkerLongClick(worker);
                isLongPressedOnce = !isLongPressedOnce;
                if (isLongPressedOnce) {
                    Glide.with(mContext)
                            .load(R.drawable.blue_check)
                            .into(imgWorker);
                    mSelectedWorkerList.add(worker.getSeqId());
                } else {
                    Glide.with(mContext)
                            .load(worker.getImageUrl())
                            .error(R.drawable.ic_baseline_supervisor_account)
                            .into(imgWorker);
                    mSelectedWorkerList.remove(worker.getSeqId());
                }
                //mOnWorkerClickListener.onWorkerLongClick(mSelectedWorkerList);
                return false;
            });
        }
    }

}
