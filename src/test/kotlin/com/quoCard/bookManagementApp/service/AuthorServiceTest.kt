package com.quoCard.bookManagementApp.service

import com.quoCard.bookManagementApp.model.Author
import com.quoCard.bookManagementApp.repository.AuthorRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.time.LocalDate

internal class AuthorServiceTest {

    private val authorRepository: AuthorRepository = Mockito.mock(AuthorRepository::class.java)
    private val authorService = AuthorService(authorRepository)

    @Test
    fun `getAllAuthors should return all authors`() {
        val authors = listOf(
            Author(1, "Author One", LocalDate.of(1980, 1, 1)),
            Author(2, "Author Two", LocalDate.of(1990, 2, 2))
        )

        Mockito.`when`(authorRepository.findAll()).thenReturn(authors)

        val result = authorService.getAllAuthors()

        assertEquals(authors.size, result.size)
        assertEquals(authors, result)
    }

    @Test
    fun `getAuthorById should return author when exists`() {
        val author = Author(1, "Author One", LocalDate.of(1980, 1, 1))
        Mockito.`when`(authorRepository.findById(1)).thenReturn(author)

        val result = authorService.getAuthorById(1)

        assertEquals(author, result)
    }

    @Test
    fun `getAuthorById should throw exception when author does not exist`() {
        Mockito.`when`(authorRepository.findById(1)).thenReturn(null)

        val exception = assertThrows<IllegalArgumentException> {
            authorService.getAuthorById(1)
        }

        assertEquals("Author with ID 1 not found", exception.message)
    }

    @Test
    fun `createAuthor should save and return the author`() {
        val author = Author(name = "Author One", birthDate = LocalDate.of(1980, 1, 1))
        val savedAuthor = author.copy(id = 1)

        Mockito.`when`(authorRepository.save(author)).thenReturn(savedAuthor)

        val result = authorService.createAuthor(author)

        assertEquals(savedAuthor, result)
    }

    @Test
    fun `updateAuthor should update and return the author`() {
        val existingAuthor = Author(1, "Old Name", LocalDate.of(1980, 1, 1))
        val updatedAuthor = Author(name = "New Name", birthDate = LocalDate.of(1985, 5, 5))
        val savedAuthor = Author(1, "New Name", LocalDate.of(1985, 5, 5))

        Mockito.`when`(authorRepository.findById(1)).thenReturn(existingAuthor)
        Mockito.`when`(authorRepository.save(savedAuthor)).thenReturn(savedAuthor)

        val result = authorService.updateAuthor(1, updatedAuthor)

        assertEquals(savedAuthor, result)
    }

    @Test
    fun `updateAuthor should throw exception when author does not exist`() {
        val updatedAuthor = Author(name = "New Name", birthDate = LocalDate.of(1985, 5, 5))

        Mockito.`when`(authorRepository.findById(1)).thenReturn(null)

        val exception = assertThrows<IllegalArgumentException> {
            authorService.updateAuthor(1, updatedAuthor)
        }

        assertEquals("Author with ID 1 not found", exception.message)
    }

    @Test
    fun `deleteAuthor should delete the author when exists`() {
        val author = Author(id = 1, name = "Author One", birthDate = LocalDate.of(1980, 1, 1))
        Mockito.`when`(authorRepository.findById(1)).thenReturn(author)

        authorService.createAuthor(author)
        authorService.deleteAuthor(1)

        Mockito.verify(authorRepository).deleteById(1)
    }

    @Test
    fun `deleteAuthor should throw exception when author does not exist`() {
        Mockito.`when`(authorRepository.findById(1)).thenReturn(null)

        val exception = assertThrows<IllegalArgumentException> {
            authorService.deleteAuthor(1)
        }

        assertEquals("Author with ID 1 not found", exception.message)
    }
}
