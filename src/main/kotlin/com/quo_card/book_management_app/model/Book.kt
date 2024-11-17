package com.quo_card.book_management_app.model

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Book entity representing a book in the system")
data class Book(
    @Schema(description = "Unique identifier of the book", example = "1")
    val id: Long = 0,

    @Schema(description = "Title of the book", example = "Kotlin Programming")
    @field:NotBlank(message = "Book title cannot be blank")
    val title: String,

    @Schema(description = "Price of the book in USD", example = "29")
    @field:Min(value = 0, message = "Book price must be at least 0")
    val price: Int,

    @Schema(description = "Publication status of the book", example = "PUBLISHED")
    var status: PublicationStatus = PublicationStatus.UNPUBLISHED,

    @Schema(description = "List of authors who wrote the book")
    val authors: MutableList<Author> = mutableListOf()
) {
    fun addAuthor(author: Author) {
        authors.add(author)
        author.books.add(this)
    }

    fun updateStatus(newStatus: PublicationStatus) {
        if (status == PublicationStatus.PUBLISHED && newStatus == PublicationStatus.UNPUBLISHED) {
            throw IllegalArgumentException("Cannot change status from PUBLISHED to UNPUBLISHED")
        }
        status = newStatus
    }
}