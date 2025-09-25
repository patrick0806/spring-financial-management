package br.com.geeknizado.Financial.Manager.bootstrap.exception.customException;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
