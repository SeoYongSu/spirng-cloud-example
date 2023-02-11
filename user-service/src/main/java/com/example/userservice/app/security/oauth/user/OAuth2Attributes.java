package com.example.userservice.app.security.oauth.user;

import java.util.Map;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Data;

@Data
public class OAuth2Attributes {

	@Id
	private String id;
	
	private String email;
	
	private String mobile;
	
	private String name;
	
	private String image;

	private String providerType;

	private Map<String, Object> attributes;
	
	@Builder
	public OAuth2Attributes(String id, String email, String mobile, String name, String image, String providerType, Map<String,Object> attributes) {
		this.id = id;
		this.email = email;
		this.mobile = mobile;
		this.name = name;
		this.image = image;
		this.providerType = providerType;
		this.attributes = attributes;
	}
	
	
	
	public static OAuth2Attributes of(String providerType, Map<String, Object> attributes) {
		switch (providerType) {
	        case "KAKAO" : return ofKakao(attributes);
	        case "NAVER" : return ofNaver(attributes);
	//        case GOOGLE : return new GoogleOAuth2UserInfo(attributes);
	        default: throw new IllegalArgumentException("Invalid Provider Type.");
	    }
		
	}
	
	/**
	 * KAKAO UserInfo
	 */
	@SuppressWarnings("unchecked")
	private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
		// kakao는 kakao_account에 유저정보가 있다. (email)
		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuth2Attributes.builder()
        		.id(attributes.get("id").toString())
        		.email((String) kakaoAccount.get("email"))
        		.mobile(null)
        		.name((String) kakaoProfile.get("nickname"))
        		.image((String) kakaoProfile.get("thumbnail_image"))
        		.providerType("KAKAO")
        		.attributes(attributes)
                .build();
		
	}
	
	/**
	 * NAVER UserInfo
	 */
	@SuppressWarnings("unchecked")
	private static OAuth2Attributes ofNaver(Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		
		return OAuth2Attributes.builder()
        		.id((String) response.get("id"))
        		.email((String) response.get("email"))
        		.mobile((String) response.get("mobile"))
        		.name((String) response.get("name"))
        		.image((String) response.get("profile_image"))
        		.providerType("NAVER")
        		.attributes(attributes)
                .build();
		
	}
	
	
}
