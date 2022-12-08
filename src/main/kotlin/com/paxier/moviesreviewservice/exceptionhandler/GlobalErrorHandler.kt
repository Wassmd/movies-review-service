package com.paxier.moviesreviewservice.exceptionhandler

import org.slf4j.LoggerFactory
import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.DefaultMessageCodesResolver
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import java.util.stream.Collector
import java.util.stream.Collectors

@ControllerAdvice
class GlobalErrorHandler {

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleRequestBodyError(ex: WebExchangeBindException): ResponseEntity<String> {
        Logger.error("Exception caught in handleRequestBodyError:", ex.message, ex)

        var error = ex.bindingResult.allErrors.stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .sorted()
            .collect(Collectors.joining(","))

        Logger.error("Error is: $error")

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    companion object {
        private val Logger = LoggerFactory.getLogger(GlobalErrorHandler::class.java)
    }
}