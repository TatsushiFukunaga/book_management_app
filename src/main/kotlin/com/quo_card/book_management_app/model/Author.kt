package com.quo_card.book_management_app.model

import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PastOrPresent

data class Author(
    val id: Long = 0,

    @field:NotBlank(message = "Author name cannot be blank")
    val name: String,

    @field:PastOrPresent(message = "Birthdate must be in the past or present")
    val birthDate: LocalDate
) {
    val books: MutableList<Book> = mutableListOf()
}
