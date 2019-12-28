package model;

public class BuyEntry {
    private int id;
    private int userid;
    private int productid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public BuyEntry(int userid, int productid) {
        this.userid = userid;
        this.productid = productid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public BuyEntry(int id, int userid, int productid) {
        this.id = id;
        this.userid = userid;
        this.productid = productid;
    }
}
