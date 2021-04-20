package com.flashsale.error;

public enum EmBusinessError implements CommonError {
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKOWN_EXCEPTION(10001,"未知错误"),
    USER_NOT_EXIST(20001,"用户不存在"),
    STOCK_NOT_ENOUGH(3001,"库存不足"),
    USER_NOT_LOGIN(2003,"用户还未登录"),
    USER_LOGIN_ERROR(2002,"手机号未注册或密码不正确");
    private int ErrorCode;
    private String ErrorMsg;
    private  EmBusinessError(int ErrorCode,String ErrorMsg){
        this.ErrorCode=ErrorCode;
        this.ErrorMsg=ErrorMsg;
    }
    @Override
    public int getErrorCode() {
        return ErrorCode;
    }

    @Override
    public String getErrorMessage() {
        return ErrorMsg;
    }

    @Override
    public CommonError setErrorMsg(String ErrorMsg) {
        this.ErrorMsg=ErrorMsg;
        return this;
    }
}
