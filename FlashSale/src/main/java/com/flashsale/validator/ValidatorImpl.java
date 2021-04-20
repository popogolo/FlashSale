package com.flashsale.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {
    private Validator validator;
    public ValidationResult validate(Object bean){
         ValidationResult validationResult=new ValidationResult();
        Set<ConstraintViolation<Object> >constraintViolationsSet=validator.validate(bean);
        if(constraintViolationsSet.size()>0){
            validationResult.setHasError(true);
            constraintViolationsSet.forEach(constraintViolation->{
                String errMsg= constraintViolation.getMessage();
                String propertyName= constraintViolation.getPropertyPath().toString();
                validationResult.getErrorMsgMap().put(propertyName,errMsg);
            });
        }
        return validationResult;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        this.validator= Validation.buildDefaultValidatorFactory().getValidator();
    }
}
