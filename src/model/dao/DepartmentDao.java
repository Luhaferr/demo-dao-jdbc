package model.dao;

import model.entities.Department;

import java.util.List;

public interface DepartmentDao {
    //insere um objeto department
    void insert(Department obj);

    //atualiza um objeto department
    void update(Department obj);

    /*
    método responsável por pegar o id do parâmetro e DELETAR no BD um objeto com esse id, se existir retorna
    se não retorna null
     */
    void deleteById(Integer id);

    /*
    método responsável por pegar o id do parâmetro e CONSULTAR no BD um objeto com esse id, se existir retorna
    se não retorna null
     */
    Department findById(Integer id);

    //retorna todos os departamentos em uma lista
    List<Department> findAll();
}
