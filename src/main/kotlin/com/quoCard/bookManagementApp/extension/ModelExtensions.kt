package com.quoCard.bookManagementApp.extension

import com.quoCard.bookManagementApp.jooq.tables.records.AuthorRecord
import com.quoCard.bookManagementApp.jooq.tables.records.BookRecord
import com.quoCard.bookManagementApp.model.dto.AuthorDto
import com.quoCard.bookManagementApp.model.dto.BookDto
import com.quoCard.bookManagementApp.model.entity.Author
import com.quoCard.bookManagementApp.model.entity.Book
import com.quoCard.bookManagementApp.model.entity.PublicationStatus
import com.quoCard.bookManagementApp.model.response.AuthorResponse
import com.quoCard.bookManagementApp.model.response.BookResponse
import java.time.LocalDate
import java.time.LocalDateTime

fun BookDto.toNewEntity() = Book(
    id = 0L,
    title = this.title,
    price = this.price,
    status = PublicationStatus.valueOf(this.status),
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)

fun AuthorDto.toNewEntity() = Author(
    id = 0L,
    name = this.name,
    birthDate = LocalDate.parse(this.birthDate),
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)

fun BookDto.toExistingEntity(existingEntity: Book): Book {
    val status = PublicationStatus.valueOf(this.status)
    if (existingEntity.status == PublicationStatus.PUBLISHED && status == PublicationStatus.UNPUBLISHED) {
        throw IllegalArgumentException("Cannot change status from PUBLISHED to UNPUBLISHED")
    }
    return Book(
        id = existingEntity.id,
        title = this.title,
        price = this.price,
        status = status,
        createdAt = existingEntity.createdAt,
        updatedAt = LocalDateTime.now()
    )
}

fun AuthorDto.toExistingEntity(existingEntity: Author) = Author(
        id = existingEntity.id,
        name = this.name,
        birthDate = LocalDate.parse(this.birthDate),
        createdAt = existingEntity.createdAt,
        updatedAt = LocalDateTime.now()
    )

fun Book.toResponse(authorIds: List<Long>) = BookResponse(
    id = this.id,
    title = this.title,
    price = this.price,
    status = this.status,
    authorIds = authorIds
)

fun Author.toResponse(bookIds: List<Long>) = AuthorResponse(
    id = this.id,
    name = this.name,
    birthDate = this.birthDate,
    bookIds = bookIds
)

fun AuthorRecord.toEntity() = Author(
    id = id.toLong(),
    name = name,
    birthDate = birthDate,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun BookRecord.toEntity() = Book(
    id = this.id.toLong(),
    title = this.title,
    price = this.price,
    status = this.status?.let {
        try {
            PublicationStatus.valueOf(it)
        } catch (e: IllegalArgumentException) {
            throw IllegalStateException("Invalid publication status value: $it")
        }
    } ?: PublicationStatus.UNPUBLISHED,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

