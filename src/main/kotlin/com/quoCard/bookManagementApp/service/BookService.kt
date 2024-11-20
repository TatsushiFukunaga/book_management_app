package com.quoCard.bookManagementApp.service

import com.quoCard.bookManagementApp.model.Book
import com.quoCard.bookManagementApp.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository
) {

    fun getAllBooks(): List<Book> {
        return bookRepository.findAll()
    }

    fun getBookById(id: Long): Book {
        return bookRepository.findById(id)
            ?: throw IllegalArgumentException("Book with ID $id not found")
    }

    fun createBook(book: Book): Book {
        if (book.authors.isEmpty()) {
            throw IllegalArgumentException("A book must have at least one author")
        }
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
        return bookRepository.save(bookToSave)
    }

    fun deleteBook(id: Long) {
        bookRepository.deleteById(id)
    }
}