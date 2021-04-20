package com.flashsale.service;

import com.flashsale.error.BusinessException;
import com.flashsale.service.model.OrderModel;

public interface OrderService {
    OrderModel createOrder(Integer userId,Integer itemId,Integer amount,Integer promoid) throws BusinessException;

}
