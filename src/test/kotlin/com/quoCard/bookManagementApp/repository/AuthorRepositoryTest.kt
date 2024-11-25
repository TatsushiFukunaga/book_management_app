package com.quoCard.bookManagementApp.repository

import com.quoCard.bookManagementApp.model.entity.Author
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
@ActiveProfiles("test")
internal class AuthorRepositoryTest {

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @Test
    fun `should save a new author`() {
        val author = Author(
            id = 0L,
            name = "John Doe",
            birthDate = LocalDate.of(1980, 1, 1),
            createdAt = LocalDate.now().atStartOfDay(),
            updatedAt = LocalDate.now().atStartOfDay()
        )

        val savedAuthor = authorRepository.save(author)
        assertNotNull(savedAuthor.id)
        assertEquals("John Doe", savedAuthor.name)
        assertEquals(LocalDate.of(1980, 1, 1), savedAuthor.birthDate)
    }

    @Test
    fun `should update an existing author`() {
        val author = Author(
            id = 0L,
            name = "John Doe",
            birthDate = LocalDate.of(1980, 1, 1),
            createdAt = LocalDate.now().atStartOfDay(),
            updatedAt = LocalDate.now().atStartOfDay()
        )

        val savedAuthor = authorRepository.save(author)
        val updatedAuthor = savedAuthor.copy(name = "Jane Doe")

        val result = authorRepository.save(updatedAuthor)
        assertEquals(savedAuthor.id, result.id)
        assertEquals("Jane Doe", result.name)
    }

    @Test
    fun `should find all authors`() {
        val author1 = Author(
            id = 0L,
            name = "John Doe",
            birthDate = LocalDate.of(1980, 1, 1),
            createdAt = LocalDate.now().atStartOfDay(),
            updatedAt = LocalDate.now().atStartOfDay()
        )

        val author2 = Author(
            id = 0L,
            name = "Jane Smith",
            birthDate = LocalDate.of(1990, 5, 15),
            createdAt = LocalDate.now().atStartOfDay(),
            updatedAt = LocalDate.now().atStartOfDay()
        )

        authorRepository.save(author1)
        authorRepository.save(author2)

        val authors = authorRepository.findAll()
        assertEquals(2, authors.size)
    }

    @Test
    fun `should find an author by ID`() {
        val author = Author(
            id = 0L,
            name = "John Doe",
            birthDate = LocalDate.of(1980, 1, 1),
            createdAt = LocalDate.now().atStartOfDay(),
            updatedAt = LocalDate.now().atStartOfDay()
        )

        val savedAuthor = authorRepository.save(author)
        val foundAuthor = authorRepository.findById(savedAuthor.id)

        assertNotNull(foundAuthor)
        assertEquals(savedAuthor.id, foundAuthor?.id)
        assertEquals("John Doe", foundAuthor?.name)
    }

    @Test
    fun `should delete an author by ID`() {
        val author = Author(
            id = 0L,
            name = "John Doe",
            birthDate = LocalDate.of(1980, 1, 1),
            createdAt = LocalDate.now().atStartOfDay(),
            updatedAt = LocalDate.now().atStartOfDay()
        )

        val savedAuthor = authorRepository.save(author)
        authorRepository.deleteById(savedAuthor.id)

        val foundAuthor = authorRepository.findById(savedAuthor.id)
        assertNull(foundAuthor)
    }

    @Test
    fun `should find all existing IDs`() {
        val author1 = Author(
            id = 0L,
            name = "John Doe",
            birthDate = LocalDate.of(1980, 1, 1),
            createdAt = LocalDate.now().atStartOfDay(),
            updatedAt = LocalDate.now().atStartOfDay()
        )

        val author2 = Author(
            id = 0L,
            name = "Jane Smith",
            birthDate = LocalDate.of(1990, 5, 15),
            createdAt = LocalDate.now().atStartOfDay(),
            updatedAt = LocalDate.now().atStartOfDay()
        )

        authorRepository.save(author1)
        authorRepository.save(author2)

        val existingIds = authorRepository.findExistingIds()
        assertEquals(2, existingIds.size)
    }
}
