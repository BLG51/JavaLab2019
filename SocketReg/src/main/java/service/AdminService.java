package service;

import model.Product;
import model.User;
import servers.RegServer;

public class AdminService {
    ProductService ps = new ProductService();
    RegService rs = new RegService();

    public void addProduct(int userid, Product item){
        if (isAdmin(userid))
        ps.create(item); else throw new IllegalArgumentException("not admin");}

    public void deleteProduct(int userid, int id){
        if (isAdmin(userid)) ps.delete(id); else throw new IllegalArgumentException("not admin");}

    public boolean isAdmin(int id) {
        User u = rs.getById(id);
        return (u.getRole().equals("ADMIN"));
    }

    public boolean isAdmin(String login) {
        User u = rs.get(login);
        return (u.getRole().equals("ADMIN"));
    }
}
