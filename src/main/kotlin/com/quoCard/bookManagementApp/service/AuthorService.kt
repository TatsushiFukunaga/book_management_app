package com.quoCard.bookManagementApp.service

import com.quoCard.bookManagementApp.model.Author
import com.quoCard.bookManagementApp.model.Book
import com.quoCard.bookManagementApp.repository.AuthorRepository
import com.quoCard.bookManagementApp.repository.BookRepository
import jakarta.validation.Valid
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Validated
@Service
class AuthorService(
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository
) {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    fun getAllAuthors(): List<Author> {
        return authorRepository.findAll()
    }

    fun getAuthorById(id: Long): Author {
        return authorRepository.findById(id)
            ?: throw IllegalArgumentException("Author with ID $id not found")
    }

    fun createAuthor(@Valid author: Author): Author {
        validateAuthor(author)
        return authorRepository.save(author)
    }

    fun updateAuthor(id: Long, @Valid updatedAuthor: Author): Author {
        val existingAuthor = getAuthorById(id)
        val authorToSave = existingAuthor.copy(
            name = updatedAuthor.name,
            birthDate = updatedAuthor.birthDate,
            books = updatedAuthor.books
        )
        validateAuthor(authorToSave)
        return authorRepository.save(authorToSave)
    }

    fun deleteAuthor(id: Long) {
        authorRepository.findById(id)
            ?: throw IllegalArgumentException("Author with ID $id not found")
        authorRepository.deleteById(id)
    }

    fun getBooksByAuthorId(authorId: Long): List<Book> {
        return bookRepository.findByAuthor(authorId)
    }

    private fun validateAuthor(author: Author) {
        val violations = validator.validate(author)
        if (violations.isNotEmpty()) {
            val errorMessage = violations.joinToString(", ") { it.message }
            throw IllegalArgumentException("Invalid author data: $errorMessage")
        }
    }
}
