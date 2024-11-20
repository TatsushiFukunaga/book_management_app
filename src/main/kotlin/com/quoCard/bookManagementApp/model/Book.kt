package com.quoCard.bookManagementApp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.util.Collections.emptyList

@Schema(description = "Book entity representing a book in the system")
data class Book(
    @Schema(description = "Unique identifier of the book", example = "1")
    @JsonProperty("id")
    val id: Long = 0,

    @Schema(description = "Title of the book", example = "Kotlin Programming")
    @field:NotBlank(message = "Book title cannot be blank")
    @JsonProperty("title")
    val title: String,

    @Schema(description = "Price of the book in USD", example = "29")
    @field:Min(value = 0, message = "Book price must be at least 0")
    @JsonProperty("price")
    val price: Int,

    @Schema(description = "Publication status of the book", example = "PUBLISHED")
    @JsonProperty("status")
    var status: PublicationStatus = PublicationStatus.UNPUBLISHED,

    @Schema(description = "List of authors who wrote the book")
    @JsonProperty("authors")
    @field:NotEmpty(message = "A book must have at least one author")
    @JsonIgnoreProperties("books")
    val authors: List<Author> = emptyList()
) {
    fun updateStatus(newStatus: PublicationStatus) {
        if (status == PublicationStatus.PUBLISHED && newStatus == PublicationStatus.UNPUBLISHED) {
            throw IllegalArgumentException("Cannot change status from PUBLISHED to UNPUBLISHED")
        }
        status = newStatus
    }
}