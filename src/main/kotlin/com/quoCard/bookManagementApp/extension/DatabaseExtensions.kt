package com.quoCard.bookManagementApp.extension

import com.quoCard.bookManagementApp.jooq.tables.records.AuthorRecord
import com.quoCard.bookManagementApp.jooq.tables.records.BookRecord
import com.quoCard.bookManagementApp.model.Author
import com.quoCard.bookManagementApp.model.Book
import com.quoCard.bookManagementApp.model.PublicationStatus
import java.util.Collections.emptyList

fun AuthorRecord.toModel(books: List<Book> = emptyList()): Author {
    return Author(
        id = this.id?.toLong() ?: throw IllegalStateException("Author ID is null"),
        name = this.name ?: throw IllegalStateException("Author name is null"),
        birthDate = this.birthDate ?: throw IllegalStateException("Author birthDate is null"),
        books = books
    )
}

fun BookRecord.toModel(authors: List<Author> = emptyList()): Book {
    return Book(
        id = this.id?.toLong() ?: throw IllegalStateException("Book ID is null"),
        title = this.title ?: throw IllegalStateException("Book title is null"),
        price = this.price ?: 0,
        status = this.status?.let {
            try {
                PublicationStatus.valueOf(it)
            } catch (e: IllegalArgumentException) {
                throw IllegalStateException("Invalid publication status value: $it")
            }
        } ?: PublicationStatus.UNPUBLISHED,
        authors = authors
    )
}
