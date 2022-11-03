package edu.ruc.liu.dto;

import lombok.Data;

@Data
public class Response<T> {
    private String code;
    private String message;
    private T data;
    public static <T> Response<T> success(T data){
        Response<T> tResponse = new Response<>();
        tResponse.setCode("200");
        tResponse.setData(data);
        return tResponse;
    }
}
