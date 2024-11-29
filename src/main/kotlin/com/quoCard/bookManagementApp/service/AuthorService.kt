package com.quoCard.bookManagementApp.service

import com.quoCard.bookManagementApp.extension.toExistingEntity
import com.quoCard.bookManagementApp.extension.toNewEntity
import com.quoCard.bookManagementApp.extension.toResponse
import com.quoCard.bookManagementApp.model.dto.AuthorDto
import com.quoCard.bookManagementApp.model.entity.Author
import com.quoCard.bookManagementApp.model.entity.Book
import com.quoCard.bookManagementApp.model.exception.InvalidAuthorException
import com.quoCard.bookManagementApp.model.exception.ResourceNotFoundException
import com.quoCard.bookManagementApp.model.response.AuthorResponse
import com.quoCard.bookManagementApp.model.response.BookResponse
import com.quoCard.bookManagementApp.repository.AuthorRepository
import com.quoCard.bookManagementApp.repository.BookAuthorRepository
import com.quoCard.bookManagementApp.repository.BookRepository
import jakarta.validation.Valid
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.util.Collections.emptyList

@Validated
@Service
class AuthorService(
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository,
    private val bookAuthorRepository: BookAuthorRepository
) {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    fun getAllAuthors(): List<AuthorResponse> {
        val authors = authorRepository.findAll()
        val authorIds = authors.map { it.id }
        val bookIdsMap = bookAuthorRepository.findBookIdsByAuthorIds(authorIds)
        return authors.map { author: Author ->
            val bookIds = bookIdsMap[author.id] ?: emptyList()
            author.toResponse(bookIds)
        }
    }

    fun getAuthorById(id: Long): AuthorResponse {
        val author = authorRepository.findById(id)
            ?: throw ResourceNotFoundException("Author with ID $id not found")
        val bookIds = bookAuthorRepository.findBookIdsByAuthorId(author.id)
        return author.toResponse(bookIds)
    }

    fun getBooksByAuthorId(authorId: Long): List<BookResponse> {
        val bookIds = bookAuthorRepository.findBookIdsByAuthorId(authorId)
        val books = bookRepository.findByIds(bookIds)
        return books.map { book: Book ->
            val authorIds = bookAuthorRepository.findAuthorIdsByBookId(book.id)
            book.toResponse(authorIds)
        }
    }

    @Transactional
    fun createAuthor(@Valid authorDto: AuthorDto): AuthorResponse {
        validateAuthor(authorDto)
        val savedAuthor = authorRepository.save(authorDto.toNewEntity())
        if (authorDto.bookIds.isNotEmpty()) {
            bookAuthorRepository.saveRelationsByAuthorId(savedAuthor.id, authorDto.bookIds)
        }
        val bookIds = bookAuthorRepository.findBookIdsByAuthorId(savedAuthor.id)
        return savedAuthor.toResponse(bookIds)
    }

    @Transactional
    fun updateAuthor(id: Long, @Valid updatedAuthorDto: AuthorDto): AuthorResponse {
        val existingAuthor = authorRepository.findById(id)
            ?: throw ResourceNotFoundException("Author with ID $id not found")
        validateAuthor(updatedAuthorDto)
        val savedAuthor = authorRepository.save(updatedAuthorDto.toExistingEntity(existingAuthor))
        if (updatedAuthorDto.bookIds.isNotEmpty()) {
            bookAuthorRepository.saveRelationsByAuthorId(savedAuthor.id, updatedAuthorDto.bookIds)
        }
        val bookIds = bookAuthorRepository.findBookIdsByAuthorId(savedAuthor.id)
        return savedAuthor.toResponse(bookIds)
    }

    fun deleteAuthor(id: Long) {
        authorRepository.findById(id)
            ?: throw ResourceNotFoundException("Author with ID $id not found")
        authorRepository.deleteById(id)
    }

    private fun validateAuthor(author: AuthorDto) {
        val violations = validator.validate(author)
        if (violations.isNotEmpty()) {
            val errorMessage = violations.joinToString(", ") { it.message }
            throw IllegalArgumentException("Invalid author data: $errorMessage")
        }
        val existingBookIds = bookRepository.findExistingIds()
        val missingBookIds = author.bookIds.filterNot { it in existingBookIds }
        if (missingBookIds.isNotEmpty()) {
            throw InvalidAuthorException("The following book IDs do not exist: $missingBookIds")
        }
    }
}
