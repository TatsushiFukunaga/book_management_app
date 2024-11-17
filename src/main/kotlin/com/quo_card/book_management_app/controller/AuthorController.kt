package com.quo_card.book_management_app.controller

import com.quo_card.book_management_app.model.Author
import com.quo_card.book_management_app.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/authors")
class AuthorController(
    private val authorService: AuthorService
) {
    @GetMapping
    fun getAllAuthors(): List<Author> {
        return authorService.getAllAuthors()
    }

    @GetMapping("/{id}")
    fun getAuthorById(@PathVariable id: Long): Author {
        return authorService.getAuthorById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAuthor(@RequestBody author: Author): Author {
        return authorService.createAuthor(author)
    }

    @PutMapping("/{id}")
    fun updateAuthor(@PathVariable id: Long, @RequestBody author: Author): Author {
        return authorService.updateAuthor(id, author)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAuthor(@PathVariable id: Long) {
        authorService.deleteAuthor(id)
    }
}
