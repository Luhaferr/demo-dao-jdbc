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
            // Validação do nome do departamento
            if (obj.getName() == null || obj.getName().trim().isEmpty()){
                throw new IllegalArgumentException("Department name cannot be null or empty.");
            }
            // Inicia a transação
            conn.setAutoCommit(false);
            st = conn.prepareStatement("INSERT INTO department (Name) Values (?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());

            //variável recebendo a execução do comando SQL e contabilizando número de linhas afetadas
            int rowsAffected = st.executeUpdate();

            //lógica para verificar quantas linhas foram afetadas e atribuir o ID aos novos objetos
            if (rowsAffected > 0) {
                //recebe as chaves geradas
                ResultSet rs = st.getGeneratedKeys();
                //caso haja mais de uma inserção
                if (rs.next()) {
                    //variavel id recebe o ID(coluna 1 no Banco de dados)
                    int id = rs.getInt(1);
                    //atribui o id recebido acima no objeto Seller
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else {
                //caso não hajam linhas afetadas
                throw new DbException("Unexpected error! No rows affected!");
            }
            //confirmação explicita para que as operações sejam executadas
            conn.commit();
            //retorno de sucesso na operação
            System.out.println("Department inserted successfully! New Id = " + obj.getId());
        }
        catch (SQLException e) {
            try {
                //o rollback faz com que a transação volte caso tenha parado no meio.
                conn.rollback();
                throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
            } catch (SQLException ex) {
                //em caso de erro no rollback
                throw new DbException("Error trying to rollback! Caused by: " + ex.getMessage());
            }
        }
        finally {
            DB.closeStatement(st);
        }
    }

    //método para atualizar os dados de um Department no Banco de dados
    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);

            st = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
            st.executeUpdate();

            //confirmação explicita para que as operações sejam executadas
            conn.commit();
            //retorno de sucesso na operação
            System.out.println("Update successful! " + obj);
        }
        catch (SQLException e) {
            try {
                //o rollback faz com que a transação volte caso tenha parado no meio.
                conn.rollback();
                throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
            } catch (SQLException ex) {
                //em caso de erro no rollback
                throw new DbException("Error trying to rollback! Caused by: " + ex.getMessage());
            }
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
            // Inicia a transação
            conn.setAutoCommit(false);
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            st.setInt(1, id);
            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new DbException("Error! Id not found");
            }
            //confirmação explicita para que as operações sejam executadas
            conn.commit();
            System.out.println("Department deleted sucessfully! Deleted Department ID = " + id);
        }
        catch (SQLException e) {
            try {
                //o rollback faz com que a transação volte caso tenha parado no meio.
                conn.rollback();
                throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
            } catch (SQLException ex) {
                //em caso de erro no rollback
                throw new DbException("Error trying to rollback! Caused by: " + ex.getMessage());
            }
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

            //lógica para transformar a tabela recebida pelo ResultSet em objeto
            while (rs.next()) {
                //criação e preenchimento do objeto
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                //adiciona o objeto na lista
                list.add(obj);
            }
            //retorna a lista de departamentos
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
