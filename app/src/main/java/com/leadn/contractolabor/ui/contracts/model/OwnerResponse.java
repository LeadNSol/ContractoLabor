package com.leadn.contractolabor.ui.contracts.model;

import android.telecom.Call;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.security.acl.Owner;
import java.util.List;

public class OwnerResponse {
    @SerializedName("contract_owner")
    @Expose
    List<ContractOwner> contractOwnerList;

    public List<ContractOwner> getContractOwnerList() {
        return contractOwnerList;
    }

    public static class ContractOwner{
        @SerializedName("seq_id")
        @Expose
        private String seqId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("address")
        @Expose
        private String address;

        public String getSeqId() {
            return seqId;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }
    }
}
