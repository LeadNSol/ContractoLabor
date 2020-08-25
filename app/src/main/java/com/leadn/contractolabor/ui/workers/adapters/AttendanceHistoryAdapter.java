package com.leadn.contractolabor.ui.workers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.workers.model.AttendanceResponse;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;

import java.util.List;

public class AttendanceHistoryAdapter extends RecyclerView.Adapter<AttendanceHistoryAdapter.AttendanceViewHolder> {

    private Context mContext;
    private List<AttendanceResponse.Attendance> mAttendanceList;
    private TextView txtTotalPresent, txtTotalAbsent;
    int totalPresent, totalAbsent;

    public AttendanceHistoryAdapter(Context mContext, List<AttendanceResponse.Attendance> mAttendanceList,
                                    TextView txtTotalPresent, TextView txtTotalAbsent) {
        this.mContext = mContext;
        this.mAttendanceList = mAttendanceList;
        this.txtTotalPresent = txtTotalPresent;
        this.txtTotalAbsent = txtTotalAbsent;
        this.totalAbsent = 0;
        this.totalPresent = 0;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_attendance_list_items, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        AttendanceResponse.Attendance attendance = mAttendanceList.get(position);
        holder.setData(attendance);
    }

    @Override
    public int getItemCount() {
        return mAttendanceList.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        private TextView txtContractName, txtPresent, txtAbsent, txtDate;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContractName = itemView.findViewById(R.id.txt_contract_name);
            txtPresent = itemView.findViewById(R.id.txt_present);
            txtAbsent = itemView.findViewById(R.id.txt_absent);
            txtDate = itemView.findViewById(R.id.txt_date);
        }

        public void setData(AttendanceResponse.Attendance attendance) {

            txtContractName.setText(attendance.getContractName());
            txtPresent.setText(attendance.getPresent());
            txtAbsent.setText(attendance.getAbsent());
            txtDate.setText(attendance.getCreatedDate());


            totalPresent += Integer.parseInt(attendance.getPresent());
            totalAbsent += Integer.parseInt(attendance.getAbsent());

            txtTotalAbsent.setText(String.valueOf(totalAbsent));
            txtTotalPresent.setText(String.valueOf(totalPresent));
        }
    }

    public interface OnWorkerClickListener {
        void onWorkerClick(WorkerResponse.Worker worker);
    }
}
