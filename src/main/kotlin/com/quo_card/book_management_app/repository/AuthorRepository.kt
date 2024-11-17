package com.quo_card.book_management_app.repository

import com.quo_card.book_management_app.extension.toModel
import com.quo_card.book_management_app.jooq.tables.Author.AUTHOR
import com.quo_card.book_management_app.jooq.tables.records.AuthorRecord
import com.quo_card.book_management_app.model.Author
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class AuthorRepository(private val dsl: DSLContext) {

    fun findAll(): List<Author> {
        return dsl.selectFrom(AUTHOR)
            .fetchInto(AuthorRecord::class.java)
            .map { it.toModel() }
    }

    fun findById(id: Long): Author? {
        return dsl.selectFrom(AUTHOR)
            .where(AUTHOR.ID.eq(id.toInt()))
            .fetchOneInto(AuthorRecord::class.java)
            ?.toModel()
    }

    fun save(author: Author): Author {
        val record = dsl.insertInto(AUTHOR)
            .set(AUTHOR.NAME, author.name)
            .set(AUTHOR.BIRTH_DATE, author.birthDate)
            .returning()
            .fetchOne()
        return record!!.toModel()
    }

    fun deleteById(id: Long) {
        dsl.deleteFrom(AUTHOR)
            .where(AUTHOR.ID.eq(id.toInt()))
            .execute()
    }
}
