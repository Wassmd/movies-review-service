package com.paxier.moviesreviewservice.exceptionhandler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalErrorHandler(private val objectMapper: ObjectMapper): ErrorWebExceptionHandler {

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val dataBufferFactory = exchange.response.bufferFactory()
        val buffer = dataBufferFactory.wrap(objectMapper.writeValueAsBytes(ex.message))

        if (ex is ReviewDataException) {
            exchange.response.statusCode = HttpStatus.BAD_REQUEST
        } else if (ex is ReviewNotFoundException) {
            exchange.response.statusCode = HttpStatus.NOT_FOUND
        }
        else {
            exchange.response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return exchange.response.writeWith(Mono.just(buffer))
    }
}