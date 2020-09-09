package com.leadn.contractolabor.utils.web_apis;

import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.ui.workers.model.AttendanceResponse;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WorkerServices {
    @FormUrlEncoded
    @POST("workers/getWorkers.php")
    Call<WorkerResponse> getWorkers(@Field("userId") String userId);

    @Multipart
    @POST("workers/postWorker.php")
    Call<StatusResponse> postWorker(@Part MultipartBody.Part imageFile,
                                    @Part("name") RequestBody name,
                                    @Part("type") RequestBody type,
                                    @Part("phone") RequestBody phone,
                                    @Part("daily_wage") RequestBody dailyWage,
                                    @Part("address") RequestBody address,
                                    @Part("created_date") RequestBody createdDate,
                                    @Part("userId") RequestBody userId);

    @FormUrlEncoded
    @POST("workers/getWorkerAttendance.php")
    Call<AttendanceResponse> getWorkerAttendance(@Field("worker_id") String workerId);

    @FormUrlEncoded
    @POST("workers/shiftWorker.php")
    Call<StatusResponse> shiftWorker(@Field("fromContract") String fromContract,
                                     @Field("toContract") String toContract,
                                     @Field("workerId") String workerId,
                                     @Field("workingDays") String workingDays);
}
