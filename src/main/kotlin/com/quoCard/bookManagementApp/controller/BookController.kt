package com.quoCard.bookManagementApp.controller

import com.quoCard.bookManagementApp.model.dto.BookDto
import com.quoCard.bookManagementApp.model.entity.Book
import com.quoCard.bookManagementApp.model.response.BookResponse
import com.quoCard.bookManagementApp.model.response.ErrorResponse
import com.quoCard.bookManagementApp.service.BookService
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
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {

    @Operation(summary = "Get all books", description = "Retrieve a list of all books in the system")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = BookResponse::class))
                ]
            )
        ]
    )
    @GetMapping
    fun getAllBooks(): List<BookResponse> {
        return bookService.getAllBooks()
    }

    @Operation(summary = "Get a book by ID", description = "Retrieve a book's details by its unique identifier")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Book found",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = BookResponse::class))
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))
                ]
            )
        ]
    )
    @GetMapping("/{id}")
    fun getBookById(
        @Parameter(description = "ID of the book to be retrieved", example = "1")
        @PathVariable
        id: Long
    ): BookResponse {
        return bookService.getBookById(id)
    }

    @Operation(summary = "Create a new book", description = "Add a new book to the system")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Book created successfully",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = BookResponse::class))
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))
                ]
            )
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(
        @Parameter(description = "Details of the book to be created")
        @RequestBody
        book: BookDto
    ): BookResponse {
        return bookService.createBook(book)
    }

    @Operation(summary = "Update a book", description = "Update the details of an existing book")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Book updated successfully",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = BookResponse::class))
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))
                ]
            )
        ]
    )
    @PutMapping("/{id}")
    fun updateBook(
        @Parameter(description = "ID of the book to be updated", example = "1")
        @PathVariable
        id: Long,
        @Parameter(description = "Updated details of the book")
        @RequestBody
        book: BookDto
    ): BookResponse {
        return bookService.updateBook(id, book)
    }

    @Operation(summary = "Delete a book", description = "Delete a book by its unique identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [
                    Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))
                ]
            )
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(
        @Parameter(description = "ID of the book to be deleted", example = "1")
        @PathVariable
        id: Long
    ) {
        bookService.deleteBook(id)
    }
}
