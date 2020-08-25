package com.leadn.contractolabor.ui.credentials;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.utils.Retrofit.RetrofitHelper;
import com.leadn.contractolabor.utils.UtilClass;
import com.leadn.contractolabor.utils.shared_preferences.SharedPreferenceHelper;
import com.leadn.contractolabor.utils.web_apis.UserServices;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 10001;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null)
            createSignInIntent();
        else
            navigateFragment(mAuth.getCurrentUser().getPhoneNumber());*/
        navigateFragment("03025580842");

    }

    public void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.PhoneBuilder().build());
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
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
    }

    private void navigateFragment(String phone) {
        if (phone != null) {

            SharedPreferenceHelper.getHelper().clearPreference();
            SharedPreferenceHelper.getHelper().setPhoneNumber(phone);

            UserServices userServices = RetrofitHelper.getInstance().getUserClient();
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
            });


           /* DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child(AppConstant.STORES).child(user.getPhoneNumber());

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Store store = dataSnapshot.getValue(Store.class);
                        Gson gson = new Gson();
                        SharedPreferenceHelper.getHelper().setUserLoggedInData(gson.toJson(store));
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        UtilClass.pushFragment(new RegistrationFragment(), LoginActivity.this, R.id.login_frame_layout, true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/
        }
    }

}