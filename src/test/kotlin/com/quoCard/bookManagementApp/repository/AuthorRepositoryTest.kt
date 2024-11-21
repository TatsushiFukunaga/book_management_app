package com.quoCard.bookManagementApp.repository

import com.quoCard.bookManagementApp.model.Author
import com.quoCard.bookManagementApp.model.Book
import com.quoCard.bookManagementApp.model.PublicationStatus
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.Collections.emptyList

@SpringBootTest
@Transactional
@ActiveProfiles("test")
internal class AuthorRepositoryTest {

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @BeforeEach
    fun setUp() {
        authorRepository.findAll().forEach { authorRepository.deleteById(it.id) }
        bookRepository.findAll().forEach { bookRepository.deleteById(it.id) }
    }

    @Test
    fun `save new author with books`() {
        val books = listOf(
            Book(
                id = 0,
                title = "Test Book 1",
                price = 1000,
                status = PublicationStatus.UNPUBLISHED
            ),
            Book(
                id = 0,
                title = "Test Book 2",
                price = 1500,
                status = PublicationStatus.PUBLISHED
            )
        )

        val author = Author(
            id = 0,
            name = "Test Author",
            birthDate = LocalDate.of(1980, 1, 1),
            books = books
        )

        val savedAuthor = authorRepository.save(author)

        assertNotNull(savedAuthor.id)
        assertEquals("Test Author", savedAuthor.name)
        assertEquals(2, savedAuthor.books.size)
        assertEquals("Test Book 1", savedAuthor.books[0].title)
    }

    @Test
    fun `update existing author with books`() {
        val author = Author(
            id = 0,
            name = "Initial Author",
            birthDate = LocalDate.of(1975, 5, 15),
            books = listOf(
                Book(
                    id = 0,
                    title = "Initial Book",
                    price = 2000,
                    status = PublicationStatus.UNPUBLISHED
                )
            )
        )

        val savedAuthor = authorRepository.save(author)

        val updatedAuthor = savedAuthor.copy(
            name = "Updated Author",
            books = listOf(
                Book(
                    id = savedAuthor.books[0].id,
                    title = "Updated Book",
                    price = 2500,
                    status = PublicationStatus.PUBLISHED
                )
            )
        )

        val result = authorRepository.save(updatedAuthor)

        assertEquals("Updated Author", result.name)
        assertEquals(1, result.books.size)
        assertEquals("Updated Book", result.books[0].title)
        assertEquals(2500, result.books[0].price)
    }

    @Test
    fun `find author by ID`() {
        val author = Author(
            id = 0,
            name = "Find Me",
            birthDate = LocalDate.of(1990, 12, 25),
            books = emptyList()
        )

        val savedAuthor = authorRepository.save(author)

        val foundAuthor = authorRepository.findById(savedAuthor.id)
        assertNotNull(foundAuthor)
        assertEquals(savedAuthor.id, foundAuthor!!.id)
        assertEquals("Find Me", foundAuthor.name)
    }

    @Test
    fun `delete author by ID`() {
        val author = Author(
            id = 0,
            name = "To Be Deleted",
            birthDate = LocalDate.of(1985, 3, 10),
            books = listOf(
                Book(
                    id = 0,
                    title = "Book to Delete",
                    price = 3000,
                    status = PublicationStatus.UNPUBLISHED
                )
            )
        )

        val savedAuthor = authorRepository.save(author)
        assertNotNull(authorRepository.findById(savedAuthor.id))

        authorRepository.deleteById(savedAuthor.id)
        assertNull(authorRepository.findById(savedAuthor.id))
    }

    @Test
    fun `find all authors`() {
        val author1 = Author(
            id = 0,
            name = "Author 1",
            birthDate = LocalDate.of(1980, 1, 1),
            books = emptyList()
        )
        val author2 = Author(
            id = 0,
            name = "Author 2",
            birthDate = LocalDate.of(1990, 6, 15),
            books = emptyList()
        )

        authorRepository.save(author1)
        authorRepository.save(author2)

        val authors = authorRepository.findAll()
        assertEquals(2, authors.size)
    }
}
