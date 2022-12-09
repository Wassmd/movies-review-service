package com.paxier.moviesreviewservice.handler

import com.paxier.moviesreviewservice.domain.Review
import com.paxier.moviesreviewservice.repository.ReviewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class ReviewHandler(@Autowired val repository: ReviewRepository) {

    fun addReview(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(Review::class.java)
            .flatMap { repository.save(it) }
            .flatMap { ServerResponse.status(HttpStatus.CREATED).bodyValue(it) }.log()
    }

    fun getReviews(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.status(HttpStatus.OK).body(repository.findAll(), Review::class.java).log()
    }

    fun updateReview(request: ServerRequest): Mono<ServerResponse> {
        val reviewId = request.pathVariable("id")
        val reviewToBeUpdated = repository.findById(reviewId)

       return reviewToBeUpdated
            .flatMap { exitingReview ->
                request.bodyToMono(Review::class.java)
                    .map { newReview ->
                        exitingReview.comment = newReview.comment
                        exitingReview.rating = newReview.rating
                    }
                    .flatMap { repository.save(exitingReview) }
            }
           .flatMap { ServerResponse.status(HttpStatus.OK).build() }
    }
}