package com.leadn.contractolabor.ui.contracts;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.ui.contracts.model.OwnerResponse;
import com.leadn.contractolabor.ui.credentials.model.UserResponse;
import com.leadn.contractolabor.utils.InputValidator;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.UtilClass;
import com.leadn.contractolabor.utils.shared_preferences.SharedPreferenceHelper;
import com.leadn.contractolabor.utils.web_apis.ContractServices;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import gun0912.tedimagepicker.builder.TedImagePicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewContractFragment extends Fragment {

    public NewContractFragment() {

    }

    private AppCompatActivity mActivity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_contract, container, false);
        initViews();
        return view;
    }

    private EditText etName, etAddress, etSqFeet;
    private TextInputLayout nameInputLayout, addressInputLayout, sqFeetInputLayout;
    private ContractServices mContractService;
    private InputValidator mInputValidator;
    private MaterialSpinner spOwners;
    private TextView txtStartingDate;

    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        if (mActivity != null && mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().setTitle("Contract Addition");
        }

        mContractService = RetrofitHelper.getInstance().getContractClient();

        etName = view.findViewById(R.id.et_name);
        etAddress = view.findViewById(R.id.et_address);
        etSqFeet = view.findViewById(R.id.et_sq_feet);

        mInputValidator = new InputValidator(mActivity);

        nameInputLayout = view.findViewById(R.id.text_input_layout_name);
        addressInputLayout = view.findViewById(R.id.text_input_layout_address);
        sqFeetInputLayout = view.findViewById(R.id.text_input_layout_sq_feet);

        spOwners = view.findViewById(R.id.sp_owner);
        setUpOwnerSpinner();

        txtStartingDate = view.findViewById(R.id.txt_starting_date);
        txtStartingDate.setOnClickListener(view -> {
            UtilClass.initDatePicker(mActivity, txtStartingDate, "");
        });

        ImageView imgContract = view.findViewById(R.id.img_contract);
        imgContract.setOnClickListener(view1 -> {
            onImageClick(imgContract);
        });

        Button btnAddContract = view.findViewById(R.id.btn_add_contract);
        btnAddContract.setOnClickListener(view -> {
            if (mInputValidator.isInputEditTextFilled(etName, nameInputLayout, "Name is required!")
                    && mInputValidator.isInputEditTextFilled(etSqFeet, sqFeetInputLayout, "Sq feet price must be like 110, 120 !")
                    && mInputValidator.isInputEditTextFilled(etAddress, addressInputLayout, "Address is required!"))
                if (selectedOwner != null) {
                    addNewContract();
                } else
                    spOwners.setError("Owner isn't Selected!");
        });
    }


    private String selectedOwner;
    private List<OwnerResponse.ContractOwner> ownerList;

    private void setUpOwnerSpinner() {
        ownerList = new ArrayList<>();
        mContractService.getOwners().enqueue(new Callback<OwnerResponse>() {
            @Override
            public void onResponse(Call<OwnerResponse> call, Response<OwnerResponse> response) {
                if (response.isSuccessful()) {
                    OwnerResponse ownerResponse = response.body();
                    if (ownerResponse != null) {
                        ownerList = ownerResponse.getContractOwnerList();
                        List<String> ownerNames = new ArrayList<>();
                        if (ownerList.size() > 0) {
                            for (OwnerResponse.ContractOwner contractOwner : ownerList) {
                                ownerNames.add(contractOwner.getName());
                            }

                        }
                        ownerNames.add("other");
                        spOwners.setItems(ownerNames);
                    }
                }
            }

            @Override
            public void onFailure(Call<OwnerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        spOwners.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            selectedOwner = item;
            if (item.equalsIgnoreCase("other")) {
                showOwnerAdditionDialog(spOwners);
            }
        });

    }

    private Dialog mAddOwnerDialog;

    private void showOwnerAdditionDialog(MaterialSpinner spOwner) {
        if (mAddOwnerDialog == null) {
            mAddOwnerDialog = new Dialog(mActivity);
            mAddOwnerDialog.setCancelable(false);
            mAddOwnerDialog.setContentView(R.layout.dialog_add_new_owner);

            int width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.8);
            int height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.8);
            if (mAddOwnerDialog.getWindow() != null) {
                mAddOwnerDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mAddOwnerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            EditText etName, etPhone, etAddress;
            TextInputLayout nameInputLayout, phoneInputLayout, addressInputLayout;

            etName = mAddOwnerDialog.findViewById(R.id.et_name);
            etPhone = mAddOwnerDialog.findViewById(R.id.et_phone);
            etAddress = mAddOwnerDialog.findViewById(R.id.et_address);

            nameInputLayout = mAddOwnerDialog.findViewById(R.id.text_input_layout_name);
            phoneInputLayout = mAddOwnerDialog.findViewById(R.id.text_input_layout_phone);
            addressInputLayout = mAddOwnerDialog.findViewById(R.id.text_input_layout_address);

            Button btnAddOwner = mAddOwnerDialog.findViewById(R.id.btn_add_owner);
            btnAddOwner.setOnClickListener(view -> {
                if (mInputValidator.isInputEditTextFilled(etName, nameInputLayout, "name is required")
                        && mInputValidator.isValidPhoneNumber(etPhone, phoneInputLayout, "Phone number should be like 03331234567")
                        && mInputValidator.isInputEditTextFilled(etAddress, addressInputLayout, "address is required")) {
                    addOwner(etName.getText().toString(), etPhone.getText().toString(), etAddress.getText().toString());
                }
            });
            TextView txtClose = mAddOwnerDialog.findViewById(R.id.txt_close);
            txtClose.setOnClickListener(view -> {
                mAddOwnerDialog.dismiss();
                mAddOwnerDialog = null;
            });


            mAddOwnerDialog.show();
        } else {
            mAddOwnerDialog.dismiss();
            mAddOwnerDialog = null;
        }
    }

    private void addOwner(String name, String phone, String address) {
        mContractService.postOwner(name, phone, address)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<StatusResponse> call, @NonNull Response<StatusResponse> response) {
                        if (response.isSuccessful()) {
                            StatusResponse statusResponse = response.body();
                            if (statusResponse != null)
                                if (statusResponse.getStatus()) {
                                    Toast.makeText(mActivity, statusResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    setUpOwnerSpinner();
                                    mAddOwnerDialog.dismiss();
                                    mAddOwnerDialog = null;
                                } else {
                                    Toast.makeText(mActivity, statusResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StatusResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    private String imagePath;

    private void onImageClick(ImageView imgWorker) {
        if (UtilClass.checkImagePermissions(mActivity)) {
            try {
                TedImagePicker.with(mActivity)
                        .showTitle(true)
                        //.setCompleteButtonText("Done")
                        //.setEmptySelectionText("No Select")
                        .start(uri -> {
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

    private MultipartBody.Part fileTOUpload;

    private void addNewContract() {
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
        RequestBody requestBodyName = RequestBody.create(etName.getText().toString(), MediaType.parse("multipart/form-data"));
        RequestBody requestBodySqFeet = RequestBody.create(etSqFeet.getText().toString(), MediaType.parse("multipart/form-data"));
        RequestBody requestBodyAddress = RequestBody.create(etAddress.getText().toString(), MediaType.parse("multipart/form-data"));
        RequestBody requestBodyOwnerId = null;
        for (OwnerResponse.ContractOwner contractOwner : ownerList) {
            if (contractOwner.getName().equalsIgnoreCase(selectedOwner)) {
                requestBodyOwnerId = RequestBody.create(contractOwner.getSeqId(), MediaType.parse("multipart/form-data"));
            }
        }
        RequestBody requestBodyDate = RequestBody.create(txtStartingDate.getText().toString(), MediaType.parse("multipart/form-data"));

        RequestBody requestBodyUserId = RequestBody.create(UtilClass.getCurrentUserId(), MediaType.parse("multipart/form-data"));

        postNewContract(
                requestBodyName,
                requestBodySqFeet,
                requestBodyAddress,
                requestBodyDate,
                requestBodyOwnerId,
                requestBodyUserId
        );
    }

    private void postNewContract(RequestBody name, RequestBody sqFeetPrice,
                                 RequestBody address, RequestBody date,
                                 RequestBody ownerId, RequestBody userId) {
        mContractService.postContract(fileTOUpload, name, sqFeetPrice, address, date, ownerId, userId)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StatusResponse> call, @NonNull Response<StatusResponse> response) {
                        if (response.isSuccessful()) {
                            StatusResponse statusResponse = response.body();
                            if (statusResponse != null) {
                                Toast.makeText(mActivity, statusResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StatusResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });

    }

}