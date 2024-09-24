package db;

public class DbException extends RuntimeException{
    //construtor da exceção personalizada recebendo uma mensagem e passando para a super classe "RunTimeException"
    public DbException(String msg) {
        super(msg);
    }
}
