package com.quoCard.bookManagementApp.model.dto

import com.quoCard.bookManagementApp.annotation.ValidBirthDate
import jakarta.validation.constraints.NotBlank
import java.util.Collections.emptyList

data class AuthorDto(
    @field:NotBlank(message = "Author name cannot be blank")
    val name: String,
    @field:ValidBirthDate
    val birthDate: String,
    val bookIds: List<Long> = emptyList()
)