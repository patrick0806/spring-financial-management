package br.com.geeknizado.financial_management.bootstrap.exception;


import br.com.geeknizado.financial_management.bootstrap.exception.customException.*;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleGenericException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Problem exceptionData = Problem.builder()
                .status(status.value())
                .title("Internal server error")
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(exceptionData);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Problem> handleAlreadyExistException(AlreadyExistsException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        Problem exceptionData = Problem.builder()
                .status(status.value())
                .title("Conflict data")
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(exceptionData);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Problem> handleAlreadyExistException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem exceptionData = Problem.builder()
                .status(status.value())
                .title("Content not found")
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(exceptionData);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Problem> handleBadRequestException(BadRequestException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Problem exceptionData = Problem.builder()
                .status(status.value())
                .title("Invalid Arguments")
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(exceptionData);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<Problem> handleJWTVerificationException(JWTVerificationException ex){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Problem exceptionData = Problem.builder()
                .status(status.value())
                .title("Invalid Token")
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(exceptionData);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Problem> handleBadCredentialsException(BadCredentialsException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Problem exceptionData = Problem.builder()
                .status(status.value())
                .title("Unauthorized access")
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(exceptionData);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Problem> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Problem exceptionData = Problem.builder()
                .status(status.value())
                .title("Unauthorized access")
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(exceptionData);
    }

    @ExceptionHandler(GenerateTokenException.class)
    public ResponseEntity<Problem> handleGenerateTokenException(GenerateTokenException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Problem problem = Problem.builder()
                .status(status.value())
                .title("Internal server error")
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Problem> handleBusinessException(BusinessException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        Problem problem = Problem.builder()
                .status(status.value())
                .title("Business rule violation")
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpStatus httpStatus = (HttpStatus) status;
        String userMessage = "One or more parameters are invalid please re send the data with valid information";
        BindingResult bindingResult = ex.getBindingResult();
        List<Problem.Field> problemFields = bindingResult.getFieldErrors()
                .stream()
                .map(fieldError ->{
                            String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                            return Problem.Field.builder()
                                    .name(fieldError.getField())
                                    .userMessage(message)
                                    .build();
                        }
                )
                .collect(Collectors.toList());


        Problem exceptionData = Problem.builder()
                .status(httpStatus.value())
                .title("Invalid Params")
                .detail("One or more fields are invalid. Fill in correctly and try again")
                .userMessage(userMessage)
                .timestamp(LocalDateTime.now())
                .fields(problemFields)
                .build();
        return handleExceptionInternal(ex,exceptionData,headers,httpStatus,request);
    }
}