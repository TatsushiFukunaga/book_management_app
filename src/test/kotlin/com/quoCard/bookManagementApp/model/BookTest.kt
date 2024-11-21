package com.quoCard.bookManagementApp.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.util.Collections.emptyList

@SpringBootTest
internal class BookTest {

    private val validatorFactory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    private val validator: Validator = validatorFactory.validator

    private val defaultAuthor = Author(
        id = 1,
        name = "John Doe",
        birthDate = LocalDate.of(1980, 1, 1),
        books = emptyList()
    )

    @Test
    fun `should pass validation when all fields are valid`() {
        val book = Book(
            title = "Kotlin Programming",
            price = 29,
            status = PublicationStatus.UNPUBLISHED,
            authors = listOf(defaultAuthor)
        )
        val violations = validator.validate(book)
        assertTrue(violations.isEmpty(), "Validation should pass when all fields are valid")
    }

    @Test
    fun `should fail validation when title is blank`() {
        val book = Book(
            title = "",
            price = 29,
            status = PublicationStatus.UNPUBLISHED,
            authors = listOf(defaultAuthor)
        )
        val violations = validator.validate(book)
        assertEquals(1, violations.size)
        assertEquals("Book title cannot be blank", violations.first().message)
    }

    @Test
    fun `should fail validation when price is negative`() {
        val book = Book(
            title = "Kotlin Programming",
            price = -1,
            status = PublicationStatus.UNPUBLISHED,
            authors = listOf(defaultAuthor)
        )
        val violations = validator.validate(book)
        assertEquals(1, violations.size)
        assertEquals("Book price must be at least 0", violations.first().message)
    }

    @Test
    fun `should fail validation when authors list is empty`() {
        val book = Book(
            title = "Kotlin Programming",
            price = 29,
            status = PublicationStatus.UNPUBLISHED,
            authors = emptyList()
        )
        val violations = validator.validate(book)
        assertEquals(1, violations.size)
        assertEquals("A book must have at least one author", violations.first().message)
    }

    @Test
    fun `should update status to PUBLISHED`() {
        val book = Book(
            title = "Kotlin Programming",
            price = 29,
            status = PublicationStatus.UNPUBLISHED,
            authors = listOf(defaultAuthor)
        )

        book.updateStatus(PublicationStatus.PUBLISHED)
        assertEquals(PublicationStatus.PUBLISHED, book.status)
    }

    @Test
    fun `should throw exception when changing status from PUBLISHED to UNPUBLISHED`() {
        val book = Book(
            title = "Kotlin Programming",
            price = 29,
            status = PublicationStatus.PUBLISHED,
            authors = listOf(defaultAuthor)
        )

        val exception = assertThrows(IllegalArgumentException::class.java) {
            book.updateStatus(PublicationStatus.UNPUBLISHED)
        }
        assertEquals("Cannot change status from PUBLISHED to UNPUBLISHED", exception.message)
    }
}
