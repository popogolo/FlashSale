package com.flashsale.controller;

import com.flashsale.Response.CommonReturnType;
import com.flashsale.error.BusinessException;
import com.flashsale.error.EmBusinessError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object  handlerException(HttpServletRequest request, Exception e){
        CommonReturnType commonReturnType=new CommonReturnType();
        Map<String,Object> responseData=new HashMap<>();
        if(e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            responseData.put("ErrorCode", businessException.getErrorCode());
            responseData.put("ErrorMessage", businessException.getErrorMessage());
        }
        else{
            responseData.put("ErrorCode", EmBusinessError.UNKOWN_EXCEPTION.getErrorCode());
            responseData.put("ErrorMessage",EmBusinessError.UNKOWN_EXCEPTION.getErrorMessage());
        }
        commonReturnType.setStatus("fail");
        commonReturnType.setData(responseData);

        return commonReturnType;
    }

}
