package com.leadn.contractolabor.ui.contracts.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.ui.contracts.model.ContractResponse;
import com.leadn.contractolabor.ui.contracts.profile.ContractProfileFragment;
import com.leadn.contractolabor.utils.InputValidator;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.UtilClass;
import com.leadn.contractolabor.utils.web_apis.ContractServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ContractViewHolder> {
    private Context mContext;
    private List<ContractResponse.Contract> mContractList;

    private OnContractClickListener mClickListener;
    private ContractResponse.Contract mContract;

    public ContractAdapter(Context mContext, List<ContractResponse.Contract> mContractList, OnContractClickListener mClickListener) {
        this.mContext = mContext;
        this.mContractList = mContractList;
        this.mClickListener = mClickListener;

    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.single_contract_list_items, parent, false);

        return new ContractViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractViewHolder holder, int position) {
        ContractResponse.Contract contract = mContractList.get(position);
        holder.bind(contract);
    }

    @Override
    public int getItemCount() {
        return mContractList.size();
    }

    public class ContractViewHolder extends RecyclerView.ViewHolder {
        private InputValidator mValidator;
        private TextView txtContractName, txtWorkers, txtAttendance, txtExpenses;
        private ImageView imgContract;
        private ContractServices mContractServices;


        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContractName = itemView.findViewById(R.id.txt_contract_name);
            txtWorkers = itemView.findViewById(R.id.txt_workers);
            txtAttendance = itemView.findViewById(R.id.txt_attendance);
            txtExpenses = itemView.findViewById(R.id.txt_expenses);

            imgContract = itemView.findViewById(R.id.img_contract);
            mContractServices = RetrofitHelper.getInstance().getContractClient();
            mValidator = new InputValidator(mContext);

        }

        public void bind(ContractResponse.Contract contract) {

            txtContractName.setText(contract.getContractName());
            txtWorkers.setOnClickListener(view -> {
                mClickListener.onWorkerClick(contract);
            });
            txtAttendance.setOnClickListener(view -> {
                mClickListener.onAttendanceClick(contract);
            });
            txtExpenses.setOnClickListener(view -> {
                mClickListener.onExpensesClick(contract);
            });
            txtContractName.setOnClickListener(view -> {
                mClickListener.onMoreClick(contract);
            });
            txtContractName.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {

                MenuItem menuMarkAsComplete = contextMenu.add(Menu.NONE, 1, 1, "Mark as Complete");
                MenuItem menuDetails = contextMenu.add(Menu.NONE, 2, 2, "Details");
                MenuItem menuEdit = contextMenu.add(Menu.NONE, 3, 3, "Edit/Update");
                MenuItem menuDelete = contextMenu.add(Menu.NONE, 4, 4, "Delete");

                menuMarkAsComplete.setOnMenuItemClickListener(menuItemClickListener);
                menuDetails.setOnMenuItemClickListener(menuItemClickListener);
                menuEdit.setOnMenuItemClickListener(menuItemClickListener);
                menuDelete.setOnMenuItemClickListener(menuItemClickListener);
                mContract = contract;
            });


        }

        MenuItem.OnMenuItemClickListener menuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case 1:
                        Toast.makeText(mContext, "Mark as a completed", Toast.LENGTH_SHORT).show();
                        showCompleteContractDialog();
                        break;
                    case 2:
                        UtilClass.pushFragment(new ContractProfileFragment(mContract.getSeqId()), (AppCompatActivity) mContext, R.id.main_frame_layout, true);
                        break;
                    case 3:
                        Toast.makeText(mContext, " Edit clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        showDeleteContractDialog();
                        break;
                }
                return true;
            }
        };
        private Dialog mContractCompleteDialog;

        private void showCompleteContractDialog() {
            if (mContractCompleteDialog == null) {
                mContractCompleteDialog = new Dialog(mContext);
                mContractCompleteDialog.setCancelable(false);

                mContractCompleteDialog.setContentView(R.layout.dialog_contract_mark_as_complete);
                EditText etTotalArea;
                TextInputLayout inputLayoutTotalArea;
                TextView txtClose, txtChooseEndingDate;
                Button btnComplete;

                etTotalArea = mContractCompleteDialog.findViewById(R.id.et_total_area);
                inputLayoutTotalArea = mContractCompleteDialog.findViewById(R.id.text_input_layout_total_area);

                txtChooseEndingDate = mContractCompleteDialog.findViewById(R.id.txt_choose_date);
                txtChooseEndingDate.setOnClickListener(view -> {
                    UtilClass.initDatePicker(mContext, txtChooseEndingDate, "current");
                });

                btnComplete = mContractCompleteDialog.findViewById(R.id.btn_complete_contract);
                btnComplete.setOnClickListener(view -> {
                    if (mValidator.isInputEditTextFilled(etTotalArea, inputLayoutTotalArea, "Area is required!")) {
                        if (!txtChooseEndingDate.getText().toString().equalsIgnoreCase(mContext.getString(R.string.choose_ending_date))) {
                            double totalBudget = Double.parseDouble(mContract.getSqrFeetPrice()) * Double.parseDouble(etTotalArea.getText().toString());

                        } else {
                            txtChooseEndingDate.setError("Ending date is required!");
                        }
                    }
                });

                txtClose = mContractCompleteDialog.findViewById(R.id.txt_close);
                txtClose.setOnClickListener(view -> {
                    mContractCompleteDialog.dismiss();
                    mContractCompleteDialog = null;
                });
                mContractCompleteDialog.show();
            } else {
                mContractCompleteDialog.dismiss();
                mContractCompleteDialog = null;
            }
        }

        private void showDeleteContractDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Attention!");
            builder.setMessage("Do you wants to delete " + mContract.getContractName() + " Contract!");

            builder.setPositiveButton("Yes", (dialog, position) -> {
                deleteContract();
                dialog.dismiss();
            });
            builder.setNegativeButton("No", (dialog, position) -> {
                dialog.dismiss();
            });
            builder.show();


        }

        private void deleteContract() {
            mContractServices.deleteContract(mContract.getSeqId())
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
                            t.printStackTrace();
                        }
                    });
        }

    }

    public interface OnContractClickListener {

        void onWorkerClick(ContractResponse.Contract contract);

        void onExpensesClick(ContractResponse.Contract contract);

        void onAttendanceClick(ContractResponse.Contract contract);

        void onMoreClick(ContractResponse.Contract contract);
    }
}
