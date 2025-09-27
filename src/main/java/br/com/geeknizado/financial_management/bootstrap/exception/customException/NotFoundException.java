package br.com.geeknizado.financial_management.bootstrap.exception.customException;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
