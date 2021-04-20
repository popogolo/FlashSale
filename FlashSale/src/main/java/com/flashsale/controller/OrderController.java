package com.flashsale.controller;


import com.flashsale.Response.CommonReturnType;
import com.flashsale.error.BusinessException;
import com.flashsale.error.EmBusinessError;
import com.flashsale.service.OrderService;
import com.flashsale.service.model.OrderModel;
import com.flashsale.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.color.CMMException;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController extends  BaseController{
    @Autowired
    OrderService orderService;
    @Autowired
    HttpServletRequest httpServletRequest;
    @RequestMapping(value="/createOrder",method={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId") Integer itemid,
                                        @RequestParam(name="amount") Integer amount,
                                        @RequestParam(name="promoid",required = false) Integer promoid) throws BusinessException {
        Boolean islogin=(Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(islogin ==null||!islogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);

        }
        UserModel userModel=(UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
       OrderModel orderModel= orderService.createOrder(userModel.getId(),itemid,amount,promoid);
       return CommonReturnType.create(null);
    }
}
