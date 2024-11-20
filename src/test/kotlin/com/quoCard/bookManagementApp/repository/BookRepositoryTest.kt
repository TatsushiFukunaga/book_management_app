package com.quoCard.bookManagementApp.repository

import com.quoCard.bookManagementApp.model.Author
import com.quoCard.bookManagementApp.model.Book
import com.quoCard.bookManagementApp.model.PublicationStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @BeforeEach
    fun setUp() {
        authorRepository.findAll().forEach { authorRepository.deleteById(it.id) }
        bookRepository.findAll().forEach { bookRepository.deleteById(it.id) }
    }

    @Test
    fun `save new book with authors`() {
        val authors = listOf(
            Author(
                id = 0,
                name = "Author 1",
                birthDate = LocalDate.of(1980, 1, 1),
                books = emptyList()
            ),
            Author(
                id = 0,
                name = "Author 2",
                birthDate = LocalDate.of(1990, 5, 15),
                books = emptyList()
            )
        )

        val book = Book(
            id = 0,
            title = "Test Book",
            price = 2000,
            status = PublicationStatus.PUBLISHED,
            authors = authors
        )

        val savedBook = bookRepository.save(book)

        assertNotNull(savedBook.id)
        assertEquals("Test Book", savedBook.title)
        assertEquals(2000, savedBook.price)
        assertEquals(PublicationStatus.PUBLISHED, savedBook.status)
        assertEquals(2, savedBook.authors.size)
        assertEquals("Author 1", savedBook.authors[0].name)
        assertEquals("Author 2", savedBook.authors[1].name)
    }

    @Test
    fun `update existing book`() {
        val author = Author(
            id = 0,
            name = "Initial Author",
            birthDate = LocalDate.of(1985, 3, 10),
            books = emptyList()
        )

        val savedAuthor = authorRepository.save(author)

        val book = Book(
            id = 0,
            title = "Initial Title",
            price = 1500,
            status = PublicationStatus.UNPUBLISHED,
            authors = listOf(savedAuthor)
        )

        val savedBook = bookRepository.save(book)

        val updatedBook = savedBook.copy(
            title = "Updated Title",
            price = 2500,
            status = PublicationStatus.PUBLISHED
        )

        val result = bookRepository.save(updatedBook)

        assertEquals(savedBook.id, result.id)
        assertEquals("Updated Title", result.title)
        assertEquals(2500, result.price)
        assertEquals(PublicationStatus.PUBLISHED, result.status)
        assertEquals(1, result.authors.size)
        assertEquals("Initial Author", result.authors[0].name)
    }

    @Test
    fun `find book by ID`() {
        val book = Book(
            id = 0,
            title = "Find Me",
            price = 1000,
            status = PublicationStatus.UNPUBLISHED,
            authors = emptyList()
        )

        val savedBook = bookRepository.save(book)

        val foundBook = bookRepository.findById(savedBook.id)

        assertNotNull(foundBook)
        assertEquals(savedBook.id, foundBook!!.id)
        assertEquals("Find Me", foundBook.title)
    }

    @Test
    fun `delete book by ID`() {
        val book = Book(
            id = 0,
            title = "To Be Deleted",
            price = 3000,
            status = PublicationStatus.UNPUBLISHED,
            authors = emptyList()
        )

        val savedBook = bookRepository.save(book)

        assertNotNull(bookRepository.findById(savedBook.id))

        bookRepository.deleteById(savedBook.id)

        assertNull(bookRepository.findById(savedBook.id))
    }

    @Test
    fun `find books by author`() {
        val author = Author(
            id = 0,
            name = "Author for Books",
            birthDate = LocalDate.of(1970, 1, 1),
            books = emptyList()
        )

        val savedAuthor = authorRepository.save(author)

        val books = listOf(
            Book(
                id = 0,
                title = "Book 1",
                price = 1200,
                status = PublicationStatus.PUBLISHED,
                authors = listOf(savedAuthor)
            ),
            Book(
                id = 0,
                title = "Book 2",
                price = 1400,
                status = PublicationStatus.UNPUBLISHED,
                authors = listOf(savedAuthor)
            )
        )

        books.forEach { bookRepository.save(it) }

        val foundBooks = bookRepository.findByAuthor(savedAuthor.id)

        assertEquals(2, foundBooks.size)
        assertEquals("Book 1", foundBooks[0].title)
        assertEquals("Book 2", foundBooks[1].title)
    }

    @Test
    fun `find all books`() {
        val books = listOf(
            Book(
                id = 0,
                title = "Book 1",
                price = 1000,
                status = PublicationStatus.PUBLISHED,
                authors = emptyList()
            ),
            Book(
                id = 0,
                title = "Book 2",
                price = 2000,
                status = PublicationStatus.UNPUBLISHED,
                authors = emptyList()
            )
        )

        books.forEach { bookRepository.save(it) }

        val foundBooks = bookRepository.findAll()

        assertEquals(2, foundBooks.size)
    }
}
