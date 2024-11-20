package com.quoCard.bookManagementApp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDate
import java.util.Collections.emptyList

@Schema(description = "Author entity representing an author in the system")
data class Author(
    @Schema(description = "Unique identifier of the author", example = "1")
    @JsonProperty("id")
    val id: Long = 0,

    @Schema(description = "Name of the author", example = "John Doe")
    @JsonProperty("name")
    @field:NotBlank(message = "Author name cannot be blank")
    val name: String,

    @Schema(description = "Birthdate of the author", example = "1980-01-01")
    @field:PastOrPresent(message = "Birthdate must be in the past or present")
    @JsonProperty("birthDate")
    val birthDate: LocalDate,

    @Schema(description = "List of books written by the author")
    @JsonProperty("books")
    @JsonIgnoreProperties("authors")
    val books: List<Book> = emptyList()
)
