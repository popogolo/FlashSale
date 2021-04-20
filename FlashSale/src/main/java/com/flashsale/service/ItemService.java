package com.flashsale.service;

import com.flashsale.error.BusinessException;
import com.flashsale.service.model.ItemModel;

import java.util.List;

public interface ItemService {
        //创建商品
        ItemModel createItem(ItemModel itemModel) throws BusinessException;
        //商品列表的浏览
         List<ItemModel> listItem();
         //商品详情浏览
         ItemModel getItem(Integer id);

         boolean decreaseItem(Integer id,Integer amount);

         void increaseSales(Integer itemId,Integer amount);
}
