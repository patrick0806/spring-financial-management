package br.com.geeknizado.financial_management.bootstrap.exception.customException;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
