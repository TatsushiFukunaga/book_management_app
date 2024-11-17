package com.quo_card.book_management_app.model

import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PastOrPresent
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Author entity representing an author in the system")
data class Author(
    @Schema(description = "Unique identifier of the author", example = "1")
    val id: Long = 0,

    @Schema(description = "Name of the author", example = "John Doe")
    @field:NotBlank(message = "Author name cannot be blank")
    val name: String,

    @Schema(description = "Birthdate of the author", example = "1980-01-01")
    @field:PastOrPresent(message = "Birthdate must be in the past or present")
    val birthDate: LocalDate,

    @Schema(description = "List of books written by the author")
    val books: MutableList<Book> = mutableListOf()
)