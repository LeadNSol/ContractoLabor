package com.leadn.contractolabor.utils.Retrofit;


import com.leadn.contractolabor.utils.web_apis.ContractServices;
import com.leadn.contractolabor.utils.web_apis.ExpensesServices;
import com.leadn.contractolabor.utils.web_apis.UserServices;
import com.leadn.contractolabor.utils.web_apis.WorkerServices;

public interface IRetrofitHelper {

    UserServices getUserClient();

    ContractServices getContractClient();

    WorkerServices getWorkersClient();

    ExpensesServices getExpensesClient();

}
