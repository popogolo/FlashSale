package com.flashsale.service.Implementation;

import com.flashsale.DAO.ItemDOMapper;
import com.flashsale.DAO.ItemStockDOMapper;
import com.flashsale.DataObject.ItemDO;
import com.flashsale.DataObject.ItemStockDO;
import com.flashsale.error.BusinessException;
import com.flashsale.error.EmBusinessError;
import com.flashsale.service.ItemService;
import com.flashsale.service.PromoService;
import com.flashsale.service.model.ItemModel;
import com.flashsale.service.model.PromoModel;
import com.flashsale.validator.ValidationResult;
import com.flashsale.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    PromoService promoService;
    @Autowired
    ValidatorImpl validator;
    @Autowired
    ItemDOMapper itemDOMapper;
    @Autowired
    ItemStockDOMapper itemStockDOMapper;
    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult result=validator.validate(itemModel);
        if(result.getHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        //转化itemmodel->dataobject
        ItemDO itemDO=convertTOItemDOFromModel(itemModel);
        ItemStockDO itemStockDO=convertTOItemStockDOFromModel(itemModel);
        //写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemStockDO.setItemId(itemDO.getId());
        itemStockDOMapper.insertSelective(itemStockDO);


        //返回创建完成的对象
        return getItem(itemDO.getId());

    }
    private ItemDO convertTOItemDOFromModel(ItemModel itemModel){
        if(itemModel==null) return null;
        ItemDO itemDO=new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        return itemDO;
    }
    private ItemStockDO convertTOItemStockDOFromModel(ItemModel itemModel){
        if(itemModel==null) return null;
        ItemStockDO itemStockDO=new ItemStockDO();
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;

    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> ItemDoList=itemDOMapper.listItem();
        List<ItemModel> itemModels=ItemDoList.stream().map(itemDO ->{
            ItemStockDO itemStockDO=itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel=this.convertModelFromDataObject(itemDO,itemStockDO);
            return itemModel;
        } ).collect(Collectors.toList());
        return itemModels;
    }

    @Override
    @Transactional
    public ItemModel getItem(Integer id) {
        ItemDO itemDO=itemDOMapper.selectByPrimaryKey(id);
        if(itemDO==null)  return null;
        ItemStockDO itemStockDO=itemStockDOMapper.selectByItemId(id);
        ItemModel itemModel=convertModelFromDataObject(itemDO,itemStockDO);
        PromoModel promoModel=promoService.getPromoByItemID(itemModel.getId());
        if(promoModel!=null&&promoModel.getStatus().intValue()!=3){
            itemModel.setPromoModel(promoModel);
        }
        return  itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseItem(Integer id, Integer amount) {
        int affectedRow=itemStockDOMapper.updateByItemid(id,amount);
        if(affectedRow>0){
            return true;
        }
        else{
            return  false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) {
       itemDOMapper.updateSales(itemId,amount);

    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO,ItemStockDO itemStockDO){
        ItemModel itemModel=new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }

}
