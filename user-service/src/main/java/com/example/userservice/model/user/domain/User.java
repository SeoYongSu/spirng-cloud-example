package com.example.userservice.model.user.domain;

import com.example.userservice.model.user.data.UserData;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Spring Security관련 Document
 */
@Getter
@Document
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userID;

    @Indexed(unique = true)
    private String email;

    private String mobile;

    private String password;

    private String name;

    private String providerType;


    @Builder
    public User(String userID, String email, String mobile, String password, String name, String providerType){
        this.userID = userID;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.name = name;
        this.providerType = providerType;
    }


    public UserData toData(){
        return UserData.builder()
                .userID(this.userID)
                .email(this.email)
                .mobile(this.mobile)
                .name(this.name)
                .providerType(this.providerType)
                .build();
    }

}
