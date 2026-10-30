package com.jamboSwift.ByteThread.Exceptions;

import com.jamboSwift.ByteThread.DTO.ErrorObject;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintDeclarationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptions {
    // Catch DTO Validation Failures (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();

        // Extract field name and error message
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        //System.out.println("ooh shit.."+ex.getMessage());


        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error type", "VERIFICATION_FAILED");
        response.put("timestamp", LocalDateTime.now());
        response.put("errors", fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorObject> clientNotFound(ClientNotFoundException ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.NOT_FOUND.value())
                .errorType("RESOURCE_NOT_FOUND")
                .timestamp(LocalDateTime.now())
                .error(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorObject,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClientAlreadyRegistered.class)
    public ResponseEntity<ErrorObject> clientRegistered(ClientAlreadyRegistered ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.CONFLICT.value())
                .errorType("ACCOUNT_REGISTRATION_CONFLICT")
                .timestamp(LocalDateTime.now())
                .error(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorObject,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredantials.class)
    public ResponseEntity<ErrorObject> badCredentials(InvalidCredantials ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.UNAUTHORIZED.value())
                .errorType("INVALID_CREDENTIALS")
                .timestamp(LocalDateTime.now())
                .error(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorObject,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccountExists.class)
    public ResponseEntity<ErrorObject> doesAccountExixt(AccountExists ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.CONFLICT.value())
                .errorType("ACCOUNT_ERROR")
                .timestamp(LocalDateTime.now())
                .error(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorObject,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnfoundCategory.class)
    public ResponseEntity<ErrorObject> noCategoryFound(UnfoundCategory ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.NOT_FOUND.value())
                .errorType("CATEGORY_NOT_FOUND")
                .timestamp(LocalDateTime.now())
                .error(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorObject,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorObject> badRequest(HttpMessageNotReadableException ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .errorType("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .error(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorObject,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryExists.class)
    public ResponseEntity<ErrorObject> categoryThere(CategoryExists ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.CONFLICT.value())
                .errorType("CATEGORY_CONFLICT")
                .timestamp(LocalDateTime.now())
                .error(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorObject,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorObject> sqlProblems(SQLException ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorType("INTERNAL_CONFLICT1")
                .timestamp(LocalDateTime.now())
                .error("something went horribly wrong.Its us not you,stay put as we try to fix the issue")
                .build();

        System.out.println("\nSOMETHING WENT WRONG :-->"+ex.getMessage());

        return new ResponseEntity<>(errorObject,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintDeclarationException.class)
    public ResponseEntity<ErrorObject> constraintsIssues(ConstraintDeclarationException ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorType("INTERNAL_CONFLICT2")
                .timestamp(LocalDateTime.now())
                .error("something went horribly wrong.Its us not you,stay put as we try to fix the issue")
                .build();

        System.out.println("\nSOMETHING WENT WRONG :-->"+ex.getMessage());

        return new ResponseEntity<>(errorObject,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorObject> persistenceIssues(PersistenceException ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorType("INTERNAL_CONFLICT3")
                .timestamp(LocalDateTime.now())
                .error("something went horribly wrong.Its us not you,stay put as we try to fix the issue")
                .build();

        System.out.println("\nSOMETHING WENT WRONG :-->"+ex.getMessage());

        return new ResponseEntity<>(errorObject,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ErrorObject> requestValueIssues(MissingRequestValueException ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .errorType("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .error("something went horribly wrong.Your request didn't come through,stay put as we try to fix the issue")
                .build();

        System.out.println("\nSOMETHING WENT WRONG :-->"+ex.getMessage());

        return new ResponseEntity<>(errorObject,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorObject> handleRunTimeExceptions(RuntimeException ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorType("INTERNAL_CONFLICT4")
                .timestamp(LocalDateTime.now())
                .error("something went horribly wrong.Your request didn't come through,stay put as we try to fix the issue")
                .build();

        System.out.println("\nSOMETHING WENT WRONG :-->"+ex.getMessage()+ "\n\n"+Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(errorObject,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidTransaction.class)
    public ResponseEntity<ErrorObject> handleInvalidTransactions(InvalidTransaction ex){
        ErrorObject errorObject = ErrorObject.builder()
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .errorType("INVALID_TRANSACTION")
                .timestamp(LocalDateTime.now())
                .error("something went horribly wrong.Your request didn't come through,stay put as we try to fix the issue ("+ex.getMessage()+")")
                .build();

        System.out.println("\nSOMETHING WENT WRONG :-->"+ex.getMessage()+ "\n\n"+Arrays.toString(ex.getStackTrace()));

        return new ResponseEntity<>(errorObject,HttpStatus.BAD_REQUEST);
    }
}
