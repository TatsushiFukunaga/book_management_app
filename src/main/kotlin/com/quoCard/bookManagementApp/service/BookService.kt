package com.quoCard.bookManagementApp.service

import com.quoCard.bookManagementApp.model.Book
import com.quoCard.bookManagementApp.repository.BookRepository
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Validated
@Service
class BookService(
    private val bookRepository: BookRepository
) {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    fun getAllBooks(): List<Book> {
        return bookRepository.findAll()
    }

    fun getBookById(id: Long): Book {
        return bookRepository.findById(id)
            ?: throw IllegalArgumentException("Book with ID $id not found")
    }

    fun createBook(book: Book): Book {
        validateBook(book)
        return bookRepository.save(book)
    }

    fun updateBook(id: Long, updatedBook: Book): Book {
        val existingBook = getBookById(id)
        val bookToSave = existingBook.copy(
            title = updatedBook.title,
            price = updatedBook.price,
            status = updatedBook.status,
            authors = updatedBook.authors
        )
        validateBook(bookToSave)
        return bookRepository.save(bookToSave)
    }

    fun deleteBook(id: Long) {
        bookRepository.findById(id)
            ?: throw IllegalArgumentException("Book with ID $id not found")
        bookRepository.deleteById(id)
    }

    private fun validateBook(book: Book) {
        val violations = validator.validate(book)
        if (violations.isNotEmpty()) {
            val errorMessage = violations.joinToString(", ") { it.message }
            throw IllegalArgumentException("Invalid book data: $errorMessage")
        }
    }
}
