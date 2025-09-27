package br.com.geeknizado.financial_management.bootstrap.exception.customException;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
