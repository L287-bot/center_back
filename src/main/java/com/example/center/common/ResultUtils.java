package com.example.center.common;

/**
 * 返回工具类 返回通用返回类BaseResponse
 *
 */
public class ResultUtils {

    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T>success(T data)
    {
        return new BaseResponse<>(0,data,"ok");
    }

    /**
     * 失败
     * public BaseResponse(ErrorCode errorCode)
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode)
    {
        return new BaseResponse(errorCode);
    }

    /**
     *  总体还是用的 public  BaseResponse(int code,T data,String message,String description)方法
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static  BaseResponse error(int code,String message,String description)
    {
        return  new BaseResponse(code,null,message,description);
    }

    public static  BaseResponse error(ErrorCode errorCode,String message,String description)
    {
        return new BaseResponse(errorCode.getCode(),null,message,description);
    }

    public static BaseResponse error(ErrorCode errorCode,String description)
    {
        return new BaseResponse(errorCode.getCode(),null,errorCode.getMessage(),description);
    }






}
