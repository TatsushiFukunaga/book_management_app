package com.quoCard.bookManagementApp.validator

import com.quoCard.bookManagementApp.annotation.ValidEnum
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValidator : ConstraintValidator<ValidEnum, String> {

    private lateinit var acceptedValues: Set<String>

    override fun initialize(annotation: ValidEnum) {
        acceptedValues = annotation.enumClass.java.enumConstants
            .map { it.toString() }
            .toSet()
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return true
        return acceptedValues.contains(value)
    }
}