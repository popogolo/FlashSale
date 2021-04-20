package com.flashsale.service.Implementation;

import com.flashsale.DAO.UserDOMapper;
import com.flashsale.DAO.UserPasswordDOMapper;
import com.flashsale.DataObject.UserDO;
import com.flashsale.DataObject.UserPasswordDO;
import com.flashsale.error.BusinessException;
import com.flashsale.error.EmBusinessError;
import com.flashsale.service.UserService;
import com.flashsale.service.model.UserModel;
import com.flashsale.validator.ValidationResult;
import com.flashsale.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServieImpl implements UserService {
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Override
    public UserModel getUserByID(Integer id) {
        UserDO userDO=userDOMapper.selectByPrimaryKey(id);
        UserPasswordDO passwordDO=userPasswordDOMapper.selectByUserID(id);
        return convertFromDataObject(userDO,passwordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel==null){
            throw new BusinessException((EmBusinessError.PARAMETER_VALIDATION_ERROR));
        }
       /* if(StringUtils.isEmpty(userModel.getName())||userModel.getAge()==null||StringUtils.isEmpty(userModel.getGender())||StringUtils.isEmpty(userModel.getTelephone())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }*/
        ValidationResult result=validator.validate(userModel);
        if(result.getHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        UserDO userDO=convertFromModelToUserDO(userModel);
        try{
            userDOMapper.insertSelective(userDO);
        }
        catch(DuplicateKeyException e){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已注册");
        }

        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO= convertFromModelToPasswordDO(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel validateLogin(String telephone, String password) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO=userDOMapper.selectByTelephone(telephone);
        if(userDO==null) throw new BusinessException(EmBusinessError.USER_LOGIN_ERROR);
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserID(userDO.getId());
        UserModel userModel=convertFromDataObject(userDO,userPasswordDO);

        //比较用户信息内加密的密码是否和传输进来的加密的密码相符合
        if(!StringUtils.equals(password,userModel.getPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_ERROR);
        }
        return userModel;
    }

    private UserPasswordDO convertFromModelToPasswordDO(UserModel userModel){
        if(userModel==null) return null;
        UserPasswordDO userPasswordDO=new UserPasswordDO();
        userPasswordDO.setPassword(userModel.getPassword());

        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }
    private UserDO convertFromModelToUserDO(UserModel userModel){
        if(userModel==null) return null;
        UserDO userDO=new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    private UserModel convertFromDataObject(UserDO userDO,UserPasswordDO userPasswordDO){
        if(userDO==null){
            return null;
        }
        UserModel userModel=new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO!=null){
            userModel.setPassword(userPasswordDO.getPassword());
        }

        return userModel;
    }
}
