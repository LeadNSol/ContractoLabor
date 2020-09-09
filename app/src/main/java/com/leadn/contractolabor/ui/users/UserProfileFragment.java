package com.leadn.contractolabor.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.credentials.LoginActivity;
import com.leadn.contractolabor.ui.users.model.UserProfileResponse;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.UtilClass;
import com.leadn.contractolabor.utils.shared_preferences.SharedPreferenceHelper;
import com.leadn.contractolabor.utils.web_apis.UserServices;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProfileFragment extends Fragment {

    public UserProfileFragment() {
    }

    private View view;
    private AppCompatActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        initViews();
        return view;
    }

    private UserServices mUserServices;
    private CircleImageView imgUser;
    private TextView txtName, txtPhone, txtAddress, txtLogout;
    private TextView txtTotalWorkers, txtTotalContracts, txtTotalBudget, txtTotalExpenses;

    private void initViews() {
        mActivity = (AppCompatActivity) getActivity();
        if (mActivity != null && mActivity.getSupportActionBar() != null)
            mActivity.getSupportActionBar().setTitle("User Profile");
        mUserServices = RetrofitHelper.getInstance().getUserClient();

        txtName = view.findViewById(R.id.txt_user_name);
        txtPhone = view.findViewById(R.id.txt_user_phone);
        txtAddress = view.findViewById(R.id.txt_user_address);

        txtTotalBudget = view.findViewById(R.id.txt_budgets);
        txtTotalContracts = view.findViewById(R.id.txt_contracts);
        txtTotalExpenses = view.findViewById(R.id.txt_expenses);
        txtTotalWorkers = view.findViewById(R.id.txt_workers);

        txtLogout = view.findViewById(R.id.txt_lag_out);
        txtLogout.setOnClickListener(view -> {
            logout();
        });


        loadProfileData();
    }

    private void logout() {
        SharedPreferenceHelper.getHelper().clearPreference();
        AuthUI.getInstance()
                .signOut(mActivity)
                .addOnCompleteListener(task -> {
                    mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                });
    }

    private List<UserProfileResponse.UserProfile> userList;

    private void loadProfileData() {
        userList = new ArrayList<>();
        mUserServices.getUserById(UtilClass.getCurrentUserId())
                .enqueue(new Callback<UserProfileResponse>() {
                    @Override
                    public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                        if (response.isSuccessful()) {
                            UserProfileResponse userProfileResponse = response.body();
                            if (userProfileResponse != null) {
                                userList = userProfileResponse.getUsers();
                                if (userList.size() > 0) {
                                    populateUserData();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfileResponse> call, Throwable t) {

                    }
                });
    }


    private void populateUserData() {
        if (userList.size() > 0) {
            UserProfileResponse.UserProfile user = userList.get(0);
            txtAddress.setText(user.getAddress());
            txtPhone.setText(user.getPhone());
            txtName.setText(user.getName());
            if (user.getContracts() != null)
                UtilClass.startCountAnimation(0, Integer.parseInt(user.getContracts()), txtTotalContracts);
            else
                UtilClass.startCountAnimation(0, 0, txtTotalContracts);

            if (user.getTotalBudget() != null)
                UtilClass.startCountAnimation(0, Integer.parseInt(user.getTotalBudget()), txtTotalBudget);
            else
                UtilClass.startCountAnimation(0, 0, txtTotalBudget);

            if (user.getTotalExpenses() != null)
                UtilClass.startCountAnimation(0, Float.parseFloat(user.getTotalExpenses()), txtTotalExpenses);
            else
                UtilClass.startCountAnimation(0, 0, txtTotalExpenses);

            if (user.getWorkers() != null)
                UtilClass.startCountAnimation(0, Integer.parseInt(user.getWorkers()), txtTotalWorkers);
            else
                UtilClass.startCountAnimation(0, 0, txtTotalWorkers);


        } else {
            Toast.makeText(mActivity, "No data found!", Toast.LENGTH_SHORT).show();
        }
    }


}