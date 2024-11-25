package com.quoCard.bookManagementApp.service

import com.quoCard.bookManagementApp.model.dto.AuthorDto
import com.quoCard.bookManagementApp.model.entity.Author
import com.quoCard.bookManagementApp.model.exception.InvalidAuthorException
import com.quoCard.bookManagementApp.model.exception.ResourceNotFoundException
import com.quoCard.bookManagementApp.repository.AuthorRepository
import com.quoCard.bookManagementApp.repository.BookAuthorRepository
import com.quoCard.bookManagementApp.repository.BookRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Collections.emptyList

internal class AuthorServiceTest {

    private val authorRepository: AuthorRepository = mock()
    private val bookRepository: BookRepository = mock()
    private val bookAuthorRepository: BookAuthorRepository = mock()
    private val authorService = AuthorService(authorRepository, bookRepository, bookAuthorRepository)

    @Test
    fun `should get all authors`() {
        val authors = listOf(
            Author(
                1L, "John Doe", LocalDate.of(1980, 1, 1),
                LocalDateTime.now(), LocalDateTime.now()
            ),
            Author(
                2L, "Jane Smith", LocalDate.of(1990, 5, 15),
                LocalDateTime.now(), LocalDateTime.now()
            )
        )
        val bookIds = listOf(1L, 2L)

        whenever(authorRepository.findAll()).thenReturn(authors)
        whenever(bookAuthorRepository.findBookIdsByAuthorId(1L)).thenReturn(bookIds)
        whenever(bookAuthorRepository.findBookIdsByAuthorId(2L)).thenReturn(emptyList())

        val responses = authorService.getAllAuthors()

        assertEquals(2, responses.size)
        assertEquals("John Doe", responses[0].name)
        assertEquals(bookIds, responses[0].bookIds)
        assertEquals("Jane Smith", responses[1].name)
        assertTrue(responses[1].bookIds.isEmpty())

        verify(authorRepository).findAll()
        verify(bookAuthorRepository, times(2)).findBookIdsByAuthorId(any())
    }

    @Test
    fun `should get author by ID`() {
        val author = Author(1L, "John Doe", LocalDate.of(1980, 1, 1), LocalDateTime.now(), LocalDateTime.now())
        val bookIds = listOf(1L, 2L)

        whenever(authorRepository.findById(1L)).thenReturn(author)
        whenever(bookAuthorRepository.findBookIdsByAuthorId(1L)).thenReturn(bookIds)

        val response = authorService.getAuthorById(1L)

        assertEquals("John Doe", response.name)
        assertEquals(bookIds, response.bookIds)

        verify(authorRepository).findById(1L)
        verify(bookAuthorRepository).findBookIdsByAuthorId(1L)
    }

    @Test
    fun `should throw exception when author not found`() {
        whenever(authorRepository.findById(1L)).thenReturn(null)

        val exception = assertThrows<ResourceNotFoundException> {
            authorService.getAuthorById(1L)
        }

        assertEquals("Author with ID 1 not found", exception.message)

        verify(authorRepository).findById(1L)
        verify(bookAuthorRepository, never()).findBookIdsByAuthorId(any())
    }

    @Test
    fun `should create a new author`() {
        val authorDto = AuthorDto("John Doe", "1980-01-01", listOf(1L, 2L))
        val newAuthor = Author(
            0L, "John Doe", LocalDate.of(1980, 1, 1),
            LocalDateTime.now(), LocalDateTime.now()
        )
        val savedAuthor = newAuthor.copy(id = 1L)

        whenever(authorRepository.save(any())).thenReturn(savedAuthor)
        whenever(bookRepository.findExistingIds()).thenReturn(listOf(1L, 2L))
        whenever(bookAuthorRepository.findBookIdsByAuthorId(1L)).thenReturn(listOf(1L, 2L))

        val response = authorService.createAuthor(authorDto)

        assertEquals("John Doe", response.name)
        assertEquals(listOf(1L, 2L), response.bookIds)

        verify(authorRepository).save(any())
        verify(bookRepository).findExistingIds()
        verify(bookAuthorRepository).saveRelationsByAuthorId(1L, listOf(1L, 2L))
    }

    @Test
    fun `should throw exception when creating author with non-existent books`() {
        val authorDto = AuthorDto("John Doe", "1980-01-01", listOf(1L, 99L))

        whenever(bookRepository.findExistingIds()).thenReturn(listOf(1L))

        val exception = assertThrows<InvalidAuthorException> {
            authorService.createAuthor(authorDto)
        }

        assertEquals("The following book IDs do not exist: [99]", exception.message)

        verify(bookRepository).findExistingIds()
        verify(authorRepository, never()).save(any())
    }

    @Test
    fun `should update an existing author`() {
        val existingAuthor = Author(1L, "John Doe", LocalDate.of(1980, 1, 1), LocalDateTime.now(), LocalDateTime.now())
        val updatedAuthorDto = AuthorDto("Jane Smith", "1990-05-15", listOf(1L, 2L))
        val updatedAuthor = existingAuthor.copy(name = "Jane Smith", birthDate = LocalDate.of(1990, 5, 15))

        whenever(authorRepository.findById(1L)).thenReturn(existingAuthor)
        whenever(authorRepository.save(any())).thenReturn(updatedAuthor)
        whenever(bookRepository.findExistingIds()).thenReturn(listOf(1L, 2L))
        whenever(bookAuthorRepository.findBookIdsByAuthorId(1L)).thenReturn(listOf(1L, 2L))

        val response = authorService.updateAuthor(1L, updatedAuthorDto)

        assertEquals("Jane Smith", response.name)
        assertEquals(listOf(1L, 2L), response.bookIds)

        verify(authorRepository).findById(1L)
        verify(authorRepository).save(any())
        verify(bookAuthorRepository).saveRelationsByAuthorId(1L, listOf(1L, 2L))
    }

    @Test
    fun `should throw exception when updating non-existent author`() {
        val updatedAuthorDto = AuthorDto("Jane Smith", "1990-05-15", listOf(1L))

        whenever(authorRepository.findById(1L)).thenReturn(null)

        val exception = assertThrows<ResourceNotFoundException> {
            authorService.updateAuthor(1L, updatedAuthorDto)
        }

        assertEquals("Author with ID 1 not found", exception.message)

        verify(authorRepository).findById(1L)
        verify(authorRepository, never()).save(any())
    }

    @Test
    fun `should delete an existing author`() {
        val existingAuthor = Author(1L, "John Doe", LocalDate.of(1980, 1, 1), LocalDateTime.now(), LocalDateTime.now())

        whenever(authorRepository.findById(1L)).thenReturn(existingAuthor)

        authorService.deleteAuthor(1L)

        verify(authorRepository).findById(1L)
        verify(authorRepository).deleteById(1L)
    }

    @Test
    fun `should throw exception when deleting non-existent author`() {
        whenever(authorRepository.findById(1L)).thenReturn(null)

        val exception = assertThrows<ResourceNotFoundException> {
            authorService.deleteAuthor(1L)
        }

        assertEquals("Author with ID 1 not found", exception.message)

        verify(authorRepository).findById(1L)
        verify(authorRepository, never()).deleteById(any())
    }
}