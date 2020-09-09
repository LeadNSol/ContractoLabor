package com.leadn.contractolabor.ui.credentials;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.MainActivity;
import com.leadn.contractolabor.ui.credentials.model.UserResponse;
import com.leadn.contractolabor.utils.InputValidator;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.shared_preferences.SharedPreferenceHelper;
import com.leadn.contractolabor.utils.web_apis.UserServices;

import java.io.File;

import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationFragment extends Fragment {

    private InputValidator mValidator;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private View view;
    private AppCompatActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registration, container, false);
        initViews();
        return view;
    }

    private ImageView imgContractor;
    private EditText etName, etPassword, etAddress;
    private TextInputLayout nameInputLayout, passwordInputLayout, addressInputLayout;

    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        if (mActivity != null && mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().setTitle("Registration");
        }

        mValidator = new InputValidator(mActivity);

        imgContractor = view.findViewById(R.id.img_contractor);

        etName = view.findViewById(R.id.et_name);
        etPassword = view.findViewById(R.id.et_password);
        etAddress = view.findViewById(R.id.et_address);


        nameInputLayout = view.findViewById(R.id.text_input_layout_name);
        passwordInputLayout = view.findViewById(R.id.text_input_layout_password);
        addressInputLayout = view.findViewById(R.id.text_input_layout_address);

        Button btnRegister = view.findViewById(R.id.btn_register);

        TextView txtImgCustomer = view.findViewById(R.id.txt_choose_image);
        imgContractor.setOnClickListener(view ->
                onImageClick());

        if (txtImgCustomer.getText().toString().equalsIgnoreCase(mActivity.getResources().getString(R.string.choose_image))) {
            txtImgCustomer.setOnClickListener(view -> onImageClick());
        }
        btnRegister.setOnClickListener(view -> onRegisterClick());


    }


    private String imagePath;
    private Uri imageUri;

    private void onImageClick() {
        if (checkCameraPermissions()) {
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

    private boolean checkCameraPermissions() {

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return false;
        } else
            return true;


    }

    private File imageFile;
    private MultipartBody.Part partFileToUpload;
    private String phoneNumber;

    private void onRegisterClick() {
        if (mValidator.isInputEditTextFilled(etName, nameInputLayout, "Name is Required")
                && mValidator.isInputEditTextFilled(etPassword, passwordInputLayout, "Password is Required")
                && mValidator.isInputEditTextFilled(etAddress, addressInputLayout, "Address is Required")) {

            if (imagePath != null) {
                imageFile = new File(imagePath);
                if (imageFile.isFile()) {
                    RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("*/*"));
                    partFileToUpload = MultipartBody.Part.createFormData("Image", imageFile.getName(), requestBody);
                }
            } else {
                RequestBody requestBody = RequestBody.create("", MediaType.parse("*/*"));
                partFileToUpload = MultipartBody.Part.createFormData("Image", "", requestBody);
            }


            if (SharedPreferenceHelper.getHelper().getPhoneNumber() != null)
                phoneNumber = SharedPreferenceHelper.getHelper().getPhoneNumber();

            RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"), etName.getText().toString().trim());
            RequestBody requestBodyPhone = RequestBody.create(MediaType.parse("multipart/form-data"), phoneNumber);
            RequestBody requestBodyAddress = RequestBody.create(MediaType.parse("multipart/form-data"), etAddress.getText().toString().trim());
            RequestBody requestBodyPassword = RequestBody.create(MediaType.parse("multipart/form-data"), etPassword.getText().toString().trim());


            registerUser(requestBodyName, requestBodyPhone, requestBodyPassword, requestBodyAddress);

        } else {
            Toast.makeText(mActivity, "All fields must be filled!", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(RequestBody name, RequestBody phone, RequestBody password, RequestBody address) {

        UserServices userServices = RetrofitHelper.getInstance().getUserClient();
        userServices.registerNewUser(partFileToUpload, name, phone, password, address)
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            UserResponse userResponse = response.body();
                            if (userResponse != null) {
                                if (userResponse.getStatus()) {
                                    Toast.makeText(mActivity, userResponse.getMessage(), Toast.LENGTH_SHORT).show();

                                    SharedPreferenceHelper.getHelper().setUserLoggedInData(new Gson().toJson(userResponse));
                                    startActivity(new Intent(mActivity, MainActivity.class));
                                    mActivity.finish();
                                } else {
                                    Toast.makeText(mActivity, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });


    }


}
