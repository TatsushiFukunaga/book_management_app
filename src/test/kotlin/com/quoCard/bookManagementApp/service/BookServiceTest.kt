package com.quoCard.bookManagementApp.service

import com.quoCard.bookManagementApp.model.dto.BookDto
import com.quoCard.bookManagementApp.model.entity.Book
import com.quoCard.bookManagementApp.model.entity.PublicationStatus
import com.quoCard.bookManagementApp.model.exception.InvalidAuthorException
import com.quoCard.bookManagementApp.model.exception.ResourceNotFoundException
import com.quoCard.bookManagementApp.repository.AuthorRepository
import com.quoCard.bookManagementApp.repository.BookAuthorRepository
import com.quoCard.bookManagementApp.repository.BookRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Collections.emptyList

internal class BookServiceTest {

    private val bookRepository: BookRepository = mock()
    private val bookAuthorRepository: BookAuthorRepository = mock()
    private val authorRepository: AuthorRepository = mock()
    private val bookService = BookService(bookRepository, bookAuthorRepository, authorRepository)

    @Test
    fun `should get all books`() {
        val books = listOf(
            Book(1L, "Book One", BigDecimal.valueOf(100.0), PublicationStatus.UNPUBLISHED, LocalDateTime.now(), LocalDateTime.now()),
            Book(2L, "Book Two", BigDecimal.valueOf(200.0), PublicationStatus.PUBLISHED, LocalDateTime.now(), LocalDateTime.now())
        )
        val authorIds = listOf(1L, 2L)

        whenever(bookRepository.findAll()).thenReturn(books)
        whenever(bookAuthorRepository.findAuthorIdsByBookId(1L)).thenReturn(authorIds)
        whenever(bookAuthorRepository.findAuthorIdsByBookId(2L)).thenReturn(emptyList())

        val responses = bookService.getAllBooks()

        assertEquals(2, responses.size)
        assertEquals("Book One", responses[0].title)
        assertEquals(authorIds, responses[0].authorIds)
        assertEquals("Book Two", responses[1].title)
        assertTrue(responses[1].authorIds.isEmpty())

        verify(bookRepository).findAll()
        verify(bookAuthorRepository, times(2)).findAuthorIdsByBookId(any())
    }

    @Test
    fun `should get book by ID`() {
        val book = Book(1L, "Book One", BigDecimal.valueOf(100.0), PublicationStatus.UNPUBLISHED, LocalDateTime.now(), LocalDateTime.now())
        val authorIds = listOf(1L, 2L)

        whenever(bookRepository.findById(1L)).thenReturn(book)
        whenever(bookAuthorRepository.findAuthorIdsByBookId(1L)).thenReturn(authorIds)

        val response = bookService.getBookById(1L)

        assertEquals("Book One", response.title)
        assertEquals(authorIds, response.authorIds)

        verify(bookRepository).findById(1L)
        verify(bookAuthorRepository).findAuthorIdsByBookId(1L)
    }

    @Test
    fun `should throw exception when book not found`() {
        whenever(bookRepository.findById(1L)).thenReturn(null)

        val exception = assertThrows<ResourceNotFoundException> {
            bookService.getBookById(1L)
        }

        assertEquals("Book with ID 1 not found", exception.message)

        verify(bookRepository).findById(1L)
        verify(bookAuthorRepository, never()).findAuthorIdsByBookId(any())
    }

    @Test
    fun `should create a new book`() {
        val bookDto = BookDto("Book One", BigDecimal.valueOf(100.0), "UNPUBLISHED", listOf(1L, 2L))
        val newBook = Book(0L, "Book One", BigDecimal.valueOf(100.0), PublicationStatus.UNPUBLISHED, LocalDateTime.now(), LocalDateTime.now())
        val savedBook = newBook.copy(id = 1L)

        whenever(bookRepository.save(any())).thenReturn(savedBook)
        whenever(authorRepository.findExistingIds()).thenReturn(listOf(1L, 2L))
        whenever(bookAuthorRepository.findAuthorIdsByBookId(1L)).thenReturn(listOf(1L, 2L))

        val response = bookService.createBook(bookDto)

        assertEquals("Book One", response.title)
        assertEquals(listOf(1L, 2L), response.authorIds)

        verify(bookRepository).save(any())
        verify(authorRepository).findExistingIds()
        verify(bookAuthorRepository).saveRelationsByBookId(1L, listOf(1L, 2L))
    }

    @Test
    fun `should throw exception when creating book with non-existent authors`() {
        val bookDto = BookDto("Book One", BigDecimal.valueOf(100.0), "UNPUBLISHED", listOf(1L, 99L))

        whenever(authorRepository.findExistingIds()).thenReturn(listOf(1L))

        val exception = assertThrows<InvalidAuthorException> {
            bookService.createBook(bookDto)
        }

        assertEquals("The following author IDs do not exist: [99]", exception.message)

        verify(authorRepository).findExistingIds()
        verify(bookRepository, never()).save(any())
    }

    @Test
    fun `should update an existing book`() {
        val existingBook = Book(1L, "Book One", BigDecimal.valueOf(100.0), PublicationStatus.UNPUBLISHED, LocalDateTime.now(), LocalDateTime.now())
        val updatedBookDto = BookDto("Updated Book", BigDecimal.valueOf(150.0), "PUBLISHED", listOf(1L))
        val savedBook = existingBook.copy(title = "Updated Book", price = BigDecimal.valueOf(150.0), status = PublicationStatus.PUBLISHED)

        whenever(bookRepository.findById(1L)).thenReturn(existingBook)
        whenever(authorRepository.findExistingIds()).thenReturn(listOf(1L))
        whenever(bookRepository.save(any())).thenReturn(savedBook)
        whenever(bookAuthorRepository.findAuthorIdsByBookId(1L)).thenReturn(listOf(1L))

        val response = bookService.updateBook(1L, updatedBookDto)

        assertEquals("Updated Book", response.title)
        assertEquals(listOf(1L), response.authorIds)

        verify(bookRepository).findById(1L)
        verify(bookRepository).save(any())
        verify(bookAuthorRepository).saveRelationsByBookId(1L, listOf(1L))
    }

    @Test
    fun `should throw exception when updating non-existent book`() {
        val updatedBookDto = BookDto("Updated Book", BigDecimal.valueOf(150.0), "PUBLISHED", listOf(1L))

        whenever(bookRepository.findById(1L)).thenReturn(null)
        whenever(authorRepository.findExistingIds()).thenReturn(listOf(1L))

        val exception = assertThrows<ResourceNotFoundException> {
            bookService.updateBook(1L, updatedBookDto)
        }

        assertEquals("Book with ID 1 not found", exception.message)

        verify(bookRepository).findById(1L)
        verify(bookRepository, never()).save(any())
    }

    @Test
    fun `should delete an existing book`() {
        val book = Book(1L, "Book One", BigDecimal.valueOf(100.0), PublicationStatus.UNPUBLISHED, LocalDateTime.now(), LocalDateTime.now())

        whenever(bookRepository.findById(1L)).thenReturn(book)

        bookService.deleteBook(1L)

        verify(bookRepository).findById(1L)
        verify(bookRepository).deleteById(1L)
    }

    @Test
    fun `should throw exception when deleting non-existent book`() {
        whenever(bookRepository.findById(1L)).thenReturn(null)

        val exception = assertThrows<ResourceNotFoundException> {
            bookService.deleteBook(1L)
        }

        assertEquals("Book with ID 1 not found", exception.message)

        verify(bookRepository).findById(1L)
        verify(bookRepository, never()).deleteById(any())
    }
}