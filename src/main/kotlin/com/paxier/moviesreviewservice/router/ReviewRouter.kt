package com.paxier.moviesreviewservice.router

import com.paxier.moviesreviewservice.handler.ReviewHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ReviewRouter {

    @Bean
    fun reviewsRoute(reviewHandler: ReviewHandler): RouterFunction<ServerResponse> {
        return route()
            .GET("/hello-world") { _ -> ServerResponse.ok().bodyValue("Hello-World") }
            .POST("/reviews") { reviewHandler.addReview(it)}
            .GET("/reviews") { reviewHandler.getReviews(it) }
            .PUT("/reviews/{id}") { reviewHandler.updateReview(it) }
            .build()
    }
}