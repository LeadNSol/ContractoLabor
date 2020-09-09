package com.leadn.contractolabor.utils.web_apis;

import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.ui.contracts.model.ContractResponse;
import com.leadn.contractolabor.ui.contracts.model.OwnerResponse;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ContractServices {

    @FormUrlEncoded
    @POST("contracts/getContracts.php")
    Call<ContractResponse> getContracts(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("contracts/getContracts.php")
    Call<ContractResponse> getContractsById(@Field("seqId") String contractId);

    @FormUrlEncoded
    @POST("workers/getContractWorkers.php")
    Call<WorkerResponse> getContractWorkers(@Field("contractId") String contractId);

    @FormUrlEncoded
    @POST("workers/getFreeWorkers.php")
    Call<WorkerResponse> getFreeWorkers(@Field("userId") String userId);

    @Multipart
    @POST("contracts/postContract.php")
    Call<StatusResponse> postContract(@Part MultipartBody.Part imageFile,
                                      @Part("name") RequestBody name,
                                      @Part("sq_feet_price") RequestBody sqFeetPrice,
                                      @Part("location") RequestBody location,
                                      @Part("starting_date") RequestBody startDate,
                                      @Part("ownerId") RequestBody ownerId,
                                      @Part("userId") RequestBody userId);

    @GET("contracts/getOwners.php")
    Call<OwnerResponse> getOwners();


    @FormUrlEncoded
    @POST("contracts/postOwner.php")
    Call<StatusResponse> postOwner(@Field("name") String name, @Field("phone") String phone, @Field("address") String address);

    @POST("contracts/assignWorkersToContract.php")
    @Headers("Content-type: application/json")
    Call<StatusResponse> assignWorkers(@Body JSONObject jsonObject);

    @FormUrlEncoded
    @POST("contracts/postAttendance.php")
    Call<StatusResponse> postAttendance(@Field("present") int present,
                                        @Field("absent") int absent,
                                        @Field("daysOfWork") int daysOfWork,
                                        @Field("daysOfAbsence") int daysOfAbsence,
                                        @Field("contractId") String contractId,
                                        @Field("userId") String userId,
                                        @Field("workerId") String workerId);

    @FormUrlEncoded
    @POST("contracts/deleteContract.php")
    Call<StatusResponse> deleteContract(@Field("seqId") String contractId);
}
