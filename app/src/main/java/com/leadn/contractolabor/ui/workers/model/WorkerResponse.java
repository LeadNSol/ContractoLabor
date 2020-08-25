package com.leadn.contractolabor.ui.workers.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkerResponse {

    @SerializedName("Workers")
    @Expose
    private List<Worker> workers = null;

    public List<Worker> getWorkersList() {
        return workers;
    }

    public static class Worker {
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
        @SerializedName("days_of_work")
        @Expose
        private String daysOfWork;
        @SerializedName("days_of_absence")
        @Expose
        private String daysOfAbsence;
        @SerializedName("total_wage")
        @Expose
        private String totalWage;
        @SerializedName("remaining_wage")
        @Expose
        private String remainingWage;
        @SerializedName("isActive")
        @Expose
        private String isActive;
        @SerializedName("isFree")
        @Expose
        private String isFree;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("created_by")
        @Expose
        private String createdBy;
        @SerializedName("contract_id")
        @Expose
        private String contractId;

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

        public String getDaysOfWork() {
            return daysOfWork;
        }

        public String getDaysOfAbsence() {
            return daysOfAbsence;
        }

        public String getContractId() {
            return contractId;
        }

        public String getTotalWage() {
            return totalWage;
        }

        public String getRemainingWage() {
            return remainingWage;
        }

        public String getIsActive() {
            return isActive;
        }

        public String getIsFree() {
            return isFree;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        @Override
        public String toString() {
            return "Worker{" +
                    "seqId='" + seqId + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", phone='" + phone + '\'' +
                    ", address='" + address + '\'' +
                    ", dailyWage='" + dailyWage + '\'' +
                    ", daysOfWork='" + daysOfWork + '\'' +
                    ", daysOfAbsence='" + daysOfAbsence + '\'' +
                    ", totalWage='" + totalWage + '\'' +
                    ", remainingWage='" + remainingWage + '\'' +
                    ", isActive='" + isActive + '\'' +
                    ", isFree='" + isFree + '\'' +
                    ", paymentStatus='" + paymentStatus + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", createdDate='" + createdDate + '\'' +
                    ", createdBy='" + createdBy + '\'' +
                    ", contractId='" + contractId + '\'' +
                    '}';
        }
    }
}
