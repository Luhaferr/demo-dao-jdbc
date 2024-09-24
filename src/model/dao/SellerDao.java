package model.dao;

import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public interface SellerDao {
    //insere um objeto seller
    void insert(Seller obj);

    //atualiza um objeto seller
    void update(Seller obj);

    /*
    método responsável por pegar o id do parâmetro e DELETAR no BD um objeto com esse id, se existir retorna
    se não retorna null
     */
    void deleteById(Integer id);

    /*
    método responsável por pegar o id do parâmetro e CONSULTAR no BD um objeto com esse id, se existir retorna
    se não retorna null
     */
    Seller findById(Integer id);

    //retorna todos os departamentos em uma lista
    List<Seller> findAll();
}
