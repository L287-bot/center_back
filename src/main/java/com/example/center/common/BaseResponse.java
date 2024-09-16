package com.example.center.common;


import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 */
@Data
public class BaseResponse<T> implements Serializable {
    /**
     * 状态码
     */
    private  int Code;
    /**
     * 数据
     */
    private T data;
    /**
     * 消息
     */
    private String message;
    /**
     * 描述
     */
    private  String description;
//    构造函数重载
    public  BaseResponse(int code,T data,String message,String description)
    {
        this.Code=code;
        this.data=data;
        this.message=message;
        this.description=description;
    }
    //this 调用这个方法 public  BaseResponse(int code,T data,String message,String description)
    public BaseResponse(int code,T data,String message)
    {
        this(code,data,message,"");
    }
    public BaseResponse(int code,T data)
    {
        this(code,data,"","");
    }
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

}
