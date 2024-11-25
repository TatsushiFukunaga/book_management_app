package com.quoCard.bookManagementApp.annotation

import com.quoCard.bookManagementApp.validator.BirthDateValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [BirthDateValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidBirthDate(
    val message: String = "Invalid birth date. It must follow the format yyyy-MM-dd and be in the past or present.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
