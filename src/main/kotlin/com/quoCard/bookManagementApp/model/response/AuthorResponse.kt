package com.quoCard.bookManagementApp.model.response

import java.time.LocalDate

data class AuthorResponse(
    val id: Long,
    val name: String,
    val birthDate: LocalDate,
    val bookIds: List<Long>
)