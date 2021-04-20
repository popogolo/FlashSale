package com.flashsale.service;

import com.flashsale.service.model.PromoModel;
import org.apache.ibatis.annotations.Param;

public interface PromoService {
    PromoModel getPromoByItemID( Integer itemid);

}
