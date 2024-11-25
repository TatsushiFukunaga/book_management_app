package com.quoCard.bookManagementApp.model.entity

import java.time.LocalDate
import java.time.LocalDateTime

data class Author(
    val id: Long = 0,
    val name: String,
    val birthDate: LocalDate,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
