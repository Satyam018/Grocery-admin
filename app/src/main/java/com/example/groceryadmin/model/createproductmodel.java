package com.example.groceryadmin.model;

public class createproductmodel {
    String productid,userid,productname,quantity,imgurl, pushid;

    public createproductmodel(String productid, String userid, String productname, String quantity, String imgurl,String pushid ) {
        this.productid = productid;
        this.userid = userid;
        this.productname = productname;
        this.quantity = quantity;
        this.imgurl = imgurl;
        this.pushid=pushid;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
