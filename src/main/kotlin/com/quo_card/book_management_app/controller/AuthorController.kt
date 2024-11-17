package com.quo_card.book_management_app.controller

import com.quo_card.book_management_app.model.Author
import com.quo_card.book_management_app.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema

@RestController
@RequestMapping("/api/authors")
class AuthorController(
    private val authorService: AuthorService
) {

    @Operation(summary = "Get all authors", description = "Retrieve a list of all authors in the system")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful operation", content = [
                Content(mediaType = "application/json", schema = Schema(implementation = Author::class))
            ])
        ]
    )
    @GetMapping
    fun getAllAuthors(): List<Author> {
        return authorService.getAllAuthors()
    }

    @Operation(summary = "Get an author by ID", description = "Retrieve an author's details by their unique identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Author found", content = [
                Content(mediaType = "application/json", schema = Schema(implementation = Author::class))
            ]),
            ApiResponse(responseCode = "404", description = "Author not found")
        ]
    )
    @GetMapping("/{id}")
    fun getAuthorById(
        @Parameter(description = "ID of the author to retrieve", example = "1")
        @PathVariable id: Long
    ): Author {
        return authorService.getAuthorById(id)
    }

    @Operation(summary = "Create a new author", description = "Add a new author to the system")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Author created successfully", content = [
                Content(mediaType = "application/json", schema = Schema(implementation = Author::class))
            ]),
            ApiResponse(responseCode = "400", description = "Invalid input")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAuthor(
        @Parameter(description = "Details of the author to create")
        @RequestBody author: Author
    ): Author {
        return authorService.createAuthor(author)
    }

    @Operation(summary = "Update an author", description = "Update the details of an existing author")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Author updated successfully", content = [
                Content(mediaType = "application/json", schema = Schema(implementation = Author::class))
            ]),
            ApiResponse(responseCode = "404", description = "Author not found"),
            ApiResponse(responseCode = "400", description = "Invalid input")
        ]
    )
    @PutMapping("/{id}")
    fun updateAuthor(
        @Parameter(description = "ID of the author to update", example = "1")
        @PathVariable id: Long,
        @Parameter(description = "Updated details of the author")
        @RequestBody author: Author
    ): Author {
        return authorService.updateAuthor(id, author)
    }

    @Operation(summary = "Delete an author", description = "Delete an author by their unique identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            ApiResponse(responseCode = "404", description = "Author not found")
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAuthor(
        @Parameter(description = "ID of the author to delete", example = "1")
        @PathVariable id: Long
    ) {
        authorService.deleteAuthor(id)
    }
}