package com.paxier.moviesreviewservice.repository

import com.paxier.moviesreviewservice.domain.Review
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ReviewRepository: ReactiveMongoRepository<Review, String> {
}