package com.leadn.contractolabor.ui.credentials;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.MainActivity;
import com.leadn.contractolabor.ui.credentials.model.UserResponse;
import com.leadn.contractolabor.utils.InputValidator;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.shared_preferences.SharedPreferenceHelper;
import com.leadn.contractolabor.utils.web_apis.UserServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordFragment extends Fragment {

    public PasswordFragment() {

    }

    private View view;
    private AppCompatActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_password, container, false);
        initViews();
        return view;
    }

    private EditText etPassword;
    private TextInputLayout textInputLayoutPassword;
    private InputValidator mInputValidator;

    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        if (mActivity != null && mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().setTitle("Password Section");
        }
        mInputValidator = new InputValidator(mActivity);

        etPassword = view.findViewById(R.id.et_password);
        textInputLayoutPassword = view.findViewById(R.id.text_input_layout_password);
        Button btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(view -> {
            login();
        });

        TextView txtUserPhone = view.findViewById(R.id.txt_user_phone);
        if (SharedPreferenceHelper.getHelper().getPhoneNumber() != null)
            txtUserPhone.setText(SharedPreferenceHelper.getHelper().getPhoneNumber());

    }

    private void login() {
        if (mInputValidator.isInputEditTextFilled(etPassword, textInputLayoutPassword, "Password required!")) {
            String password = etPassword.getText().toString().trim();
            String phone = "";
            if (SharedPreferenceHelper.getHelper().getPhoneNumber() != null)
                phone = SharedPreferenceHelper.getHelper().getPhoneNumber();


            UserServices userServices = RetrofitHelper.getInstance().getUserClient();
            userServices.getCurrentUser(phone, password)
                    .enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            if (response.isSuccessful()) {
                                UserResponse userResponse = response.body();
                                if (userResponse != null) {
                                    if (userResponse.getStatus()) {

                                        SharedPreferenceHelper.getHelper().setUserLoggedInData(new Gson().toJson(userResponse));
                                        mActivity.startActivity(new Intent(mActivity, MainActivity.class));
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

        } else {
            textInputLayoutPassword.setError("Enter password");
        }
    }
}
