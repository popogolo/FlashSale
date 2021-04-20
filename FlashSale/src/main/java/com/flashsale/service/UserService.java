package com.flashsale.service;

import com.flashsale.error.BusinessException;
import com.flashsale.service.model.UserModel;

public interface UserService {
     UserModel getUserByID(Integer id);
     void register(UserModel userModel) throws BusinessException;
     /*
     telephone 是用户传入的手机号，password是用户加密后的代码

      */
     UserModel validateLogin(String telephone,String password) throws BusinessException;
}
