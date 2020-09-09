package com.leadn.contractolabor.utils.web_apis;

import com.leadn.contractolabor.common_model.StatusResponse;
import com.leadn.contractolabor.ui.contracts.expenses.model.ExpenseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ExpensesServices {


    @FormUrlEncoded
    @POST("expenses/postExpenses.php")
    Call<StatusResponse> postExpenses(@Field("name") String name,
                                      @Field("expendedAmount") String expendedAmount,
                                      @Field("date") String date,
                                      @Field("paymentStatus") String paymentStatus,
                                      @Field("contractId") String contractId,
                                      @Field("userId") String userId);

    @FormUrlEncoded
    @POST("expenses/getContractExpenses.php")
    Call<ExpenseResponse> getContractExpenses(@Field("contractId") String contractId);
}
