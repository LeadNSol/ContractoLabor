package com.leadn.contractolabor.ui.workers.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceResponse {
    @SerializedName("WorkerAttendance")
    @Expose
    private List<Attendance> workerAttendance = null;

    public List<Attendance> getWorkerAttendance() {
        return workerAttendance;
    }

    public static class Attendance {
        @SerializedName("seqId")
        @Expose
        private String seqId;
        @SerializedName("present")
        @Expose
        private String present;
        @SerializedName("absent")
        @Expose
        private String absent;
        @SerializedName("date")
        @Expose
        private String createdDate;
        @SerializedName("contractName")
        @Expose
        private String contractName;


        public String getSeqId() {
            return seqId;
        }

        public String getPresent() {
            return present;
        }

        public String getAbsent() {
            return absent;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public String getContractName() {
            return contractName;
        }
    }
}
