package com.quo_card.book_management_app.controller

import com.quo_card.book_management_app.model.Book
import com.quo_card.book_management_app.service.BookService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {
    @GetMapping
    fun getAllBooks(): List<Book> {
        return bookService.getAllBooks()
    }

    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: Long): Book {
        return bookService.getBookById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@RequestBody book: Book): Book {
        return bookService.createBook(book)
    }

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Long, @RequestBody book: Book): Book {
        return bookService.updateBook(id, book)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Long) {
        bookService.deleteBook(id)
    }
}
