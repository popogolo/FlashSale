package com.flashsale.controller;

import com.flashsale.DataObject.UserDO;
import com.flashsale.Response.CommonReturnType;
import com.flashsale.controller.ViewObject.UserVO;
import com.flashsale.error.BusinessException;
import com.flashsale.error.EmBusinessError;
import com.flashsale.service.UserService;
import com.flashsale.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest httpServletRequest;
    //用户登录接口
    @RequestMapping(value="/login",method={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telephone")String telphone,@RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(telphone==null||telphone.equals("")||password==null||password.equals("")){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号或密码不能为空");
        }
        //用户登陆服务
        UserModel userModel= userService.validateLogin(telphone,EncodeByMD5(password));
        //将登录的凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);
    }
    //用户获取手机opt码的接口
    @RequestMapping(value="/getotp",method={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOpt(@RequestParam(name="telphone") String telephone){
        Random random=new Random();
        int randomInt=random.nextInt(9999);
        randomInt+=1000;
        String Optcode=String.valueOf(randomInt);
        httpServletRequest.getSession().setAttribute(telephone,Optcode);
        System.out.println("telephone "+telephone+"  Optcode "+Optcode);
        return CommonReturnType.create(null);
    }
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        UserModel userModel=userService.getUserByID(id);
        if(userModel==null) throw new BusinessException(EmBusinessError.USER_NOT_EXIST);

        UserVO userVO=convertFromUserModel(userModel);
        return CommonReturnType.create(userVO);
    }
    //用户注册接口
    @RequestMapping(value="/register",method={RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="name") String name,
                                     @RequestParam(name="age")  Integer age,
                                     @RequestParam(name="gender") String gender,
                                     @RequestParam(name="telephone") String telephone,
                                     @RequestParam(name="password") String password,
                                     @RequestParam(name="optcode") String otpcode) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证otpcode和telephone是否相符合
        String inSessionOtpCode=(String)this.httpServletRequest.getSession().getAttribute(telephone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpcode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"验证码错误");
        }
        //用户注册
        UserModel userModel=new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(gender);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("Byphone");
        userModel.setPassword(EncodeByMD5(password));
        userService.register(userModel);
        return CommonReturnType.create(null);


    }
    public String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder=new BASE64Encoder();
        //加密字符串
        String newstr =base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }
    private UserVO convertFromUserModel(UserModel userModel){
        UserVO userVO=new UserVO();
        if(userModel==null)
            return null;
        BeanUtils.copyProperties(userModel ,userVO);
        return userVO;
    }

}
