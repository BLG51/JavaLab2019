package service;

import model.BuyEntry;
import model.Product;

public class UserService {
    ProductService ps = new ProductService();
    BuyListService bls = new BuyListService();


    public void buyProduct(int userid, int productid) {
        Product p = ps.get(productid);
        if (p.getCount() < 2) {
            throw new IllegalArgumentException("not in stock");
        } else {
            p.setCount(p.getCount()-1);
            ps.update(p.getId(), p);
            bls.create(new BuyEntry(userid, productid));
        }
    }
}
