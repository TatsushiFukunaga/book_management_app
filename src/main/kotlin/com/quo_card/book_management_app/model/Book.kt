package com.quo_card.book_management_app.model

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class Book(
    val id: Long = 0,

    @field:NotBlank(message = "Book title cannot be blank")
    val title: String,

    @field:Min(value = 0, message = "Book price must be at least 0")
    val price: Int,

    var status: PublicationStatus = PublicationStatus.UNPUBLISHED,
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
