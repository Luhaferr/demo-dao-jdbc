package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

//classe auxiliar responsável por instanciar os Daos
public class DaoFactory {
    /*
    método estático do tipo SellerDao(interface) para criar um objeto de acesso a dados que retorna
    um novo objeto Seller já conectado ao BD. Por isso é obrigado a passar uma conexão como argumento.
     */
    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }
}
