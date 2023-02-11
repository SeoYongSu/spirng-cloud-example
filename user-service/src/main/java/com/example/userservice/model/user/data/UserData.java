package com.example.userservice.model.user.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
@Data
public class UserData {
    private String userID;
    private String email;
    private String mobile;
    private String name;
    private String providerType;

    @Builder
    public UserData(String userID, String email, String mobile, String name, String providerType){
        this.userID=userID;
        this.email = email;
        this.mobile = mobile;
        this.name = name;
        this.providerType = providerType;
    }

}
