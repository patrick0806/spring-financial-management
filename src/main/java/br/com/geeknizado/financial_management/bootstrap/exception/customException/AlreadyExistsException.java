package br.com.geeknizado.financial_management.bootstrap.exception.customException;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
