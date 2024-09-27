package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection conn;
    //construtor com argumento tipo Connection para forçar injeção de dependência
    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    //método para inserir um novo Department no Banco de dados
    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO department (Name) Values (?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    /*
    método para alterar dados de um departamento, primeiro parâmetro um objeto do tipo department para que ele possa ser
    acessado pelo index através do st.setString. O segundo parâmetro é o id do departamento a ser alterado
     */
    @Override
    public void update(Department obj, Integer id) {
        //todo: lógica de verificação em caso de id não existente
        //validação de ID para evitar consultas desnecessárias no BD.
        if (id == null || id <=0) {
            throw new DbException("Invalid Id: " + id);
        }
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
            st.setString(1, obj.getName());
            st.setInt(2, id);
            st.executeUpdate();
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    //método para deletar um Department por Id
    @Override
    public void deleteById(Integer id) {
        //validação de ID para evitar consultas desnecessárias no BD.
        if (id == null || id <=0) {
            throw new DbException("Invalid Id: " + id);
        }
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            st.setInt(1, id);
            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new DbException("Error! Id not found");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    //método para consultar Department por Id
    @Override
    public Department findById(Integer id) {
        //validação de ID para evitar consultas desnecessárias no BD.
        if (id == null || id <=0) {
            throw new DbException("Invalid Id: " + id);
        }
        //objeto para executar consultas SQL com parâmetros.
        PreparedStatement st = null;
        //armazena e manipula os resultados da consulta SQL feitas pelo PreparedStatement
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM department WHERE Id = ?");
            //substituindo o placeholder
            st.setInt(1, id);

            //rs recebe o resultado da execução da consulta
            rs = st.executeQuery();

            /*
            o resultset traz resultados em forma de tabela, como usamos POO,
            a lógica abaixo cria um objeto do tipo Department

            o if é pra existe algum resultado, ou seja, se existe algum department com esses parâmetros,
            se não houver ele retorna null/não existe department com esse Id.
            o next é para continuar navegando pelos dados da tabela para instanciar o objeto
             */
            if (rs.next()) {
                //instanciando departamento
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                return obj;
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        //fechamento dos recursos
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    //método para consultar e listar os Departments ordenados por nome
    @Override
    public List<Department> findAll() {
        //objeto para executar consultas SQL com parâmetros.
        PreparedStatement st = null;
        //armazena e manipula os resultados da consulta SQL feitas pelo PreparedStatement
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM department ORDER BY Name");
            rs = st.executeQuery();

            List<Department> list = new ArrayList<>();

            while (rs.next()) {
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                list.add(obj);
            }
            return list;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
