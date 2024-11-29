package com.quoCard.bookManagementApp.service

import com.quoCard.bookManagementApp.extension.toExistingEntity
import com.quoCard.bookManagementApp.extension.toNewEntity
import com.quoCard.bookManagementApp.extension.toResponse
import com.quoCard.bookManagementApp.model.dto.BookDto
import com.quoCard.bookManagementApp.model.entity.Book
import com.quoCard.bookManagementApp.model.exception.InvalidAuthorException
import com.quoCard.bookManagementApp.model.exception.ResourceNotFoundException
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
class BookService(
    private val bookRepository: BookRepository,
    private val bookAuthorRepository: BookAuthorRepository,
    private val authorRepository: AuthorRepository
) {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    fun getAllBooks(): List<BookResponse> {
        val books = bookRepository.findAll()
        val bookIds = books.map { it.id }
        val authorIdsMap = bookAuthorRepository.findAuthorIdsByBookIds(bookIds)
        return books.map { book: Book ->
            val authorIds = authorIdsMap[book.id] ?: emptyList()
            book.toResponse(authorIds)
        }
    }

    fun getBookById(id: Long): BookResponse {
        val book = bookRepository.findById(id)
            ?: throw ResourceNotFoundException("Book with ID $id not found")
        val authors = bookAuthorRepository.findAuthorIdsByBookId(book.id)
        return book.toResponse(authors)
    }

    @Transactional
    fun createBook(@Valid bookDto: BookDto): BookResponse {
        validateBook(bookDto)
        val savedBook = bookRepository.save(bookDto.toNewEntity())
        bookAuthorRepository.saveRelationsByBookId(savedBook.id, bookDto.authorIds)
        val authorIds = bookAuthorRepository.findAuthorIdsByBookId(savedBook.id)
        return savedBook.toResponse(authorIds)
    }

    @Transactional
    fun updateBook(id: Long, @Valid updatedBookDto: BookDto): BookResponse {
        val existingBook = bookRepository.findById(id)
            ?: throw ResourceNotFoundException("Book with ID $id not found")
        validateBook(updatedBookDto)
        val savedBook = bookRepository.save(updatedBookDto.toExistingEntity(existingBook))
        bookAuthorRepository.saveRelationsByBookId(savedBook.id, updatedBookDto.authorIds)
        val authorIds = bookAuthorRepository.findAuthorIdsByBookId(savedBook.id)
        return savedBook.toResponse(authorIds)
    }

    fun deleteBook(id: Long) {
        bookRepository.findById(id)
            ?: throw ResourceNotFoundException("Book with ID $id not found")
        bookRepository.deleteById(id)
    }

    private fun validateBook(book: BookDto) {
        val violations = validator.validate(book)
        if (violations.isNotEmpty()) {
            val errorMessage = violations.joinToString(", ") { it.message }
            throw IllegalArgumentException("Invalid book data: $errorMessage")
        }
        val existingAuthorIds = authorRepository.findExistingIds()
        val missingAuthorIds = book.authorIds.filterNot { it in existingAuthorIds }
        if (missingAuthorIds.isNotEmpty()) {
            throw InvalidAuthorException("The following author IDs do not exist: $missingAuthorIds")
        }
    }
}
