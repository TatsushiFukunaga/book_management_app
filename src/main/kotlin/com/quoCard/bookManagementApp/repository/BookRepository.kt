package com.quoCard.bookManagementApp.repository

import com.quoCard.bookManagementApp.extension.toModel
import com.quoCard.bookManagementApp.jooq.tables.Book.BOOK
import com.quoCard.bookManagementApp.jooq.tables.records.BookRecord
import com.quoCard.bookManagementApp.model.Book
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class BookRepository(private val dsl: DSLContext) {

    fun findAll(): List<Book> {
        return dsl.selectFrom(BOOK)
            .fetchInto(BookRecord::class.java)
            .map { it.toModel() }
    }

    fun findById(id: Long): Book? {
        return dsl.selectFrom(BOOK)
            .where(BOOK.ID.eq(id.toInt()))
            .fetchOneInto(BookRecord::class.java)
            ?.toModel()
    }
git 
    fun save(book: Book): Book {
        val record = dsl.insertInto(BOOK)
            .set(BOOK.TITLE, book.title)
            .set(BOOK.PRICE, book.price)
            .set(BOOK.STATUS, book.status.name)
            .returning()
            .fetchOne()

        return record?.toModel() ?: throw IllegalStateException("Insert operation failed")
    }

    fun deleteById(id: Long) {
        dsl.deleteFrom(BOOK)
            .where(BOOK.ID.eq(id.toInt()))
            .execute()
    }
}
