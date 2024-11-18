package com.quoCard.bookManagementApp.extension

import com.quoCard.bookManagementApp.jooq.tables.records.AuthorRecord
import com.quoCard.bookManagementApp.jooq.tables.records.BookRecord
import com.quoCard.bookManagementApp.model.Author
import com.quoCard.bookManagementApp.model.Book
import com.quoCard.bookManagementApp.model.PublicationStatus

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
