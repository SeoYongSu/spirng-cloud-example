package com.example.userservice.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ApiResult {
    /**
     * 성공 여부
     */
    private boolean success;

    /**
     * 에러 타입
     */
    private String errorType;

    /**
     * 메시지
     */
    private String message;

    /**
     * 추가 변수 1
     */
    private String val1;

    /**
     * 추가 변수 2
     */
    private String val2;


    public ApiResult (boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }

    public ApiResult (boolean success, String message, String val1)
    {
        this.success = success;
        this.message = message;
        this.val1 = val1;
    }

    public ApiResult (boolean success, String message, String val1, String val2)
    {
        this.success = success;
        this.message = message;
        this.val1 = val1;
        this.val2 = val2;
    }

    public ApiResult (boolean success, String errorType, String message, String val1, String val2)
    {
        this.success = success;
        this.errorType = errorType;
        this.message = message;
        this.val1 = val1;
        this.val2 = val2;
    }
}
