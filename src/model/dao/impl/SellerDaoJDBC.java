package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//é a classe que implementa a interface SelleDao
public class SellerDaoJDBC implements SellerDao {
    private Connection conn;

    //construtor com argumento tipo Connection para forçar injeção de dependência
    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    //método para consultar Seller por Id
    @Override
    public Seller findById(Integer id) {
        //objeto para executar consultas SQL com parâmetros.
        PreparedStatement st = null;
        //armazena e manipula os resultados da consulta SQL feitas pelo PreparedStatement
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ?");
            //substituindo o placeholder
            st.setInt(1, id);

            //rs recebe o resultado da execução da consulta
            rs = st.executeQuery();

            /*
            o resultset traz resultados em forma de tabela, como usamos POO, a lógica abaixo cria um objeto do tipo Seller
            associado a outro objeto com os dados do departamento dele

            o if é pra existe algum resultado, ou seja, se existe algum vendedor com esses parâmetros
            se não houver, ele retorna null/não existe vendedor com esse Id.
            o next é para continuar navegando pelos dados da tabela para instanciar o objeto
             */
            if (rs.next()) {
                //instanciando departamento
                Department dep = instantiateDepartment(rs);
                //instanciando vendedor
                Seller obj = instantiateSeller(rs, dep);
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
    //método pra instanciar department e poder reusar esse método quando necessário
    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }
    //método pra instanciar seller e poder reusar esse método quando necessário
    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        obj.setDepartment(dep);
        return obj;
    }

    //método para consultar e listar os Sellers ordenados por nome
    @Override
    public List<Seller> findAll() {
        //objeto para executar consultas SQL com parâmetros.
        PreparedStatement st = null;
        //armazena e manipula os resultados da consulta SQL feitas pelo PreparedStatement
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name");

            //rs recebe o resultado da execução da consulta
            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            //map pra guardar qualquer department e auxiliar no controle de instancias do Department
            Map<Integer, Department> map = new HashMap<>();

            /*
            o resultset traz resultados em forma de tabela, como usamos POO, a lógica abaixo cria um objeto do tipo Seller
            associado a outro objeto com os dados do departamento dele

            quero instanciar uma lista com um ou mais sellers, mas apenas um department, lógica abaixo
            */
            while (rs.next()) {
                //busca no map se existe algum department com o mesmo id, se existir, eu uso, se não, é igual a null
                Department dep = map.get(rs.getInt("DepartmentId"));

                //lógica para instanciar department caso não exista map com esse id instanciado
                if (dep == null) {
                    //instanciando departamento
                    dep = instantiateDepartment(rs);
                    //salva o department no map
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                //instanciando vendedor
                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
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

    //método para consultar um Seller por departamento ordenados por nome
    @Override
    public List<Seller> findByDepartment(Department department) {
        //objeto para executar consultas SQL com parâmetros.
        PreparedStatement st = null;
        //armazena e manipula os resultados da consulta SQL feitas pelo PreparedStatement
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE DepartmentId = ? "
                            + "ORDER BY Name");
            //substituindo o placeholder
            st.setInt(1, department.getId());

            //rs recebe o resultado da execução da consulta
            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            //map pra guardar qualquer department e auxiliar no controle de instancias do Department
            Map<Integer, Department> map = new HashMap<>();

            /*
            o resultset traz resultados em forma de tabela, como usamos POO, a lógica abaixo cria um objeto do tipo Seller
            associado a outro objeto com os dados do departamento dele

            quero instanciar uma lista com um ou mais sellers, mas apenas um department, lógica abaixo
            */
            while (rs.next()) {
                //busca no map se existe algum department com o mesmo id, se não existir é igual a null
                Department dep = map.get(rs.getInt("DepartmentId"));

                //lógica para instanciar department caso não exista map com esse id instanciado
                if (dep == null) {
                    //instanciando departamento
                    dep = instantiateDepartment(rs);
                    //salva o department no map
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                //instanciando vendedor
                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
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
}
