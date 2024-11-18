package com.quoCard.bookManagementApp.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class AuthorTest {

    private val validatorFactory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    private val validator: Validator = validatorFactory.validator

    @Test
    fun `should pass validation when all fields are valid`() {
        val author = Author(
            id = 1,
            name = "John Doe",
            birthDate = LocalDate.of(1980, 1, 1)
        )

        val violations = validator.validate(author)
        assertTrue(violations.isEmpty(), "Validation should pass when all fields are valid")
    }

    @Test
    fun `should fail validation when name is blank`() {
        val author = Author(
            id = 1,
            name = "",
            birthDate = LocalDate.of(1980, 1, 1)
        )

        val violations = validator.validate(author)
        assertEquals(1, violations.size, "Validation should fail due to blank name")
        assertEquals("Author name cannot be blank", violations.first().message)
    }

    @Test
    fun `should fail validation when birthDate is in the future`() {
        val author = Author(
            id = 1,
            name = "John Doe",
            birthDate = LocalDate.now().plusDays(1) // Future date
        )

        val violations = validator.validate(author)
        assertEquals(1, violations.size, "Validation should fail due to future birthDate")
        assertEquals("Birthdate must be in the past or present", violations.first().message)
    }

    @Test
    fun `should allow adding books to author's list`() {
        val author = Author(
            id = 1,
            name = "John Doe",
            birthDate = LocalDate.of(1980, 1, 1)
        )

        val book = Book(
            id = 1,
            title = "Kotlin Programming",
            price = 29,
            status = PublicationStatus.PUBLISHED
        )

        author.books.add(book)
        assertEquals(1, author.books.size, "Author should have one book in the list")
        assertEquals("Kotlin Programming", author.books.first().title, "Book title should match")
    }
}
