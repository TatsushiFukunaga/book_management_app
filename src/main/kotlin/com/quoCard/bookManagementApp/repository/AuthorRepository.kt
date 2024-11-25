package com.quoCard.bookManagementApp.repository

import com.quoCard.bookManagementApp.extension.toEntity
import com.quoCard.bookManagementApp.jooq.tables.Author.AUTHOR
import com.quoCard.bookManagementApp.jooq.tables.records.AuthorRecord
import com.quoCard.bookManagementApp.model.entity.Author
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class AuthorRepository(private val dsl: DSLContext) {

    fun findAll(): List<Author> {
        return dsl.selectFrom(AUTHOR)
            .fetchInto(AuthorRecord::class.java)
            .map { it.toEntity() }
        }

    fun findById(id: Long): Author? {
        return dsl.selectFrom(AUTHOR)
            .where(AUTHOR.ID.eq(id.toInt()))
            .fetchOneInto(AuthorRecord::class.java)
            ?.toEntity()
    }

    fun findExistingIds(): List<Long> {
        return dsl.selectFrom(AUTHOR)
            .fetchInto(AuthorRecord::class.java)
            .map { it.toEntity().id }
    }

    @Transactional
    fun save(author: Author): Author {
        if (author.id == 0L) {
            return dsl.insertInto(AUTHOR)
                .set(AUTHOR.NAME, author.name)
                .set(AUTHOR.BIRTH_DATE, author.birthDate)
                .returning()
                .fetchOneInto(AuthorRecord::class.java)
                ?.toEntity()
                ?: throw IllegalStateException("Failed to save author ${author.name}")
        } else {
            return dsl.update(AUTHOR)
                .set(AUTHOR.NAME, author.name)
                .set(AUTHOR.BIRTH_DATE, author.birthDate)
                .where(AUTHOR.ID.eq(author.id.toInt()))
                .returning()
                .fetchOneInto(AuthorRecord::class.java)
                ?.toEntity()
                ?: throw IllegalStateException("Failed to update author with ID ${author.id}")
        }
    }

    fun deleteById(id: Long) {
        dsl.deleteFrom(AUTHOR)
            .where(AUTHOR.ID.eq(id.toInt()))
            .execute()
    }
}
