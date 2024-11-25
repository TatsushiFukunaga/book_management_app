package com.quoCard.bookManagementApp.model.dto

import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.Collections.emptyList

internal class BookDtoTest {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `valid BookDto should pass validation`() {
        val bookDto = BookDto(
            title = "Effective Java",
            price = BigDecimal("49.99"),
            status = "PUBLISHED",
            authorIds = listOf(1, 2, 3)
        )

        val violations: Set<ConstraintViolation<BookDto>> = validator.validate(bookDto)

        assertTrue(violations.isEmpty(), "Expected no validation errors")
    }

    @Test
    fun `BookDto with blank title should fail validation`() {
        val bookDto = BookDto(
            title = "",
            price = BigDecimal("49.99"),
            status = "PUBLISHED",
            authorIds = listOf(1, 2, 3)
        )

        val violations: Set<ConstraintViolation<BookDto>> = validator.validate(bookDto)

        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("Book title cannot be blank", violation.message)
    }

    @Test
    fun `BookDto with negative price should fail validation`() {
        val bookDto = BookDto(
            title = "Effective Java",
            price = BigDecimal("-10.00"),
            status = "PUBLISHED",
            authorIds = listOf(1, 2, 3)
        )

        val violations: Set<ConstraintViolation<BookDto>> = validator.validate(bookDto)

        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("Book price must be at least 0", violation.message)
    }

    @Test
    fun `BookDto with invalid status should fail validation`() {
        val bookDto = BookDto(
            title = "Effective Java",
            price = BigDecimal("49.99"),
            status = "INVALID_STATUS", // Invalid Enum value
            authorIds = listOf(1, 2, 3)
        )

        val violations: Set<ConstraintViolation<BookDto>> = validator.validate(bookDto)

        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("Status must be one of: PUBLISHED, UNPUBLISHED", violation.message)
    }

    @Test
    fun `BookDto with empty authorIds should fail validation`() {
        val bookDto = BookDto(
            title = "Effective Java",
            price = BigDecimal("49.99"),
            status = "PUBLISHED",
            authorIds = emptyList() // No authors
        )

        val violations: Set<ConstraintViolation<BookDto>> = validator.validate(bookDto)

        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("A book must have at least one author", violation.message)
    }
}