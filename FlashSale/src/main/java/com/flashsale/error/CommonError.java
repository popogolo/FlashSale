package com.flashsale.error;

public interface CommonError {
    int getErrorCode();
    String getErrorMessage();
    CommonError setErrorMsg(String ErrorMsg);
}
