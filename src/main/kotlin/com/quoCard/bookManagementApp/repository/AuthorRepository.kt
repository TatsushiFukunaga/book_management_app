package com.quoCard.bookManagementApp.repository

import com.quoCard.bookManagementApp.extension.toModel
import com.quoCard.bookManagementApp.jooq.tables.Author.AUTHOR
import com.quoCard.bookManagementApp.jooq.tables.records.AuthorRecord
import com.quoCard.bookManagementApp.model.Author
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
