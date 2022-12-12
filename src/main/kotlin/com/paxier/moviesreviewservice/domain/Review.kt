package com.paxier.moviesreviewservice.domain

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
data class Review(
    @Id val id: String,
    @field:NotNull(message = "Movie Info Id is required")
    val movieInfoId: String,
    var comment: String,
    @field:Min(value = 0, message = "Rating should be positive number")var rating: Double
)