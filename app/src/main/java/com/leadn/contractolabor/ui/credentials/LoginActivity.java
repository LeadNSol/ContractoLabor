package com.leadn.contractolabor.ui.credentials;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.MainActivity;
import com.leadn.contractolabor.ui.credentials.model.UserModel;
import com.leadn.contractolabor.utils.AppConstant;
import com.leadn.contractolabor.utils.UtilClass;
import com.leadn.contractolabor.utils.shared_preferences.SharedPreferenceHelper;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 10001;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null)
            createSignInIntent();
        else
            navigateFragment(mAuth.getCurrentUser().getPhoneNumber());

    }

    public void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = List.of(new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_create_intent]
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );


    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                SharedPreferenceHelper.getHelper().setPhoneNumber(user.getPhoneNumber());
                navigateFragment(user.getPhoneNumber());
            }
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...

            if (response != null) {
                Toast.makeText(this, Objects.requireNonNull(response.getError()).getErrorCode() + "",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void navigateFragment(String phone) {
        if (phone != null) {

            SharedPreferenceHelper.getHelper().clearPreference();
            SharedPreferenceHelper.getHelper().setPhoneNumber(phone);

           /* UserServices userServices = RetrofitHelper.getInstance().getUserClient();
            userServices.checkUserExist(phone).enqueue(new Callback<StatusResponse>() {
                @Override
                public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                    if (response.isSuccessful() && response.code() != 500) {
                        StatusResponse statusResponse = response.body();
                        if (statusResponse != null && statusResponse.getStatus()) {
                            UtilClass.pushFragment(new PasswordFragment(), LoginActivity.this, R.id.login_frame_layout, false);
                        } else
                            UtilClass.pushFragment(new RegistrationFragment(), LoginActivity.this, R.id.login_frame_layout, false);

                    }
                }

                @Override
                public void onFailure(Call<StatusResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });*/


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child(AppConstant.USERS).child(phone);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //Store store = dataSnapshot.getValue(Store.class);
                        UserModel user = dataSnapshot.getValue(UserModel.class);
                        Gson gson = new Gson();
                        SharedPreferenceHelper.getHelper().setUserLoggedInData(gson.toJson(user));
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        UtilClass.pushFragment(new RegistrationFragment(), LoginActivity.this, R.id.login_frame_layout, true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled: ", databaseError.toException());
                }
            });
        }
    }

}