package com.quoCard.bookManagementApp.controller

import com.quoCard.bookManagementApp.model.Author
import com.quoCard.bookManagementApp.model.Book
import com.quoCard.bookManagementApp.service.AuthorService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/authors")
class AuthorController(
    private val authorService: AuthorService
) {

    @Operation(summary = "Get all authors", description = "Retrieve a list of all authors in the system")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = Author::class))
                ]
            )
        ]
    )
    @GetMapping
    fun getAllAuthors(): List<Author> {
        return authorService.getAllAuthors()
    }

    @Operation(summary = "Get an author by ID", description = "Retrieve an author's details by their unique identifier")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Author found",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = Author::class))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Author not found")
        ]
    )
    @GetMapping("/{id}")
    fun getAuthorById(
        @Parameter(description = "ID of the author to retrieve", example = "1")
        @PathVariable
        id: Long
    ): Author {
        return authorService.getAuthorById(id)
    }

    @Operation(summary = "Create a new author", description = "Add a new author to the system")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Author created successfully",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = Author::class))
                ]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAuthor(
        @Parameter(description = "Details of the author to create")
        @RequestBody
        author: Author
    ): Author {
        return authorService.createAuthor(author)
    }

    @Operation(summary = "Update an author", description = "Update the details of an existing author")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Author updated successfully",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = Author::class))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Author not found"),
            ApiResponse(responseCode = "400", description = "Invalid input")
        ]
    )
    @PutMapping("/{id}")
    fun updateAuthor(
        @Parameter(description = "ID of the author to update", example = "1")
        @PathVariable
        id: Long,
        @Parameter(description = "Updated details of the author")
        @RequestBody
        author: Author
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
        @PathVariable
        id: Long
    ) {
        authorService.deleteAuthor(id)
    }

    @Operation(summary = "Get all books by author ID", description = "Retrieve all books associated with a specific author")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Books retrieved successfully",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = Book::class))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Author not found")
        ]
    )
    @GetMapping("/{id}/books")
    fun getBooksByAuthorId(
        @Parameter(description = "ID of the author whose books are to be retrieved", example = "1")
        @PathVariable
        id: Long
    ): List<Book> {
        return authorService.getBooksByAuthorId(id)
    }
}
