package br.com.geeknizado.Financial.Manager.bootstrap.exception.customException;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
