package com.quoCard.bookManagementApp.validator

import com.quoCard.bookManagementApp.annotation.ValidBirthDate
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class BirthDateValidator : ConstraintValidator<ValidBirthDate, String> {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value.isNullOrBlank()) return false

        return try {
            val parsedDate = LocalDate.parse(value, dateFormatter)
            parsedDate.isBefore(LocalDate.now().plusDays(1))
        } catch (e: DateTimeParseException) {
            false
        }
    }
}