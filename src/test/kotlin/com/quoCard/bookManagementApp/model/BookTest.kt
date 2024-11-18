package com.quoCard.bookManagementApp.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class BookTest {

    private val validatorFactory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    private val validator: Validator = validatorFactory.validator

    @Test
    fun `should pass validation when all fields are valid`() {
        val book = Book(
            title = "Kotlin Programming",
            price = 29,
            status = PublicationStatus.UNPUBLISHED
        )
        val violations = validator.validate(book)
        assertTrue(violations.isEmpty(), "Validation should pass when all fields are valid")
    }

    @Test
    fun `should fail validation when title is blank`() {
        val book = Book(
            title = "",
            price = 29,
            status = PublicationStatus.UNPUBLISHED
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
            status = PublicationStatus.UNPUBLISHED
        )
        val violations = validator.validate(book)
        assertEquals(1, violations.size)
        assertEquals("Book price must be at least 0", violations.first().message)
    }

    @Test
    fun `should add author correctly`() {
        val book = Book(title = "Kotlin Programming", price = 29)
        val author = Author(name = "John Doe", birthDate = LocalDate.of(1980, 1, 1))

        book.addAuthor(author)
        assertTrue(book.authors.contains(author))
        assertTrue(author.books.contains(book))
    }

    @Test
    fun `should update status to PUBLISHED`() {
        val book = Book(title = "Kotlin Programming", price = 29)

        book.updateStatus(PublicationStatus.PUBLISHED)
        assertEquals(PublicationStatus.PUBLISHED, book.status)
    }

    @Test
    fun `should throw exception when changing status from PUBLISHED to UNPUBLISHED`() {
        val book = Book(title = "Kotlin Programming", price = 29, status = PublicationStatus.PUBLISHED)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            book.updateStatus(PublicationStatus.UNPUBLISHED)
        }
        assertEquals("Cannot change status from PUBLISHED to UNPUBLISHED", exception.message)
    }
}
