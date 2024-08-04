package com.leadn.contractolabor.ui.credentials.model;

public class UserModel {

    private String seqId, name, phone, imageUrl, address, message;
    private Boolean isActive;

    public UserModel(){

    }
    public UserModel(String seqId, String name, String phone, String imageUrl, String address, String message, Boolean isActive){
        this.seqId = seqId;
        this.name = name;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.address = address;
        this. message = message;
        this.isActive = isActive;
    }

    public String getSeqId() {
        return seqId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getIsActive(){
        return isActive;
    }

    public String getMessage() {
        return message;
    }

}
