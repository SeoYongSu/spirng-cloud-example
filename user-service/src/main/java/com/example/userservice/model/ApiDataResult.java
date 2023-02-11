package com.example.userservice.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ApiDataResult<T> extends ApiResult {
    /**
     * 데이터
     */
    private T data;

    public ApiDataResult (boolean success, String message, T data)
    {
        super (success, message);
        this.data = data;

    }
}
