package com.leadn.contractolabor.ui.users;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.credentials.model.UserResponse;
import com.leadn.contractolabor.utils.AppConstant;
import com.leadn.contractolabor.utils.InputValidator;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.UtilClass;
import com.leadn.contractolabor.utils.shared_preferences.SharedPreferenceHelper;
import com.leadn.contractolabor.utils.web_apis.UserServices;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserFragment extends Fragment {
    private InputValidator mValidator;

    public UpdateUserFragment() {
    }

    private View view;
    private AppCompatActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration, container, false);
        initViews();
        return view;
    }

    private ImageView imgContractor;
    private EditText etName, etPassword, etAddress;
    private TextInputLayout nameInputLayout, addressInputLayout;

    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        assert mActivity != null && mActivity.getSupportActionBar() != null;
        mActivity.getSupportActionBar().setTitle("Update User");


        mValidator = new InputValidator(mActivity);

        imgContractor = view.findViewById(R.id.img_contractor);

        etName = view.findViewById(R.id.et_name);
        etPassword = view.findViewById(R.id.et_password);
        etPassword.setVisibility(View.GONE);

        etAddress = view.findViewById(R.id.et_address);


        nameInputLayout = view.findViewById(R.id.text_input_layout_name);
        addressInputLayout = view.findViewById(R.id.text_input_layout_address);

        Button btnUpdate = view.findViewById(R.id.btn_register);
        btnUpdate.setText("Update");
        TextView txtImgCustomer = view.findViewById(R.id.txt_choose_image);
        imgContractor.setOnClickListener(view ->
                onImageClick());

        if (txtImgCustomer.getText().toString().equalsIgnoreCase(mActivity.getResources().getString(R.string.choose_image))) {
            txtImgCustomer.setOnClickListener(view -> onImageClick());
        }
        //btnRegister.setOnClickListener(view -> onRegisterClick());
        btnUpdate.setOnClickListener(view -> {
            onUpdate();
        });

        loadProfileData();

    }

    private UserResponse userResponse;

    private void loadProfileData() {
        if (SharedPreferenceHelper.getHelper().getUserLoggedInData() != null) {
            userResponse = new Gson().fromJson(SharedPreferenceHelper.getHelper().getUserLoggedInData(), UserResponse.class);

            Glide.with(mActivity)
                    .load(AppConstant.USER_IMAGE_URL.concat(userResponse.getImageUrl()))
                    .error(R.drawable.profile)
                    .into(imgContractor);

            etName.setText(userResponse.getName());
            etAddress.setText(userResponse.getAddress());
        }
    }

    private MultipartBody.Part partFileToUpload;

    private void onUpdate() {
        UserServices mUserServices = RetrofitHelper.getInstance().getUserClient();
        if (mValidator.isInputEditTextFilled(etName, nameInputLayout, "Name is Required")
                && mValidator.isInputEditTextFilled(etAddress, addressInputLayout, "Address is Required")) {
            File imageFile;
            if (imagePath != null) {
                imageFile = new File(imagePath);
                if (imageFile.isFile()) {
                    RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("*/*"));
                    partFileToUpload = MultipartBody.Part.createFormData("Image", imageFile.getName(), requestBody);
                }
            } else {
                imageFile = new File(AppConstant.USER_IMAGE_URL.concat(userResponse.getImageUrl()));
                RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("*/*"));
                partFileToUpload = MultipartBody.Part.createFormData("Image", imageFile.getName(), requestBody);
            }
            RequestBody name = RequestBody.create(etName.getText().toString().trim(), MediaType.parse("multipart/form-data"));
            RequestBody address = RequestBody.create(etAddress.getText().toString().trim(), MediaType.parse("multipart/form-data"));
            RequestBody userId = RequestBody.create(String.valueOf(userResponse.getSeqId()), MediaType.parse("multipart/form-data"));

            mUserServices
                    .updateUser(partFileToUpload, name, address, userId)
                    .enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<UserResponse> call, @NotNull Response<UserResponse> response) {
                            if (response.isSuccessful()) {
                                UserResponse userResponse = response.body();
                                if (userResponse != null) {
                                    SharedPreferenceHelper.getHelper().setUserLoggedInData(new Gson().toJson(userResponse));
                                    Toast.makeText(mActivity, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    loadProfileData();
                                }
                            }
                        }
                        @Override
                        public void onFailure(@NotNull Call<UserResponse> call, @NotNull Throwable t) {
                            t.printStackTrace();
                        }
                    });

        }
    }

    private String imagePath;
    private Uri imageUri;
    private void onImageClick() {
        if (UtilClass.checkImagePermissions(mActivity)) {
            try {
                TedBottomPicker.with(mActivity)
                        .showTitle(true)
                        .setCompleteButtonText("Done")
                        .setEmptySelectionText("No Select")
                        .show(uri -> {
                            imagePath = uri.getPath();
                            imageUri = uri;
                            Glide.with(mActivity)
                                    .load(imagePath)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(imgContractor);
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(mActivity, "permission not allowed", Toast.LENGTH_SHORT).show();
    }
}
