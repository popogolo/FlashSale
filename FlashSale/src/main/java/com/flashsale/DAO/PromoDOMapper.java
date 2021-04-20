package com.flashsale.DAO;

import com.flashsale.DataObject.PromoDO;
import org.apache.ibatis.annotations.Param;

public interface PromoDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromoDO record);

    int insertSelective(PromoDO record);

    PromoDO selectByPrimaryKey(Integer id);

    PromoDO getPromoByItemID(@Param("itemid") Integer itemid);

    int updateByPrimaryKeySelective(PromoDO record);

    int updateByPrimaryKey(PromoDO record);
}