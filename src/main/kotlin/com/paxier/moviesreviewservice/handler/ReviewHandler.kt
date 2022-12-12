package com.paxier.moviesreviewservice.handler

import com.paxier.moviesreviewservice.domain.Review
import com.paxier.moviesreviewservice.exceptionhandler.ReviewDataException
import com.paxier.moviesreviewservice.exceptionhandler.ReviewNotFoundException
import com.paxier.moviesreviewservice.repository.ReviewRepository
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.logging.Logger
import java.util.stream.Collector
import java.util.stream.Collectors
import javax.validation.ConstraintViolation
import javax.validation.Validator

@Configuration
class ReviewHandler(
    @Autowired val repository: ReviewRepository,
    @Autowired val validator: Validator
    ) {

    fun addReview(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(Review::class.java)
            .doOnNext(this::validate)
            .flatMap { repository.save(it) }
            .flatMap { ServerResponse.status(HttpStatus.CREATED).bodyValue(it) }.log()
    }

    private fun validate(review: Review) {
       val constraintViolations = validator.validate(review)
        logger.info("constraints violation:{} $constraintViolations")

        if (constraintViolations.size > 0) {
            val errorMessage = constraintViolations
                .stream()
                .map { it.message }
                .sorted()
                .collect(Collectors.joining(","))

            throw ReviewDataException(errorMessage)
        }
    }

    fun getReviews(request: ServerRequest): Mono<ServerResponse> {
        val movieInfoId = request.queryParam("movieInfoId")
        if (movieInfoId.isPresent) {
            val reviews = repository.findByMovieInfoId(movieInfoId.get())

            return ServerResponse.status(HttpStatus.OK).body(reviews, Review::class.java).log()
        }

        return ServerResponse.status(HttpStatus.OK).body(repository.findAll(), Review::class.java).log()
    }

    fun updateReview(request: ServerRequest): Mono<ServerResponse> {
        val reviewId = request.pathVariable("id")
        val reviewToBeUpdated = repository.findById(reviewId)
//            .switchIfEmpty(Mono.error(ReviewNotFoundException("Review not found")))

       return reviewToBeUpdated
            .flatMap { exitingReview ->
                request.bodyToMono(Review::class.java)
                    .map { newReview ->
                        exitingReview.comment = newReview.comment
                        exitingReview.rating = newReview.rating
                    }
                    .flatMap { repository.save(exitingReview) }
                    .flatMap { ServerResponse.status(HttpStatus.OK).build() }
            }
           .switchIfEmpty(ServerResponse.notFound().build())
    }

    fun deleteReview(request: ServerRequest): Mono<ServerResponse> {
        val reviewId = request.pathVariable("id")
        val existingReview = repository.findById(reviewId)

        return existingReview.flatMap { repository.deleteById(reviewId) }
            .then(ServerResponse.noContent().build())
    }


    companion object {
        private val logger = LoggerFactory.getLogger(ReviewHandler::class.java)
    }
}