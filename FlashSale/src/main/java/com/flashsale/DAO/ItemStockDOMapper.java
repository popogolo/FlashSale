package com.flashsale.DAO;

import com.flashsale.DataObject.ItemStockDO;
import org.apache.ibatis.annotations.Param;

public interface ItemStockDOMapper {
    int deleteByPrimaryKey(Integer id);

     ItemStockDO selectByItemId(Integer itemid);

     Integer updateByItemid(@Param("itemid") Integer itemid, @Param("amount")Integer amount);

    int insert(ItemStockDO record);

    int insertSelective(ItemStockDO record);

    ItemStockDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemStockDO record);

    int updateByPrimaryKey(ItemStockDO record);
}