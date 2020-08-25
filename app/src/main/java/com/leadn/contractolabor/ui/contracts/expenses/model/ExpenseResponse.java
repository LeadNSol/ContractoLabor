package com.leadn.contractolabor.ui.contracts.expenses.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExpenseResponse {
    @SerializedName("Expenses")
    @Expose
    private List<Expense> expenses = null;

    public List<Expense> getExpenses() {
        return expenses;
    }

    public static class Expense {
        @SerializedName("seq_id")
        @Expose
        private String seqId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("expended_amount")
        @Expose
        private String expendedAmount;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("contract_id")
        @Expose
        private String contractId;
        @SerializedName("user_id")
        @Expose
        private String userId;

        public String getSeqId() {
            return seqId;
        }

        public String getName() {
            return name;
        }

        public String getExpendedAmount() {
            return expendedAmount;
        }

        public String getDate() {
            return date;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public String getContractId() {
            return contractId;
        }

        public String getUserId() {
            return userId;
        }
    }
}
