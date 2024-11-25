package com.quoCard.bookManagementApp.annotation

import com.quoCard.bookManagementApp.validator.EnumValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass


@MustBeDocumented
@Constraint(validatedBy = [EnumValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidEnum(
    val enumClass: KClass<out Enum<*>>,
    val message: String = "Value must be one of the specified enum values",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
