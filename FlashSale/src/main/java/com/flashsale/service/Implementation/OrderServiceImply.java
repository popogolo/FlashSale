package com.flashsale.service.Implementation;

import com.flashsale.DAO.ItemStockDOMapper;
import com.flashsale.DAO.OrderDOMapper;
import com.flashsale.DAO.SequenceDOMapper;
import com.flashsale.DataObject.ItemDO;
import com.flashsale.DataObject.OrderDO;
import com.flashsale.DataObject.SequenceDO;
import com.flashsale.error.BusinessException;
import com.flashsale.error.EmBusinessError;
import com.flashsale.service.ItemService;
import com.flashsale.service.OrderService;
import com.flashsale.service.UserService;
import com.flashsale.service.model.ItemModel;
import com.flashsale.service.model.OrderModel;
import com.flashsale.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImply implements OrderService {

    @Autowired
    private SequenceDOMapper sequenceDOMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount,Integer promoId) throws BusinessException {
        //1.校验下单状态，下单的商品是否存在，下单的用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItem(itemId);
        UserModel userModel = userService.getUserByID(userId);
        if (itemModel == null) throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品不存在");
        if (userModel == null) throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        if (amount <= 0 || amount > itemModel.getStock())
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "购买数量不合法");
        if(promoId !=null){
            if(promoId.intValue()!=itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
            }
            if(itemModel.getPromoModel().getStatus()!=2){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动已结束");
            }
        }
        //2.落单减库存，支付减库存
        boolean result=itemService.decreaseItem(itemId,amount);
        if(!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //3.订单入库
        OrderModel orderModel=new OrderModel();
        orderModel.setId(generateOrderNo());
        orderModel.setPromoId(promoId);
        orderModel.setAmount(amount);
        orderModel.setItemid(itemId);
        if(promoId!=null){
            orderModel.setItemprice(itemModel.getPromoModel().getPromoitemprice());
        }
        else{
        orderModel.setItemprice(itemModel.getPrice());
        }
        orderModel.setUserid(userId);
        orderModel.setOrderprice(orderModel.getItemprice().multiply(new BigDecimal(amount)));
        OrderDO orderDO=convertDataObjectFromModel(orderModel);
        orderDOMapper.insertSelective(orderDO);
        itemService.increaseSales(itemId,amount);
        //4.返回前端

        return orderModel;
    }
    private OrderDO convertDataObjectFromModel(OrderModel orderModel){
        OrderDO orderDO=new  OrderDO();
        if(orderModel==null) return null;
        BeanUtils.copyProperties(orderModel,orderDO);
        return orderDO;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generateOrderNo(){
        //订单号为16位
        StringBuilder orderNum=new StringBuilder();
        //前8位时间信息
        LocalDateTime now=LocalDateTime.now();

        orderNum.append(now.format(DateTimeFormatter.ISO_DATE).replace("-",""));
        //中间6位为自增数据
        //获取当前的sequence
        int sequence=0;
        SequenceDO sequenceDO=sequenceDOMapper.selectByPrimaryKey("ordersequence");
        sequence=sequenceDO.getCurrenetvalue();
        sequenceDO.setCurrenetvalue(sequenceDO.getCurrenetvalue()+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr=String.valueOf(sequence);
        for(int i=0;i<6-sequenceStr.length();i++){
           orderNum.append("0");
        }
        orderNum.append(sequenceStr);


        //最后两位为分库分表位,暂时写死
        orderNum.append(00);
        return orderNum.toString();
    }


}
