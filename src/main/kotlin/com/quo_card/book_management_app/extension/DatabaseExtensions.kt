package com.quo_card.book_management_app.extension

import com.quo_card.book_management_app.jooq.tables.records.AuthorRecord
import com.quo_card.book_management_app.jooq.tables.records.BookRecord
import com.quo_card.book_management_app.model.Author
import com.quo_card.book_management_app.model.Book
import com.quo_card.book_management_app.model.PublicationStatus

fun AuthorRecord.toModel(): Author {
    return Author(
        id = this.id?.toLong() ?: 0L,
        name = this.name ?: "",
        birthDate = this.birthDate ?: throw IllegalStateException("Birthdate is null")
    )
}

fun BookRecord.toModel(): Book {
    return Book(
        id = this.id?.toLong() ?: 0L,
        title = this.title ?: "",
        price = this.price ?: 0,
        status = PublicationStatus.valueOf(this.status ?: "UNPUBLISHED")
    )
}
