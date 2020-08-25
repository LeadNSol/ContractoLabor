package com.leadn.contractolabor.ui.workers;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.ui.credentials.model.UserResponse;
import com.leadn.contractolabor.ui.workers.adapters.WorkersAdapter;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;
import com.leadn.contractolabor.utils.InputValidator;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.UtilClass;
import com.leadn.contractolabor.utils.shared_preferences.SharedPreferenceHelper;
import com.leadn.contractolabor.utils.web_apis.WorkerServices;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WorkersFragment extends Fragment implements WorkersAdapter.OnWorkerClickListener {

    public WorkersFragment() {
        // Required empty public constructor
    }

    private View view;
    private AppCompatActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_workers, container, false);
        initViews();
        return view;
    }

    private RecyclerView mRvWorkers;
    private InputValidator mInputValidator;

    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        if (mActivity != null && mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().setTitle("Workers");
        }
        mInputValidator = new InputValidator(mActivity);

        mRvWorkers = view.findViewById(R.id.rv_workers);
        mRvWorkers.setLayoutManager(new LinearLayoutManager(mActivity));
        mWorkerService = RetrofitHelper.getInstance().getWorkersClient();

        getWorkers();

        Button btnAddWorker = view.findViewById(R.id.btn_add_new_worker);
        btnAddWorker.setOnClickListener(view -> {
            showAddNewWorkerDialog();
        });

    }

    private Dialog mAddWorkerDialog;

    private void showAddNewWorkerDialog() {
        if (mAddWorkerDialog == null) {
            mAddWorkerDialog = new Dialog(mActivity);
            // mAddWorkerDialog.setCancelable(false);
            mAddWorkerDialog.setContentView(R.layout.dialog_add_new_worker);

            int width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.95);
            int height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.9);
            mAddWorkerDialog.getWindow().setLayout(width, height);


            TextInputLayout textInputLayoutName, textInputLayoutPhone, textInputLayoutWage, textInputLayoutAddress;
            EditText etName, etPhone, etWage, etAddress;
            MaterialSpinner spType;
            ImageView imgWorker;
            // Button btnAddWorker;

            etName = mAddWorkerDialog.findViewById(R.id.et_name);
            etPhone = mAddWorkerDialog.findViewById(R.id.et_phone);
            etWage = mAddWorkerDialog.findViewById(R.id.et_daily_wage);
            etAddress = mAddWorkerDialog.findViewById(R.id.et_address);

            textInputLayoutAddress = mAddWorkerDialog.findViewById(R.id.text_input_layout_address);
            textInputLayoutName = mAddWorkerDialog.findViewById(R.id.text_input_layout_name);
            textInputLayoutPhone = mAddWorkerDialog.findViewById(R.id.text_input_layout_phone);
            textInputLayoutWage = mAddWorkerDialog.findViewById(R.id.text_input_layout_wage);

            imgWorker = mAddWorkerDialog.findViewById(R.id.img_worker);
            imgWorker.setOnClickListener(view -> {
                onImageClick(imgWorker);
            });

            spType = mAddWorkerDialog.findViewById(R.id.sp_worker_type);
            /*spType.setItems("Mason", "Labor");
            spType.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
                selectedType = item;
            })*/
            ;
            setSpinner(spType);


            Button btnAdd = mAddWorkerDialog.findViewById(R.id.btn_add_worker);
            btnAdd.setOnClickListener(view -> {
                if (mInputValidator.isInputEditTextFilled(etName, textInputLayoutName, "Name is required!")
                        && mInputValidator.isValidPhoneNumber(etPhone, textInputLayoutPhone, "Phone number must be like 03001234567")
                        && mInputValidator.isInputEditTextFilled(etWage, textInputLayoutWage, "Wage is required!")
                        && mInputValidator.isInputEditTextFilled(etAddress, textInputLayoutAddress, "Address is required!")) {

                    if (selectedType == null)
                        spType.setError("Type isn't selected!");
                    else
                        addWorker(selectedType,
                                etName.getText().toString().trim(),
                                etPhone.getText().toString().trim(),
                                etWage.getText().toString().trim(),
                                etAddress.getText().toString().trim());

                }

            });

            TextView btnClose = mAddWorkerDialog.findViewById(R.id.txt_close);
            btnClose.setOnClickListener(view -> {
                mAddWorkerDialog.dismiss();
                mAddWorkerDialog = null;
            });

            // mAddWorkerDialog.setContentView(R.layout.dialog_add_new_worker);
            mAddWorkerDialog.show();
        } else {
            mAddWorkerDialog.dismiss();
            mAddWorkerDialog = null;
        }
    }

    private String imagePath;

    private void onImageClick(ImageView imgWorker) {
        if (UtilClass.checkImagePermissions(mActivity)) {
            try {
                TedBottomPicker.with(mActivity)
                        .showTitle(true)
                        .setCompleteButtonText("Done")
                        .setEmptySelectionText("No Select")
                        .show(uri -> {
                            imagePath = uri.getPath();
                            Glide.with(mActivity)
                                    .load(uri)
                                    //.apply(RequestOptions.circleCropTransform())
                                    .into(imgWorker);
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mActivity, "permission not allowed", Toast.LENGTH_SHORT).show();
    }


    private String selectedType;

    private void setSpinner(MaterialSpinner spType) {

        spType.setItems("Mason", "Labor");
        spType.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            selectedType = item;
        });
    }

    private MultipartBody.Part fileTOUpload;

    private void addWorker(String selectedType, String name, String phone, String wage, String address) {
        if (imagePath != null) {
            File imageFile = new File(imagePath);
            if (imageFile.isFile()) {
                RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("*/*"));
                fileTOUpload = MultipartBody.Part.createFormData("Image", imageFile.getName(), requestBody);
            }
        } else {
            RequestBody requestBody = RequestBody.create("", MediaType.parse("*/*"));
            fileTOUpload = MultipartBody.Part.createFormData("Image", "", requestBody);
        }

        RequestBody requestBodyName = RequestBody.create(name, MediaType.parse("multipart/form-data"));
        RequestBody requestBodyPhone = RequestBody.create(phone, MediaType.parse("multipart/form-data"));
        RequestBody requestBodyWage = RequestBody.create(wage, MediaType.parse("multipart/form-data"));
        RequestBody requestBodyAddress = RequestBody.create(address, MediaType.parse("multipart/form-data"));
        RequestBody requestBodyType = RequestBody.create(selectedType, MediaType.parse("multipart/form-data"));

        String date = UtilClass.getCurrentDate();
        RequestBody requestBodyDate = RequestBody.create(date, MediaType.parse("multipart/form-data"));

        Integer UserID = 0;
        if (SharedPreferenceHelper.getHelper().getUserLoggedInData() != null) {
            UserResponse userResponse = new Gson().fromJson(SharedPreferenceHelper.getHelper().getUserLoggedInData(), UserResponse.class);
            UserID = userResponse.getSeqId();
        }
        RequestBody requestBodyUserId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(UserID));


        postWorker(requestBodyName,
                requestBodyType,
                requestBodyPhone,
                requestBodyWage,
                requestBodyAddress,
                requestBodyDate,
                requestBodyUserId
        );


    }

    private void postWorker(RequestBody name, RequestBody type, RequestBody phone, RequestBody wage,
                            RequestBody address, RequestBody date, RequestBody userId) {
//, RequestBody userId

        mWorkerService.postWorker(fileTOUpload, name, type, phone, wage, address, date, userId)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        if (response.isSuccessful()) {
                            StatusResponse statusResponse = response.body();
                            if (statusResponse != null) {
                                Toast.makeText(mActivity, statusResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                mAddWorkerDialog.dismiss();
                                mAddWorkerDialog = null;
                                getWorkers();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

    }

    private List<WorkerResponse.Worker> mWorkerList;
    private WorkerServices mWorkerService;
    private WorkersAdapter.OnWorkerClickListener mOnWorkerClickListener;

    private void getWorkers() {
        mOnWorkerClickListener = this;
        mWorkerList = new ArrayList<>();
        mWorkerService.getWorkers().enqueue(new Callback<WorkerResponse>() {
            @Override
            public void onResponse(Call<WorkerResponse> call, Response<WorkerResponse> response) {
                if (response.isSuccessful()) {
                    WorkerResponse workerResponse = response.body();
                    if (workerResponse != null) {
                        mWorkerList = workerResponse.getWorkersList();
                        if (mWorkerList.size() > 0) {
                            WorkersAdapter workersAdapter = new WorkersAdapter(mActivity, mWorkerList, mOnWorkerClickListener);
                            mRvWorkers.setAdapter(workersAdapter);
                            workersAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(mActivity, "no Workers is found!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onWorkerClick(WorkerResponse.Worker worker) {
        UtilClass.pushFragment(new WorkerProfileFragment(worker), mActivity, R.id.main_frame_layout, true);
    }

   /* private Dialog mProfileDialog;

    private void showWorkerProfileDialog(WorkerResponse.Worker worker) {
        if (mProfileDialog == null) {
            mProfileDialog = new Dialog(mActivity);
            mProfileDialog.setCancelable(false);

            mProfileDialog.setContentView(R.layout.fragment_assigns_workers_to_contract);
            if (mProfileDialog.getWindow() != null) {
                mProfileDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mProfileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            mProfileDialog.show();
        } else {
            mProfileDialog.dismiss();
            mProfileDialog = null;
        }
    }*/
}