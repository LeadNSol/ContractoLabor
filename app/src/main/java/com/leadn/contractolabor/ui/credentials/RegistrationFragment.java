package com.leadn.contractolabor.ui.credentials;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.MainActivity;
import com.leadn.contractolabor.ui.credentials.model.UserResponse;
import com.leadn.contractolabor.utils.InputValidator;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.shared_preferences.SharedPreferenceHelper;
import com.leadn.contractolabor.utils.web_apis.UserServices;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import gun0912.tedimagepicker.builder.TedImagePicker;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationFragment extends Fragment {

    private static final String TAG = "RegistrationFragment";
    private InputValidator mValidator;

    public RegistrationFragment() {
        // Required empty public constructor
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
    //private Uri imageUri;

    private void onImageClick() {
        if (checkCameraPermissions()) {
            try {
                TedImagePicker.with(mActivity)
                        .showTitle(true)
                        //.setCompleteButtonText("Done")
                        //.setEmptySelectionText("No Select")
                        .start(uri -> {
                            imagePath = uri.getPath();
                            // imageUri = uri;
                            Glide.with(mActivity)
                                    .load(imagePath)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(imgContractor);
                        });
            } catch (Exception e) {
                Log.e(TAG, "onImageClick: ", e);
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

    private MultipartBody.Part partFileToUpload;
    private String phoneNumber;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    private void onRegisterClick() {
        if (mValidator.isInputEditTextFilled(etName, nameInputLayout, "Name is Required")
                && mValidator.isInputEditTextFilled(etPassword, passwordInputLayout, "Password is Required")
                && mValidator.isInputEditTextFilled(etAddress, addressInputLayout, "Address is Required")) {

//            if (imagePath != null) {
//                File imageFile = new File(imagePath);
//                if (imageFile.isFile()) {
//                    RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("*/*"));
//                    partFileToUpload = MultipartBody.Part.createFormData("Image", imageFile.getName(), requestBody);
//                }
//            } else {
//                RequestBody requestBody = RequestBody.create("", MediaType.parse("*/*"));
//                partFileToUpload = MultipartBody.Part.createFormData("Image", "", requestBody);
//            }


            if (SharedPreferenceHelper.getHelper().getPhoneNumber() != null) {
                phoneNumber = SharedPreferenceHelper.getHelper().getPhoneNumber();

                Map<String, Object> newUserMap = new HashMap<>();

                ////creation of firebase

                if(imagePath!=null){
                    File imageFile = new File(imagePath);
                    Uri uri = Uri.fromFile(imageFile);
                    StorageReference imageRef =  firebaseStorage.getReference().child("profileImages/"+uri.getLastPathSegment());
                    UploadTask uploadTask = imageRef.putFile(uri);
                    uploadTask.addOnFailureListener(exception -> {

                    }).addOnSuccessListener(taskSnapshot -> {

                    });
                }
            }


//            MediaType mediaType = MediaType.get("multipart/form-data");
//            RequestBody requestBodyName = RequestBody.create(etName.getText().toString().trim(), mediaType);
//            RequestBody requestBodyPhone = RequestBody.create(phoneNumber, mediaType);
//            RequestBody requestBodyAddress = RequestBody.create(etAddress.getText().toString().trim(), mediaType);
//            RequestBody requestBodyPassword = RequestBody.create(etPassword.getText().toString().trim(), mediaType);
//
//            registerUser(requestBodyName, requestBodyPhone, requestBodyPassword, requestBodyAddress);

        } else {
            Toast.makeText(mActivity, "All fields must be filled!", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUserFirebase(){


    }

        ///Api use
    private void registerUserWithApi(RequestBody name, RequestBody phone, RequestBody password, RequestBody address) {

        UserServices userServices = RetrofitHelper.getInstance().getUserClient();
        userServices.registerNewUser(partFileToUpload, name, phone, password, address)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
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
                    public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });


    }


}
