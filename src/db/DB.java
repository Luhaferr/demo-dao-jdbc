package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    //objeto de conexão com o banco de dados do jdbc, iniciamos ele como null só pra iniciar mesmo.
    private static Connection conn = null;

    //método para conectar de fato ao banco de dados e retornar a conexão.
    public static Connection getConnection() {
        //se o objeto for nulo, ou seja, desconectado, fazemos a lógica abaixo para conectar ao bd
        if (conn == null) {
            try {
                //pega as propriedades do bd usando o loadProperties criado lá embaixo.
                Properties props = loadProperties();
                //url do banco de dados, a url se chama dburl pq foi definido assim lá no arquivo properties
                String url = props.getProperty("dburl");
                //conn recebe a conexão
                conn = DriverManager.getConnection(url, props);
            }
            catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conn;
    }

    //método para fechar a conexão e evitar possível vazamento de memória
    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            }
            catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    //método para carregar as propriedades, abre o arquivo db.properties, lê os dados e guarda no objeto Properties
    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        }
        catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    //método para fechar o Statement
    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    //método para fechar o ResultSet
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }
}
