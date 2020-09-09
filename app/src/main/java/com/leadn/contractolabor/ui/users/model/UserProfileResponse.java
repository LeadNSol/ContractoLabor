package com.leadn.contractolabor.ui.users.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileResponse {

   public static String name;

    @SerializedName("Users")
    @Expose
    private List<UserProfile> users = null;

    public List<UserProfile> getUsers() {
        return users;
    }

    public static class UserProfile {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("workers")
        @Expose
        private String workers;
        @SerializedName("totalBudget")
        @Expose
        private String totalBudget;
        @SerializedName("contracts")
        @Expose
        private String contracts;
        @SerializedName("totalExpenses")
        @Expose
        private String totalExpenses;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getPassword() {
            return password;
        }

        public String getAddress() {
            return address;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getWorkers() {
            return workers;
        }

        public String getTotalBudget() {
            return totalBudget;
        }

        public String getContracts() {
            return contracts;
        }

        public String getTotalExpenses() {
            return totalExpenses;
        }
    }
}
