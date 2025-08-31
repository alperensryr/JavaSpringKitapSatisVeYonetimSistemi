/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.DuplicateEntityException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 *  com.alperen.kitapsatissistemi.exception.GlobalExceptionHandler
 *  com.alperen.kitapsatissistemi.service.SecurityAuditService
 *  javax.persistence.EntityNotFoundException
 *  javax.servlet.http.HttpServletRequest
 *  javax.validation.ConstraintViolationException
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.dao.DataIntegrityViolationException
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.security.access.AccessDeniedException
 *  org.springframework.validation.FieldError
 *  org.springframework.web.bind.MethodArgumentNotValidException
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.context.request.WebRequest
 *  org.springframework.web.servlet.ModelAndView
 */
package com.alperen.kitapsatissistemi.exception;

import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.DuplicateEntityException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.service.SecurityAuditService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Autowired
    private SecurityAuditService securityAuditService;

    @ExceptionHandler(value={EntityNotFoundException.class})
    public Object handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request, HttpServletRequest httpRequest) {
        if (httpRequest.getRequestURI().contains("/api/")) {
            HashMap<String, Object> errorDetails = new HashMap<String, Object>();
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("message", ex.getMessage());
            errorDetails.put("details", request.getDescription(false));
            errorDetails.put("status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", (Object)ex.getMessage());
        modelAndView.addObject("timestamp", (Object)LocalDateTime.now());
        modelAndView.setViewName("error/404");
        return modelAndView;
    }

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    public Object handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String ipAddress = this.securityAuditService.getClientIpAddress(request);
        String userAgent = this.securityAuditService.getUserAgent(request);
        logger.warn("Validation error on {} from IP {}", (Object)request.getRequestURI(), (Object)ipAddress);
        if (request.getRequestURI().contains("/api/")) {
            HashMap<String, Object> errorDetails = new HashMap<String, Object>();
            HashMap errors = new HashMap();
            ex.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError)error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("message", "Validation failed");
            errorDetails.put("errors", errors);
            errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
        }
        ModelAndView modelAndView = new ModelAndView();
        StringBuilder errorMessage = new StringBuilder("Do\u011frulama hatas\u0131: ");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errorMessage.append(fieldName).append(": ").append(message).append("; ");
        });
        modelAndView.addObject("errorMessage", (Object)errorMessage.toString());
        modelAndView.addObject("timestamp", (Object)LocalDateTime.now());
        modelAndView.setViewName("error/400");
        return modelAndView;
    }

    @ExceptionHandler(value={ConstraintViolationException.class})
    public Object handleConstraintViolationException(ConstraintViolationException ex, WebRequest request, HttpServletRequest httpRequest) {
        if (httpRequest.getRequestURI().contains("/api/")) {
            HashMap<String, Object> errorDetails = new HashMap<String, Object>();
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("message", "Constraint violation: " + ex.getMessage());
            errorDetails.put("details", request.getDescription(false));
            errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", (Object)("Do\u011frulama hatas\u0131: " + ex.getMessage()));
        modelAndView.addObject("timestamp", (Object)LocalDateTime.now());
        modelAndView.setViewName("error/400");
        return modelAndView;
    }

    @ExceptionHandler(value={DataIntegrityViolationException.class})
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        HashMap<String, Object> errorDetails = new HashMap<String, Object>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "Data integrity violation. This operation conflicts with existing data.");
        errorDetails.put("details", request.getDescription(false));
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        return new ResponseEntity(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value={AccessDeniedException.class})
    public Object handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        String ipAddress = this.securityAuditService.getClientIpAddress(request);
        String userAgent = this.securityAuditService.getUserAgent(request);
        this.securityAuditService.logUnauthorizedAccess("unknown", ipAddress, request.getRequestURI(), userAgent);
        logger.warn("Access denied on {} from IP {}", (Object)request.getRequestURI(), (Object)ipAddress);
        if (request.getRequestURI().contains("/api/")) {
            HashMap<String, Object> errorDetails = new HashMap<String, Object>();
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("message", "Bu sayfaya eri\u015fim yetkiniz yok");
            errorDetails.put("details", request.getRequestURI());
            errorDetails.put("status", HttpStatus.FORBIDDEN.value());
            return new ResponseEntity(errorDetails, HttpStatus.FORBIDDEN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", (Object)"Bu sayfaya eri\u015fim yetkiniz yok");
        modelAndView.addObject("timestamp", (Object)LocalDateTime.now());
        modelAndView.setViewName("error/403");
        return modelAndView;
    }

    @ExceptionHandler(value={BusinessException.class})
    public Object handleBusinessException(BusinessException ex, HttpServletRequest request) {
        String ipAddress = this.securityAuditService.getClientIpAddress(request);
        String userAgent = this.securityAuditService.getUserAgent(request);
        logger.warn("Business error on {} from IP {}: {}", new Object[]{request.getRequestURI(), ipAddress, ex.getMessage()});
        if (request.getRequestURI().contains("/api/")) {
            HashMap<String, Object> errorDetails = new HashMap<String, Object>();
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("message", ex.getMessage());
            errorDetails.put("details", request.getRequestURI());
            errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", (Object)ex.getMessage());
        modelAndView.addObject("timestamp", (Object)LocalDateTime.now());
        modelAndView.setViewName("error/400");
        return modelAndView;
    }

    @ExceptionHandler(value={EntityNotFoundBusinessException.class})
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundBusinessException(EntityNotFoundBusinessException ex, HttpServletRequest request) {
        String ipAddress = this.securityAuditService.getClientIpAddress(request);
        String userAgent = this.securityAuditService.getUserAgent(request);
        logger.warn("Entity not found error on {} from IP {}: {}", new Object[]{request.getRequestURI(), ipAddress, ex.getMessage()});
        HashMap<String, Object> errorDetails = new HashMap<String, Object>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("details", request.getRequestURI());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value={DuplicateEntityException.class})
    public ResponseEntity<Map<String, Object>> handleDuplicateEntityException(DuplicateEntityException ex, HttpServletRequest request) {
        String ipAddress = this.securityAuditService.getClientIpAddress(request);
        String userAgent = this.securityAuditService.getUserAgent(request);
        logger.warn("Duplicate entity error on {} from IP {}: {}", new Object[]{request.getRequestURI(), ipAddress, ex.getMessage()});
        HashMap<String, Object> errorDetails = new HashMap<String, Object>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("details", request.getRequestURI());
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        return new ResponseEntity(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value={RuntimeException.class})
    public Object handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        String ipAddress = this.securityAuditService.getClientIpAddress(request);
        String userAgent = this.securityAuditService.getUserAgent(request);
        logger.error("Runtime error on {} from IP {}: {}", new Object[]{request.getRequestURI(), ipAddress, ex.getMessage(), ex});
        if (request.getRequestURI().contains("/api/")) {
            HashMap<String, Object> errorDetails = new HashMap<String, Object>();
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("message", "Bir sistem hatas\u0131 olu\u015ftu");
            errorDetails.put("details", request.getRequestURI());
            errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", (Object)"Sunucu hatas\u0131");
        modelAndView.addObject("timestamp", (Object)LocalDateTime.now());
        modelAndView.setViewName("error/500");
        return modelAndView;
    }

    @ExceptionHandler(value={Exception.class})
    public Object handleAllExceptions(Exception ex, HttpServletRequest request) {
        String ipAddress = this.securityAuditService.getClientIpAddress(request);
        String userAgent = this.securityAuditService.getUserAgent(request);
        logger.error("Unhandled error on {} from IP {}: {}", new Object[]{request.getRequestURI(), ipAddress, ex.getMessage(), ex});
        if (request.getRequestURI().contains("/api/")) {
            HashMap<String, Object> errorDetails = new HashMap<String, Object>();
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("message", "Internal server error");
            errorDetails.put("details", request.getRequestURI());
            errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", (Object)"Beklenmeyen bir hata olu\u015ftu");
        modelAndView.addObject("timestamp", (Object)LocalDateTime.now());
        modelAndView.setViewName("error/500");
        return modelAndView;
    }
}

