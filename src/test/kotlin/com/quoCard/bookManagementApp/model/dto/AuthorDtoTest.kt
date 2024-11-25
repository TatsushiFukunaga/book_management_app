package com.quoCard.bookManagementApp.model.dto

import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.Collections.emptyList

internal class AuthorDtoTest {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `valid AuthorDto should pass validation`() {
        val authorDto = AuthorDto(
            name = "John Doe",
            birthDate = "1980-01-01",
            bookIds = listOf(1, 2, 3)
        )

        val violations: Set<ConstraintViolation<AuthorDto>> = validator.validate(authorDto)

        assertTrue(violations.isEmpty(), "Expected no validation errors")
    }

    @Test
    fun `AuthorDto with blank name should fail validation`() {
        val authorDto = AuthorDto(
            name = "",
            birthDate = "1980-01-01",
            bookIds = listOf(1, 2, 3)
        )

        val violations: Set<ConstraintViolation<AuthorDto>> = validator.validate(authorDto)

        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("Author name cannot be blank", violation.message)
    }

    @Test
    fun `AuthorDto with invalid birthDate format should fail validation`() {
        val authorDto = AuthorDto(
            name = "John Doe",
            birthDate = "01-01-1980", // Invalid format
            bookIds = listOf(1, 2, 3)
        )

        val violations: Set<ConstraintViolation<AuthorDto>> = validator.validate(authorDto)

        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("Invalid birth date. It must follow the format yyyy-MM-dd and be in the past or present.", violation.message)
    }

    @Test
    fun `AuthorDto with future birthDate should fail validation`() {
        val authorDto = AuthorDto(
            name = "John Doe",
            birthDate = "2030-01-01", // Future date
            bookIds = listOf(1, 2, 3)
        )

        val violations: Set<ConstraintViolation<AuthorDto>> = validator.validate(authorDto)

        assertEquals(1, violations.size)
        val violation = violations.first()
        assertEquals("Invalid birth date. It must follow the format yyyy-MM-dd and be in the past or present.", violation.message)
    }

    @Test
    fun `AuthorDto with empty bookIds should pass validation`() {
        val authorDto = AuthorDto(
            name = "John Doe",
            birthDate = "1980-01-01",
            bookIds = emptyList() // Empty list is valid
        )

        val violations: Set<ConstraintViolation<AuthorDto>> = validator.validate(authorDto)

        assertTrue(violations.isEmpty(), "Expected no validation errors")
    }
}