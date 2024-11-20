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
class BookRepository(private val dsl: DSLContext) {

    fun findAll(): List<Book> {
        val books = dsl.selectFrom(BOOK)
            .fetchInto(BookRecord::class.java)

        return books.map { bookRecord: BookRecord ->
            val authors = dsl.select()
                .from(AUTHOR)
                .where(
                    AUTHOR.ID.`in`(
                        dsl.select(BOOK_AUTHOR.AUTHOR_ID)
                            .from(BOOK_AUTHOR)
                            .where(BOOK_AUTHOR.BOOK_ID.eq(bookRecord.id!!.toInt()))
                    )
                )
                .fetch()
                .map { record ->
                    record.into(AuthorRecord::class.java).toModel()
                }

            bookRecord.toModel(authors as List<Author>)
        }
    }

    fun findById(id: Long): Book? {
        val bookRecord = dsl.selectFrom(BOOK)
            .where(BOOK.ID.eq(id.toInt()))
            .fetchOneInto(BookRecord::class.java)

        val authors = dsl.select()
            .from(AUTHOR)
            .where(
                AUTHOR.ID.`in`(
                    dsl.select(BOOK_AUTHOR.AUTHOR_ID)
                        .from(BOOK_AUTHOR)
                        .where(BOOK_AUTHOR.BOOK_ID.eq(id.toInt()))
                )
            )
            .fetch()
            .map { record ->
                record.into(AuthorRecord::class.java).toModel()
            }

        return bookRecord?.toModel(authors)
    }

    @Transactional
    fun save(book: Book): Book {
        val record = if (book.id == 0L) {
            dsl.insertInto(BOOK)
                .set(BOOK.TITLE, book.title)
                .set(BOOK.PRICE, book.price)
                .set(BOOK.STATUS, book.status.name)
                .returning()
                .fetchOne()
                ?: throw IllegalStateException("Failed to save book ${book.title}")
        } else {
            dsl.update(BOOK)
                .set(BOOK.TITLE, book.title)
                .set(BOOK.PRICE, book.price)
                .set(BOOK.STATUS, book.status.name)
                .where(BOOK.ID.eq(book.id.toInt()))
                .returning()
                .fetchOne()
                ?: throw IllegalStateException("Failed to update book with ID ${book.id}")
        }

        val savedBook = record.into(BookRecord::class.java).toModel(emptyList())

        // BOOK_AUTHORテーブルを更新
        dsl.deleteFrom(BOOK_AUTHOR)
            .where(BOOK_AUTHOR.BOOK_ID.eq(savedBook.id.toInt()))
            .execute()

        val updatedAuthors = book.authors.map { author: Author ->
            val authorRecord = if (author.id == 0L) {
                dsl.insertInto(AUTHOR)
                    .set(AUTHOR.NAME, author.name)
                    .set(AUTHOR.BIRTH_DATE, author.birthDate)
                    .returning()
                    .fetchOne()
                    ?.into(AuthorRecord::class.java)
                    ?: throw IllegalStateException("Failed to save author ${author.name}")
            } else {
                dsl.update(AUTHOR)
                    .set(AUTHOR.NAME, author.name)
                    .set(AUTHOR.BIRTH_DATE, author.birthDate)
                    .where(AUTHOR.ID.eq(author.id.toInt()))
                    .returning()
                    .fetchOne()
                    ?.into(AuthorRecord::class.java)
                    ?: throw IllegalStateException("Failed to update author with ID ${author.id}")
            }

            // BOOK_AUTHOR テーブルに関連を登録
            dsl.insertInto(BOOK_AUTHOR)
                .set(BOOK_AUTHOR.BOOK_ID, savedBook.id.toInt())
                .set(BOOK_AUTHOR.AUTHOR_ID, authorRecord.id!!.toInt())
                .onDuplicateKeyIgnore()
                .execute()

            authorRecord.into(AuthorRecord::class.java).toModel(emptyList<Book>())
        }

        return savedBook.copy(authors = updatedAuthors)
    }

    fun deleteById(id: Long) {
        dsl.deleteFrom(BOOK_AUTHOR)
            .where(BOOK_AUTHOR.BOOK_ID.eq(id.toInt()))
            .execute()

        dsl.deleteFrom(BOOK)
            .where(BOOK.ID.eq(id.toInt()))
            .execute()
    }

    fun findByAuthor(authorId: Long): List<Book> {
        val bookRecords = dsl.selectFrom(BOOK)
            .where(
                BOOK.ID.`in`(
                    dsl.select(BOOK_AUTHOR.BOOK_ID)
                        .from(BOOK_AUTHOR)
                        .where(BOOK_AUTHOR.AUTHOR_ID.eq(authorId.toInt()))
                )
            )
            .fetchInto(BookRecord::class.java)

        return bookRecords.map { bookRecord: BookRecord ->
            val authors = dsl.select()
                .from(AUTHOR)
                .where(
                    AUTHOR.ID.`in`(
                        dsl.select(BOOK_AUTHOR.AUTHOR_ID)
                            .from(BOOK_AUTHOR)
                            .where(BOOK_AUTHOR.BOOK_ID.eq(bookRecord.id!!.toInt()))
                    )
                )
                .fetch()
                .map { record ->
                    record.into(AuthorRecord::class.java).toModel()
                }

            bookRecord.toModel(authors as List<Author>)
        }
    }
}
