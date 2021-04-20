package com.flashsale;

import com.flashsale.DAO.UserDOMapper;
import com.flashsale.DataObject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.flashsale"})
@RestController
@MapperScan("com.flashsale.DAO")
public class App 
{
    @Autowired
    private UserDOMapper userDOMapper;
    @RequestMapping("/")
    public String test(){
        UserDO userDO=userDOMapper.selectByPrimaryKey(1);
        if(userDO==null)
        return "用户对象不存在";
        else return userDO.getName();
    }
    public static void main( String[] args )
    {

        SpringApplication.run(App.class,args);

    }
}
