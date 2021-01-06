package com.restarauntvote.restarauntservice.exceptions;

import com.restarauntvote.restarauntservice.util.ValidationUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";
    private static final String EXCEPTION_DUPLICATE_RESTAURANT = "Restaurant with this name already exist";
    private static final String EXCEPTION_DUPLICATE_LUNCH = "In the selected restaurant a lunch with this name already exists";

    private static final Map<String, String> CONSTRAINS_I18N_MAP = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("unique_users_email_idx", EXCEPTION_DUPLICATE_EMAIL);
                    put("unique_restaurant_name_idx", EXCEPTION_DUPLICATE_RESTAURANT);
                    put("unique_lunch_name_idx", EXCEPTION_DUPLICATE_LUNCH);
                }
            });


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ExceptionResponse(BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(BAD_REQUEST);
        exceptionResponse.setMessage("Validation error");
        exceptionResponse.addValidationErrors(ex.getBindingResult().getFieldErrors());
        exceptionResponse.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(exceptionResponse);
    }

    @ExceptionHandler(AccessDeniedException.class) //403
    public final ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.FORBIDDEN, "Access denied", ex);
        return buildResponseEntity(exceptionResponse);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(NOT_FOUND, ex);
        return handleExceptionInternal(ex, exceptionResponse, headers, exceptionResponse.getStatus(), request);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public final ResponseEntity<Object> handleRestaurantNotFoundException(RestaurantNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(NOT_FOUND, ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(exceptionResponse);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(NOT_FOUND, ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(exceptionResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponseEntity(new ExceptionResponse(NOT_FOUND, ex));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public final ResponseEntity<Object> handleDataNotFound(DataNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(CONFLICT, ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(exceptionResponse);
    }

    @ExceptionHandler(ValidationLimitException.class)
    public final ResponseEntity<Object> handleValidationLimit(ValidationLimitException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(CONFLICT, ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(exceptionResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Object> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {
        String rootMsg = ValidationUtil.getRootCause(ex).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            Optional<Map.Entry<String, String>> entry = CONSTRAINS_I18N_MAP.entrySet().stream()
                    .filter(it -> lowerCaseMsg.contains(it.getKey()))
                    .findAny();
            if (entry.isPresent()) {
                String message = "Validation error, " + entry.get().getValue();
                return buildResponseEntity(new ExceptionResponse(CONFLICT, message, ex));
            }
        }
        return buildResponseEntity(new ExceptionResponse(BAD_REQUEST, ex));
    }


    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(
            ValidationException ex) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(CONFLICT, "Validation error", ex);
        if (ex instanceof ConstraintViolationException) {
            exceptionResponse.addValidationErrors(((ConstraintViolationException) ex).getConstraintViolations());
        }

        return buildResponseEntity(exceptionResponse);
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleErrorException(Exception ex) {
        Throwable rootCause = ValidationUtil.getRootCause(ex);

        if (rootCause instanceof ConstraintViolationException) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(CONFLICT, "Validation error", ex.getCause());
            exceptionResponse.addValidationErrors(((ConstraintViolationException) rootCause).getConstraintViolations());
            return buildResponseEntity(exceptionResponse);
        }

        return buildResponseEntity(new ExceptionResponse(BAD_REQUEST, ex));
    }


    private ResponseEntity<Object> buildResponseEntity(ExceptionResponse exceptionResponse) {
        return new ResponseEntity<Object> (exceptionResponse, exceptionResponse.getStatus());
    }
}
