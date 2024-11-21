package com.quoCard.bookManagementApp.model

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
internal class AuthorTest {

    private val validatorFactory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    private val validator: Validator = validatorFactory.validator

    private val defaultBook = Book(
        id = 1,
        title = "Sample Book",
        price = 20,
        status = PublicationStatus.PUBLISHED,
        authors = emptyList()
    )

    @Test
    fun `should pass validation when all fields are valid`() {
        val author = Author(
            id = 1,
            name = "John Doe",
            birthDate = LocalDate.of(1980, 1, 1),
            books = listOf(defaultBook)
        )

        val violations = validator.validate(author)
        assertTrue(violations.isEmpty(), "Validation should pass when all fields are valid")
    }

    @Test
    fun `should fail validation when name is blank`() {
        val author = Author(
            id = 1,
            name = "",
            birthDate = LocalDate.of(1980, 1, 1),
            books = listOf(defaultBook)
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
            birthDate = LocalDate.now().plusDays(1), // Future date
            books = listOf(defaultBook)
        )

        val violations = validator.validate(author)
        assertEquals(1, violations.size, "Validation should fail due to future birthDate")
        assertEquals("Birthdate must be in the past or present", violations.first().message)
    }

    @Test
    fun `should pass validation when books list is empty`() {
        val author = Author(
            id = 1,
            name = "John Doe",
            birthDate = LocalDate.of(1980, 1, 1),
            books = emptyList()
        )

        val violations = validator.validate(author)
        assertTrue(violations.isEmpty(), "Validation should pass even if books list is empty")
    }
}
