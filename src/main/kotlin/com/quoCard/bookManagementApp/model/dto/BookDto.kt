package com.quoCard.bookManagementApp.model.dto

import com.quoCard.bookManagementApp.annotation.ValidEnum
import com.quoCard.bookManagementApp.model.entity.PublicationStatus
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.math.BigDecimal

data class BookDto(
    @field:NotBlank(message = "Book title cannot be blank")
    val title: String,
    @field:Min(value = 0, message = "Book price must be at least 0")
    val price: BigDecimal,
    @field:ValidEnum(
        enumClass = PublicationStatus::class,
        message = "Status must be one of: PUBLISHED, UNPUBLISHED"
    )
    val status: String,
    @field:NotEmpty(message = "A book must have at least one author")
    val authorIds: List<Long>
)