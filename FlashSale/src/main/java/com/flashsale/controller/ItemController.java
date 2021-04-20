package com.flashsale.controller;

import com.flashsale.Response.CommonReturnType;
import com.flashsale.controller.ViewObject.ItemVO;
import com.flashsale.error.BusinessException;
import com.flashsale.service.ItemService;
import com.flashsale.service.model.ItemModel;
import com.flashsale.service.model.PromoModel;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ItemController extends BaseController {
    @Autowired
    ItemService itemService;
    @RequestMapping(value="/createItem",method={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name="title") String title,
                                       @RequestParam(name="price") BigDecimal price,
                                       @RequestParam(name="stock") Integer stock,
                                       @RequestParam(name="description") String description,
                                       @RequestParam(name="imgUrl") String imgUrl) throws BusinessException {
        ItemModel itemmodel=new ItemModel();
        itemmodel.setStock(stock);
        itemmodel.setDescription(description);
        itemmodel.setPrice(price);
        itemmodel.setImgUrl(imgUrl);
        itemmodel.setTitle(title);
        ItemModel returnItemModel=itemService.createItem(itemmodel);
        ItemVO itemVO=convertVOFromModel(returnItemModel);
        return CommonReturnType.create(itemVO);
    }

    //商品详情页浏览
    @RequestMapping(value="/get",method={RequestMethod.GET})
    @ResponseBody
    public CommonReturnType  getItemByID(@RequestParam(name ="id") Integer id){
        ItemModel itemModel=itemService.getItem(id);
        ItemVO itemVO=convertVOFromModel(itemModel);
        return CommonReturnType.create(itemVO);

    }
    //商品列表页面浏览
    @RequestMapping(value="/list",method={RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem(){
        List<ItemModel> itemModelList=itemService.listItem();
        List<ItemVO> itemVOList=itemModelList.stream().map(itemModel -> {
            ItemVO itemVO=convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOList);
    }
    private ItemVO convertVOFromModel(ItemModel itemModel){
        if(itemModel==null){
            return null;
        }
        ItemVO itemVO=new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        PromoModel promoModel=itemModel.getPromoModel();
        if(promoModel!=null){

            itemVO.setPromoId(promoModel.getId());
            itemVO.setPromoPrice(promoModel.getPromoitemprice());
            itemVO.setPromoStatus(promoModel.getStatus());
            itemVO.setStartDate(promoModel.getStartdate().toString(DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss")));
        }
        return itemVO;
    }
}
