package com.quoCard.bookManagementApp.service

import com.quoCard.bookManagementApp.model.Author
import com.quoCard.bookManagementApp.model.Book
import com.quoCard.bookManagementApp.model.PublicationStatus
import com.quoCard.bookManagementApp.repository.BookRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.util.Collections.emptyList

@SpringBootTest
internal class BookServiceTest {

    private val bookRepository: BookRepository = Mockito.mock(BookRepository::class.java)
    private val bookService = BookService(bookRepository)

    private val defaultAuthor = Author(
        id = 1,
        name = "John Doe",
        birthDate = LocalDate.of(1980, 1, 1),
        books = emptyList()
    )

    @Test
    fun `getAllBooks should return all books`() {
        val books = listOf(
            Book(1, "Book One", 10, PublicationStatus.PUBLISHED, authors = listOf(defaultAuthor)),
            Book(2, "Book Two", 20, PublicationStatus.UNPUBLISHED, authors = listOf(defaultAuthor))
        )
        Mockito.`when`(bookRepository.findAll()).thenReturn(books)

        val result = bookService.getAllBooks()

        assertEquals(2, result.size)
        assertEquals(books, result)
    }

    @Test
    fun `getBookById should return book when it exists`() {
        val book = Book(1, "Book One", 10, PublicationStatus.PUBLISHED, authors = listOf(defaultAuthor))
        Mockito.`when`(bookRepository.findById(1)).thenReturn(book)

        val result = bookService.getBookById(1)

        assertEquals(book, result)
    }

    @Test
    fun `getBookById should throw exception when book does not exist`() {
        Mockito.`when`(bookRepository.findById(1)).thenReturn(null)

        val exception = assertThrows<IllegalArgumentException> {
            bookService.getBookById(1)
        }
        assertEquals("Book with ID 1 not found", exception.message)
    }

    @Test
    fun `createBook should save and return the book`() {
        val book = Book(
            title = "New Book",
            price = 15,
            status = PublicationStatus.PUBLISHED,
            authors = listOf(defaultAuthor)
        )
        val savedBook = book.copy(id = 1)
        Mockito.`when`(bookRepository.save(book)).thenReturn(savedBook)

        val result = bookService.createBook(book)

        assertEquals(savedBook, result)
    }

    @Test
    fun `createBook should throw exception for invalid book data`() {
        val invalidBook = Book(
            title = "",
            price = -1,
            status = PublicationStatus.UNPUBLISHED,
            authors = emptyList()
        )

        val exception = assertThrows<IllegalArgumentException> {
            bookService.createBook(invalidBook)
        }

        assertTrue(exception.message!!.contains("Book title cannot be blank"))
        assertTrue(exception.message!!.contains("Book price must be at least 0"))
        assertTrue(exception.message!!.contains("A book must have at least one author"))
    }

    @Test
    fun `updateBook should update and return the book`() {
        val existingBook = Book(
            id = 1,
            title = "Old Book",
            price = 10,
            status = PublicationStatus.UNPUBLISHED,
            authors = listOf(defaultAuthor)
        )
        val updatedBook = Book(
            title = "Updated Book",
            price = 20,
            status = PublicationStatus.PUBLISHED,
            authors = listOf(defaultAuthor)
        )
        val savedBook = existingBook.copy(
            title = "Updated Book",
            price = 20,
            status = PublicationStatus.PUBLISHED
        )

        Mockito.`when`(bookRepository.findById(1)).thenReturn(existingBook)
        Mockito.`when`(bookRepository.save(savedBook)).thenReturn(savedBook)

        val result = bookService.updateBook(1, updatedBook)

        assertEquals(savedBook, result)
    }

    @Test
    fun `updateBook should throw exception when book does not exist`() {
        val updatedBook = Book(
            title = "Updated Book",
            price = 20,
            status = PublicationStatus.PUBLISHED,
            authors = listOf(defaultAuthor)
        )
        Mockito.`when`(bookRepository.findById(1)).thenReturn(null)

        val exception = assertThrows<IllegalArgumentException> {
            bookService.updateBook(1, updatedBook)
        }
        assertEquals("Book with ID 1 not found", exception.message)
    }

    @Test
    fun `deleteBook should delete the book when it exists`() {
        val book = Book(
            id = 1,
            title = "Book One",
            price = 10,
            status = PublicationStatus.PUBLISHED,
            authors = listOf(defaultAuthor)
        )
        Mockito.`when`(bookRepository.findById(1)).thenReturn(book)

        bookService.deleteBook(1)

        Mockito.verify(bookRepository).deleteById(1)
    }

    @Test
    fun `deleteBook should throw exception when book does not exist`() {
        Mockito.`when`(bookRepository.findById(1)).thenReturn(null)

        val exception = assertThrows<IllegalArgumentException> {
            bookService.deleteBook(1)
        }
        assertEquals("Book with ID 1 not found", exception.message)
    }
}
