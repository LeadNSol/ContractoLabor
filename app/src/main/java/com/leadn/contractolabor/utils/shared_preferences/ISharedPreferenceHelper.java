package com.leadn.contractolabor.utils.shared_preferences;

public interface ISharedPreferenceHelper {

    void setPhoneNumber(String phoneNumber);

    String getPhoneNumber();


    void clearPreference();

    void setUserLoggedInData(String userLoggedInData);

    String getUserLoggedInData();


//    void setUserDetails(String users);
//
//    String getUserDetails();

}
