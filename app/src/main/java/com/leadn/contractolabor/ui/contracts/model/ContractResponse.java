package com.leadn.contractolabor.ui.contracts.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.leadn.contractolabor.ui.workers.model.WorkerResponse;

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
        @SerializedName("totalArea")
        @Expose
        private String totalArea;
        @SerializedName("totalBudget")
        @Expose
        private String totalBudget;
        @SerializedName("expenses")
        @Expose
        private String expenses;
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
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("workers")
        @Expose
        private List<WorkerResponse.Worker> workers = null;
        @SerializedName("owner")
        @Expose
        private List<OwnerResponse.ContractOwner> owner = null;

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

        public String getTotalArea() {
            return totalArea;
        }

        public String getTotalBudget() {
            return totalBudget;
        }

        public String getExpenses() {
            return expenses;
        }

        public List<OwnerResponse.ContractOwner> getOwner() {
            return owner;
        }

        public String getUserId() {
            return userId;
        }

        public List<WorkerResponse.Worker> getWorkers() {
            return workers;
        }
    }


}
