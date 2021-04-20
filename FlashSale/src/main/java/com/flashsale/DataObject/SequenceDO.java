package com.flashsale.DataObject;

import java.io.Serializable;

public class SequenceDO implements Serializable {
    private String name;

    private Integer currenetvalue;

    private Integer step;

    private static final long serialVersionUID = 1L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCurrenetvalue() {
        return currenetvalue;
    }

    public void setCurrenetvalue(Integer currenetvalue) {
        this.currenetvalue = currenetvalue;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }
}