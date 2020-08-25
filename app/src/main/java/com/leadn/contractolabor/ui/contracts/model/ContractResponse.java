package com.leadn.contractolabor.ui.contracts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContractResponse {
    @SerializedName("contracts")
    @Expose
    private List<Contract> contracts = null;

    public List<Contract> getContracts() {
        return contracts;
    }

    public static class Contract {
        @SerializedName("seq_id")
        @Expose
        private String seqId;
        @SerializedName("contractName")
        @Expose
        private String contractName;
        @SerializedName("sqrFeetPrice")
        @Expose
        private String sqrFeetPrice;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("startingDate")
        @Expose
        private String startingDate;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("ownerId")
        @Expose
        private String ownerId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("workers")
        @Expose
        private List<Worker> workers = null;

        public Contract(String seqId, String contractName) {
            this.seqId = seqId;
            this.contractName = contractName;
        }

        public String getSeqId() {
            return seqId;
        }

        public String getContractName() {
            return contractName;
        }

        public String getSqrFeetPrice() {
            return sqrFeetPrice;
        }

        public String getLocation() {
            return location;
        }

        public String getStartingDate() {
            return startingDate;
        }

        public String getStatus() {
            return status;
        }

        public String getImage() {
            return image;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public String getUserId() {
            return userId;
        }

        public List<Worker> getWorkers() {
            return workers;
        }
    }

    public class Worker {

        @SerializedName("seq_id")
        @Expose
        private String seqId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("daily_wage")
        @Expose
        private String dailyWage;
        @SerializedName("isActive")
        @Expose
        private String isActive;
        @SerializedName("isFree")
        @Expose
        private String isFree;

        public String getSeqId() {
            return seqId;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public String getDailyWage() {
            return dailyWage;
        }

        public String getIsActive() {
            return isActive;
        }

        public String getIsFree() {
            return isFree;
        }
    }

}
