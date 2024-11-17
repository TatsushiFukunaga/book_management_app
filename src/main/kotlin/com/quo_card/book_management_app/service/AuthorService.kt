package com.quo_card.book_management_app.service

import com.quo_card.book_management_app.model.Author
import com.quo_card.book_management_app.repository.AuthorRepository
import org.springframework.stereotype.Service

@Service
class AuthorService(
    private val authorRepository: AuthorRepository
) {
    fun getAllAuthors(): List<Author> {
        return authorRepository.findAll()
    }

    fun getAuthorById(id: Long): Author {
        return authorRepository.findById(id)
            ?: throw IllegalArgumentException("Author with ID $id not found")
    }

    fun createAuthor(author: Author): Author {
        return authorRepository.save(author)
    }

    fun updateAuthor(id: Long, updatedAuthor: Author): Author {
        val existingAuthor = getAuthorById(id)
        val authorToSave = existingAuthor.copy(
            name = updatedAuthor.name,
            birthDate = updatedAuthor.birthDate
        )
        return authorRepository.save(authorToSave)
    }

    fun deleteAuthor(id: Long) {
        authorRepository.deleteById(id)
    }
}
