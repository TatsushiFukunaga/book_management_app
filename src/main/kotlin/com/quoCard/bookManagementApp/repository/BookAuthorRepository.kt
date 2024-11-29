package com.quoCard.bookManagementApp.repository

import com.quoCard.bookManagementApp.jooq.Tables.BOOK_AUTHOR
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class BookAuthorRepository(private val dsl: DSLContext) {

    fun findBookIdsByAuthorId(authorId: Long): List<Long> {
        return dsl.select(BOOK_AUTHOR.BOOK_ID)
            .from(BOOK_AUTHOR)
            .where(BOOK_AUTHOR.AUTHOR_ID.eq(authorId))
            .fetch()
            .map { record -> record[BOOK_AUTHOR.BOOK_ID] }
    }

    fun findAuthorIdsByBookId(bookId: Long): List<Long> {
        return dsl.select(BOOK_AUTHOR.AUTHOR_ID)
            .from(BOOK_AUTHOR)
            .where(BOOK_AUTHOR.BOOK_ID.eq(bookId))
            .fetch()
            .map { record -> record[BOOK_AUTHOR.AUTHOR_ID] }
    }

    fun findBookIdsByAuthorIds(authorIds: List<Long>): Map<Long, List<Long>> {
        return dsl.select(BOOK_AUTHOR.AUTHOR_ID, BOOK_AUTHOR.BOOK_ID)
            .from(BOOK_AUTHOR)
            .where(BOOK_AUTHOR.AUTHOR_ID.`in`(authorIds.map { it.toInt() }))
            .fetchGroups({ it[BOOK_AUTHOR.AUTHOR_ID].toLong() }, { it[BOOK_AUTHOR.BOOK_ID].toLong() })
    }

    fun findAuthorIdsByBookIds(bookIds: List<Long>): Map<Long, List<Long>> {
        return dsl.select(BOOK_AUTHOR.BOOK_ID, BOOK_AUTHOR.AUTHOR_ID)
            .from(BOOK_AUTHOR)
            .where(BOOK_AUTHOR.BOOK_ID.`in`(bookIds.map { it.toInt() }))
            .fetchGroups({ it[BOOK_AUTHOR.BOOK_ID].toLong() }, { it[BOOK_AUTHOR.AUTHOR_ID].toLong() })
    }

    fun saveRelationsByAuthorId(authorId: Long, bookIds: List<Long>) {
        bookIds.forEach { bookId ->
            dsl.insertInto(BOOK_AUTHOR)
                .set(BOOK_AUTHOR.AUTHOR_ID, authorId)
                .set(BOOK_AUTHOR.BOOK_ID, bookId)
                .onDuplicateKeyIgnore()
                .execute()
        }
    }

    fun saveRelationsByBookId(bookId: Long, authorIds: List<Long>) {
        authorIds.forEach { authorId ->
            dsl.insertInto(BOOK_AUTHOR)
                .set(BOOK_AUTHOR.BOOK_ID, bookId)
                .set(BOOK_AUTHOR.AUTHOR_ID, authorId)
                .onDuplicateKeyIgnore()
                .execute()
        }
    }
}