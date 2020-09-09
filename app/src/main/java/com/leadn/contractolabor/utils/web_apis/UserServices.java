package com.leadn.contractolabor.utils.web_apis;

import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.ui.credentials.model.UserResponse;
import com.leadn.contractolabor.ui.users.model.UserProfileResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserServices {

    @FormUrlEncoded
    @POST("user/getCurrentUserByPhone.php")
    Call<UserResponse> getCurrentUser(@Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/checkUser.php")
    Call<StatusResponse> checkUserExist(@Field("phone") String phone);

    @Multipart
    @POST("user/registerNewUser.php")
    Call<UserResponse> registerNewUser(@Part MultipartBody.Part imageFile,
                                       @Part("name") RequestBody name,
                                       @Part("phone") RequestBody phone,
                                       @Part("password") RequestBody password,
                                       @Part("address") RequestBody address);

    @FormUrlEncoded
    @POST("user/getUserById.php")
    Call<UserProfileResponse> getUserById(@Field("userId") String userId);


    @Multipart
    @POST("user/updateUser.php")
    Call<UserResponse> updateUser(@Part MultipartBody.Part imageFile,
                                  @Part("name") RequestBody name,
                                  @Part("address") RequestBody address,
                                  @Part("userId") RequestBody userId);


}
