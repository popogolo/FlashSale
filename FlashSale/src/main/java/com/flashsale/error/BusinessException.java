package com.flashsale.error;

public class BusinessException extends Exception implements CommonError {
    private CommonError commonError;
    public BusinessException(CommonError commonError){
        super();
        this.commonError=commonError;
    }
    public BusinessException(CommonError commonError,String ErrorMsg){
        super();
        this.commonError=commonError.setErrorMsg(ErrorMsg);
    }
    @Override
    public int getErrorCode() {
        return commonError.getErrorCode();
    }

    @Override
    public String getErrorMessage() {
        return commonError.getErrorMessage();
    }

    @Override
    public CommonError setErrorMsg(String ErrorMsg) {
        return commonError.setErrorMsg(ErrorMsg);
    }
}
