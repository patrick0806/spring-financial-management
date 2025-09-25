package br.com.geeknizado.Financial.Manager.bootstrap.exception.customException;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
