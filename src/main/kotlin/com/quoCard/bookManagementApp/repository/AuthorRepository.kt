package com.quoCard.bookManagementApp.repository

import com.quoCard.bookManagementApp.extension.toModel
import com.quoCard.bookManagementApp.jooq.Tables.BOOK_AUTHOR
import com.quoCard.bookManagementApp.jooq.tables.Author.AUTHOR
import com.quoCard.bookManagementApp.jooq.tables.Book.BOOK
import com.quoCard.bookManagementApp.jooq.tables.records.AuthorRecord
import com.quoCard.bookManagementApp.jooq.tables.records.BookRecord
import com.quoCard.bookManagementApp.model.Author
import com.quoCard.bookManagementApp.model.Book
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Collections.emptyList

@Repository
class AuthorRepository(private val dsl: DSLContext) {

    fun findAll(): List<Author> {
        val authors = dsl.selectFrom(AUTHOR)
            .fetchInto(AuthorRecord::class.java)

        return authors.map { authorRecord: AuthorRecord ->
            val books = dsl.select()
                .from(BOOK)
                .where(BOOK.ID.`in`(
                    dsl.select(BOOK_AUTHOR.BOOK_ID)
                        .from(BOOK_AUTHOR)
                        .where(BOOK_AUTHOR.AUTHOR_ID.eq(authorRecord.id!!.toInt()))
                ))
                .fetch()
                .map { record ->
                    record.into(BookRecord::class.java).toModel()
                }

            authorRecord.toModel(books as List<Book>)
        }
    }

    fun findById(id: Long): Author? {
        val authorRecord = dsl.selectFrom(AUTHOR)
            .where(AUTHOR.ID.eq(id.toInt()))
            .fetchOneInto(AuthorRecord::class.java)

        val books = dsl.select()
            .from(BOOK)
            .where(BOOK.ID.`in`(
                dsl.select(BOOK_AUTHOR.BOOK_ID)
                    .from(BOOK_AUTHOR)
                    .where(BOOK_AUTHOR.AUTHOR_ID.eq(id.toInt()))
            ))
            .fetch()
            .map { record ->
                record.into(BookRecord::class.java).toModel()
            }

        return authorRecord?.toModel(books)
    }

    @Transactional
    fun save(author: Author): Author {
        val record = if (author.id == 0L) {
            dsl.insertInto(AUTHOR)
                .set(AUTHOR.NAME, author.name)
                .set(AUTHOR.BIRTH_DATE, author.birthDate)
                .returning()
                .fetchOne()
                ?: throw IllegalStateException("Failed to save author ${author.name}")
        } else {
            dsl.update(AUTHOR)
                .set(AUTHOR.NAME, author.name)
                .set(AUTHOR.BIRTH_DATE, author.birthDate)
                .where(AUTHOR.ID.eq(author.id!!.toInt()))
                .returning()
                .fetchOne()
                ?: throw IllegalStateException("Failed to update author with ID ${author.id}")
        }

        val savedAuthor = record.into(AuthorRecord::class.java).toModel(emptyList())

        // BOOK_AUTHORテーブルを更新
        dsl.deleteFrom(BOOK_AUTHOR)
            .where(BOOK_AUTHOR.AUTHOR_ID.eq(savedAuthor.id.toInt()))
            .execute()

        val updatedBooks = author.books.map { book: Book ->
            val bookRecord = if (book.id == 0L) {
                dsl.insertInto(BOOK)
                    .set(BOOK.TITLE, book.title)
                    .set(BOOK.PRICE, book.price)
                    .set(BOOK.STATUS, book.status.name)
                    .returning()
                    .fetchOne()
                    ?.into(BookRecord::class.java)
                    ?: throw IllegalStateException("Failed to save book ${book.title}")
            } else {
                dsl.update(BOOK)
                    .set(BOOK.TITLE, book.title)
                    .set(BOOK.PRICE, book.price)
                    .set(BOOK.STATUS, book.status.name)
                    .where(BOOK.ID.eq(book.id.toInt()))
                    .returning()
                    .fetchOne()
                    ?.into(BookRecord::class.java)
                    ?: throw IllegalStateException("Failed to update book with ID ${book.id}")
            }

            // BOOK_AUTHOR テーブルに関連を登録
            dsl.insertInto(BOOK_AUTHOR)
                .set(BOOK_AUTHOR.AUTHOR_ID, savedAuthor.id.toInt())
                .set(BOOK_AUTHOR.BOOK_ID, bookRecord.id!!.toInt())
                .onDuplicateKeyIgnore()
                .execute()

            bookRecord.into(BookRecord::class.java).toModel(emptyList<Author>())
        }

        return savedAuthor.copy(books = updatedBooks as List<Book>)
    }

    fun deleteById(id: Long) {
        dsl.deleteFrom(BOOK_AUTHOR)
            .where(BOOK_AUTHOR.AUTHOR_ID.eq(id.toInt()))
            .execute()

        dsl.deleteFrom(AUTHOR)
            .where(AUTHOR.ID.eq(id.toInt()))
            .execute()
    }
}