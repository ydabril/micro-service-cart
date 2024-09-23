package com.emazon.mscart.infraestructure.exception_handler;

import com.emazon.mscart.domain.exception.ArticleNotFoundException;
import com.emazon.mscart.domain.exception.CategoryLimitExceededException;
import com.emazon.mscart.domain.exception.OutOfStockException;
import com.emazon.mscart.domain.util.OutOfStockResponse;
import com.emazon.mscart.infraestructure.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvisor {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(propertyPath, errorMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleArticleNotFoundException(ArticleNotFoundException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(
                String.format(Constants.ARTICLE_NOT_FOUND_EXCEPTION),
                HttpStatus.NOT_FOUND.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(CategoryLimitExceededException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryLimitExceededException(CategoryLimitExceededException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(
                String.format(Constants.LIMIT_CATEGORIES_EXCEPTION),
                HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }


    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<OutOfStockResponse> handleOutOfStock(OutOfStockException ex) {
        return new ResponseEntity<>(ex.getOutOfStockResponse(), HttpStatus.BAD_REQUEST);
    }
}
