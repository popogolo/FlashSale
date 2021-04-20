package com.flashsale.service.Implementation;

import com.flashsale.DAO.PromoDOMapper;
import com.flashsale.DataObject.PromoDO;
import com.flashsale.service.PromoService;
import com.flashsale.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    PromoDOMapper promoDOMapper;


    @Override
    @Transactional
    public PromoModel getPromoByItemID(Integer itemid) {
        PromoDO promoDO=promoDOMapper.getPromoByItemID(itemid);
        PromoModel promoModel=convertModelFromObject(promoDO);
        if(promoModel==null) return null;
            //判读当前活动是否正在进行
            if (promoModel.getStartdate().isAfterNow()) {
                promoModel.setStatus(1);
            } else if (promoModel.getEnddate().isBeforeNow()) {
                promoModel.setStatus(3);
            } else promoModel.setStatus(2);

        return promoModel;
    }

    private PromoModel convertModelFromObject(PromoDO promoDO) {
        if(promoDO==null) return null;
        PromoModel promoModel=new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setStartdate(new DateTime(promoDO.getStartdate()));
        promoModel.setEnddate(new DateTime(promoDO.getEnddate()));
        return promoModel;
    }


}
