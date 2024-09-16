package com.example.center.common;

/**
 * 错误
 */
public enum ErrorCode {
    SUCCESS(0,"ok",""),
    PARAM_ERROR(4000,"请求参数错误",""),
    NULL_ERROR(4001,"请求数据为空",""),
    NOT_LOGIN(4010,"未登录",""),
    NO_AUTH(4011,"没有权限",""),
    SYSTEM_ERROR(5000,"系统内部错误","");

    private  final int  Code;
    private final String message;
    private final String description;


    ErrorCode (int code,String message ,String description)
    {
        this.Code=code;
        this.message=message;
        this.description=description;
    }

   public int getCode(){
        return Code;
   }

   public String getMessage()
   {
       return message;
   }
   public  String getDescription()
   {
       return description;
   }



}
