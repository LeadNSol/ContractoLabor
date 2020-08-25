package com.leadn.contractolabor.utils.Retrofit;


import com.leadn.contractolabor.utils.web_apis.ContractServices;
import com.leadn.contractolabor.utils.web_apis.ExpensesServices;
import com.leadn.contractolabor.utils.web_apis.UserServices;
import com.leadn.contractolabor.utils.web_apis.WorkerServices;

public class RetrofitHelper implements IRetrofitHelper {
    private static RetrofitHelper mHelper;

    public static RetrofitHelper getInstance() {
        if (mHelper == null)
            mHelper = new RetrofitHelper();
        return mHelper;
    }

    @Override
    public UserServices getUserClient() {
        return RetrofitClient.getClient().create(UserServices.class);
    }

    @Override
    public ContractServices getContractClient() {
        return RetrofitClient.getClient().create(ContractServices.class);
    }

    @Override
    public WorkerServices getWorkersClient() {
        return RetrofitClient.getClient().create(WorkerServices.class);
    }

    @Override
    public ExpensesServices getExpensesClient() {
        return RetrofitClient.getClient().create(ExpensesServices.class);
    }

}
