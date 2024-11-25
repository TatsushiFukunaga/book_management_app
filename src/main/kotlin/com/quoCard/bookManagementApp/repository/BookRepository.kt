package com.quoCard.bookManagementApp.repository

import com.quoCard.bookManagementApp.extension.toEntity
import com.quoCard.bookManagementApp.jooq.tables.Book.BOOK
import com.quoCard.bookManagementApp.jooq.tables.records.BookRecord
import com.quoCard.bookManagementApp.model.entity.Book
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class BookRepository(private val dsl: DSLContext) {

    fun findAll(): List<Book> {
        return dsl.selectFrom(BOOK)
            .fetchInto(BookRecord::class.java)
            .map { it.toEntity() }
    }

    fun findById(id: Long): Book? {
        return dsl.selectFrom(BOOK)
            .where(BOOK.ID.eq(id.toInt()))
            .fetchOneInto(BookRecord::class.java)
            ?.toEntity()
    }

    fun findByIds(bookIds: List<Long>): List<Book> {
        return dsl.selectFrom(BOOK)
            .where(BOOK.ID.`in`(bookIds.map { it.toInt() }))
            .fetchInto(BookRecord::class.java)
            .map { it.toEntity() }
    }

    fun findExistingIds(): List<Long> {
        return dsl.selectFrom(BOOK)
            .fetchInto(BookRecord::class.java)
            .map { it.toEntity().id }
    }

    @Transactional
    fun save(book: Book): Book {
        if (book.id == 0L) {
            return dsl.insertInto(BOOK)
                .set(BOOK.TITLE, book.title)
                .set(BOOK.PRICE, book.price)
                .set(BOOK.STATUS, book.status.name)
                .returning()
                .fetchOneInto(BookRecord::class.java)
                ?.toEntity()
                ?: throw IllegalStateException("Failed to save book ${book.title}")
        } else {
            return dsl.update(BOOK)
                .set(BOOK.TITLE, book.title)
                .set(BOOK.PRICE, book.price)
                .set(BOOK.STATUS, book.status.name)
                .where(BOOK.ID.eq(book.id.toInt()))
                .returning()
                .fetchOneInto(BookRecord::class.java)
                ?.toEntity()
                ?: throw IllegalStateException("Failed to update book with ID ${book.id}")
        }
    }

    fun deleteById(id: Long) {
        dsl.deleteFrom(BOOK)
            .where(BOOK.ID.eq(id.toInt()))
            .execute()
    }
}
