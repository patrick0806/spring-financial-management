package br.com.geeknizado.Financial.Manager.bootstrap.exception.customException;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
