package service;

import dao.BuyListDao;
import dao.BuyListDaoImpl;
import dao.ProductDao;
import dao.ProductDaoImpl;
import model.BuyEntry;
import model.Product;

import java.sql.Connection;
import java.util.List;

public class BuyListService{
    private BuyListDao dao;

    public BuyListService() {
        Connection connection = ConnectionManager.getConnection();
        dao = new BuyListDaoImpl(connection);
    }
    public List<BuyEntry> getAll(){return dao.getAll();}
    public void create(BuyEntry item){dao.create(item);}
    public void delete(int id){dao.delete(id);}
}
