package com.paxier.moviesreviewservice.domain

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
data class Review(
    @Id val id: String,
    val movieInfoId: Long,
    var comment: String,
    var rating: Double
)