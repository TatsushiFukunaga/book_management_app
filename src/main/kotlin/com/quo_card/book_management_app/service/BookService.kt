package com.quo_card.book_management_app.service

import com.quo_card.book_management_app.model.Book
import com.quo_card.book_management_app.repository.BookRepository
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
        return bookRepository.save(book)
    }

    fun updateBook(id: Long, updatedBook: Book): Book {
        val existingBook = getBookById(id)
        val bookToSave = existingBook.copy(
            title = updatedBook.title,
            price = updatedBook.price,
            status = updatedBook.status
        )
        return bookRepository.save(bookToSave)
    }

    fun deleteBook(id: Long) {
        bookRepository.deleteById(id)
    }
}
