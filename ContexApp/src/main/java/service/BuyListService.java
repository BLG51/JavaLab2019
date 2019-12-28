package service;

import context.Component;
import repository.BuyListRepository;
import repository.BuyListRepositoryImpl;
import model.BuyEntry;
import util.ConnectionManager;

import java.sql.Connection;
import java.util.List;

public class BuyListService implements Component {
    private BuyListRepository dao;

    public BuyListService() {
        Connection connection = ConnectionManager.getConnection();
        dao = new BuyListRepositoryImpl(connection);
    }
    public List<BuyEntry> getAll(){return dao.getAll();}
    public void create(BuyEntry item){dao.create(item);}
    public void delete(int id){dao.delete(id);}

    @Override
    public String getName() {
        return "buyListService";
    }
}
