package com.flashsale.DataObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class PromoDO implements Serializable {
    private Integer id;

    private String promoname;

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    private Date startdate;

    private Date enddate;

    private Integer itemid;

    private BigDecimal promoitemprice;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoname() {
        return promoname;
    }

    public void setPromoname(String promoname) {
        this.promoname = promoname == null ? null : promoname.trim();
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public BigDecimal getPromoitemprice() {
        return promoitemprice;
    }

    public void setPromoitemprice(BigDecimal promoitemprice) {
        this.promoitemprice = promoitemprice;
    }
}