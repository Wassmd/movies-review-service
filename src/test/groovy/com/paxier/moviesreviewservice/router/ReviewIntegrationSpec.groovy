package com.paxier.moviesreviewservice.router

import com.paxier.moviesreviewservice.domain.Review
import com.paxier.moviesreviewservice.repository.ReviewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "360000")
@ActiveProfiles("test")
class ReviewIntegrationSpec extends Specification {
    @Autowired
    ReviewRepository reviewRepository

    @Autowired
    WebTestClient webTestClient

    def setup() {
        def reviews = List.of(
                new Review("1",1L, "Fantastic", 4.5),
                new Review("2",2L, "Disappointing", 1),
                new Review("3",3L, "Average", 2.5),
        )

        reviewRepository.saveAll(reviews).blockLast()
    }

    def cleanup() {
        reviewRepository.deleteAll().block( )
    }

    def 'Add movie review'() {
        given: "A movie Review"
        def review = new Review("4",1L, "Awesome", 5)
        when: "add movie review endpoint is called"
        def webClientResponse = webTestClient
                .post()
                .uri("/reviews")
                .bodyValue(review)
                .exchange()

        then: "the status code of the response is created 201 "
        webClientResponse.expectStatus().isCreated()
        webClientResponse.expectBody(Review.class)
    }
}
