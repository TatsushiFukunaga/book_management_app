package com.quoCard.bookManagementApp.model.exception

import com.quoCard.bookManagementApp.model.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(
        ex: ResourceNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorDetails = ErrorResponse(
            message = ex.message ?: "Resource not found",
            status = HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(errorDetails, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorDetails = ErrorResponse(
            message = ex.message ?: "Invalid request",
            status = HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidAuthorException::class)
    fun handleInvalidAuthorException(
        ex: InvalidAuthorException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorDetails = ErrorResponse(
            message = ex.message ?: "Invalid authors provided",
            status = HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorDetails = ErrorResponse(
            message = ex.message ?: "An unexpected error occurred",
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        )
        return ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}