package model.dao;

import model.dao.impl.SellerDaoJDBC;

//classe auxiliar responsável por instanciar os Daos
public class DaoFactory {
    //todo: entender melhor sobre essa lógica
    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC();
    }
}
