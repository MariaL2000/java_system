package com.example.entrepisej.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Recurso no encontrado (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
    }

    // 2. Error de Seguridad: No tiene permisos/roles (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "Forbidden", 
            "No tienes permisos suficientes para realizar esta acción.", request);
    }

    // 3. Error de Autenticación: Credenciales inválidas o Token expirado (401)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationError(AuthenticationException ex, WebRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", 
            "Error de autenticación: " + ex.getMessage(), request);
    }

    // 4. Error en subida de imágenes a Cloudinary (400)
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadError(IOException ex, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "File Upload Error", 
            "Error al procesar la imagen: " + ex.getMessage(), request);
    }

    // 5. Error genérico (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", 
            "Ocurrió un error inesperado: " + ex.getMessage(), request);
    }

    // Método auxiliar para no repetir código (DRY)
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, String message, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            error,
            message,
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(response, status);
    }
}