package com.leadn.contractolabor.utils.web_apis;

import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.ui.workers.model.AttendanceResponse;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WorkerServices {

    @GET("workers/getWorkers.php")
    Call<WorkerResponse> getWorkers();

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
}
