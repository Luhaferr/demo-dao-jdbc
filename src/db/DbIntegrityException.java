package db;

public class DbIntegrityException extends RuntimeException{
    //exception criada para evitar erros de integridade referencial
    public DbIntegrityException(String message) {
        super(message);
    }
}
