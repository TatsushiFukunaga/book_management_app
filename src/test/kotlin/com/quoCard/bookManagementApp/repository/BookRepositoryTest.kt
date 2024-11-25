package com.quoCard.bookManagementApp.repository

import com.quoCard.bookManagementApp.model.entity.Book
import com.quoCard.bookManagementApp.model.entity.PublicationStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
@Transactional
@ActiveProfiles("test")
internal class BookRepositoryTest {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Test
    fun `should save a new book`() {
        val book = Book(
            id = 0L,
            title = "Effective Java",
            price = BigDecimal("49.99"),
            status = PublicationStatus.PUBLISHED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedBook = bookRepository.save(book)
        assertNotNull(savedBook.id)
        assertEquals("Effective Java", savedBook.title)
        assertEquals(BigDecimal("49.99"), savedBook.price)
        assertEquals(PublicationStatus.PUBLISHED, savedBook.status)
    }

    @Test
    fun `should update an existing book`() {
        val book = Book(
            id = 0L,
            title = "Effective Java",
            price = BigDecimal("49.99"),
            status = PublicationStatus.PUBLISHED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedBook = bookRepository.save(book)
        val updatedBook = savedBook.copy(title = "Updated Java", price = BigDecimal("59.99"))

        val result = bookRepository.save(updatedBook)
        assertEquals(savedBook.id, result.id)
        assertEquals("Updated Java", result.title)
        assertEquals(BigDecimal("59.99"), result.price)
    }

    @Test
    fun `should find all books`() {
        val book1 = Book(
            id = 0L,
            title = "Effective Java",
            price = BigDecimal("49.99"),
            status = PublicationStatus.PUBLISHED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val book2 = Book(
            id = 0L,
            title = "Clean Code",
            price = BigDecimal("39.99"),
            status = PublicationStatus.UNPUBLISHED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        bookRepository.save(book1)
        bookRepository.save(book2)

        val books = bookRepository.findAll()
        assertEquals(2, books.size)
    }

    @Test
    fun `should find a book by ID`() {
        val book = Book(
            id = 0L,
            title = "Effective Java",
            price = BigDecimal("49.99"),
            status = PublicationStatus.PUBLISHED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedBook = bookRepository.save(book)
        val foundBook = bookRepository.findById(savedBook.id)

        assertNotNull(foundBook)
        assertEquals(savedBook.id, foundBook?.id)
        assertEquals("Effective Java", foundBook?.title)
    }

    @Test
    fun `should find books by IDs`() {
        val book1 = Book(
            id = 0L,
            title = "Effective Java",
            price = BigDecimal("49.99"),
            status = PublicationStatus.PUBLISHED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val book2 = Book(
            id = 0L,
            title = "Clean Code",
            price = BigDecimal("39.99"),
            status = PublicationStatus.UNPUBLISHED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedBook1 = bookRepository.save(book1)
        val savedBook2 = bookRepository.save(book2)

        val foundBooks = bookRepository.findByIds(listOf(savedBook1.id, savedBook2.id))

        assertEquals(2, foundBooks.size)
        assertTrue(foundBooks.any { it.title == "Effective Java" })
        assertTrue(foundBooks.any { it.title == "Clean Code" })
    }

    @Test
    fun `should delete a book by ID`() {
        val book = Book(
            id = 0L,
            title = "Effective Java",
            price = BigDecimal("49.99"),
            status = PublicationStatus.PUBLISHED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedBook = bookRepository.save(book)
        bookRepository.deleteById(savedBook.id)

        val foundBook = bookRepository.findById(savedBook.id)
        assertNull(foundBook)
    }

    @Test
    fun `should find all existing IDs`() {
        val book1 = Book(
            id = 0L,
            title = "Effective Java",
            price = BigDecimal("49.99"),
            status = PublicationStatus.PUBLISHED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val book2 = Book(
            id = 0L,
            title = "Clean Code",
            price = BigDecimal("39.99"),
            status = PublicationStatus.UNPUBLISHED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        bookRepository.save(book1)
        bookRepository.save(book2)

        val existingIds = bookRepository.findExistingIds()
        assertEquals(2, existingIds.size)
    }
}
