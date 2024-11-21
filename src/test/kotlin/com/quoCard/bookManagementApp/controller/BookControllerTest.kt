package com.quoCard.bookManagementApp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.quoCard.bookManagementApp.model.Book
import com.quoCard.bookManagementApp.model.PublicationStatus
import com.quoCard.bookManagementApp.service.BookService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookController::class)
internal class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var bookService: BookService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return all books`() {
        val books = listOf(
            Book(id = 1, title = "Kotlin Programming", price = 29, status = PublicationStatus.PUBLISHED),
            Book(id = 2, title = "Spring Boot Guide", price = 39, status = PublicationStatus.UNPUBLISHED)
        )

        Mockito.`when`(bookService.getAllBooks()).thenReturn(books)

        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].title").value("Kotlin Programming"))
            .andExpect(jsonPath("$[1].price").value(39))
    }

    @Test
    fun `should return a book by ID`() {
        val book = Book(id = 1, title = "Kotlin Programming", price = 29, status = PublicationStatus.PUBLISHED)

        Mockito.`when`(bookService.getBookById(1)).thenReturn(book)

        mockMvc.perform(get("/api/books/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("Kotlin Programming"))
            .andExpect(jsonPath("$.price").value(29))
    }

    @Test
    fun `should create a new book`() {
        val book = Book(id = 1, title = "New Book", price = 19, status = PublicationStatus.UNPUBLISHED)

        Mockito.`when`(bookService.createBook(book)).thenReturn(book)

        mockMvc.perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book))
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("New Book"))
            .andExpect(jsonPath("$.price").value(19))
    }

    @Test
    fun `should update an existing book`() {
        val updatedBook = Book(id = 1, title = "Updated Book", price = 25, status = PublicationStatus.PUBLISHED)

        Mockito.`when`(bookService.updateBook(1, updatedBook)).thenReturn(updatedBook)

        mockMvc.perform(
            put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("Updated Book"))
            .andExpect(jsonPath("$.price").value(25))
    }

    @Test
    fun `should delete a book by ID`() {
        Mockito.doNothing().`when`(bookService).deleteBook(1)

        mockMvc.perform(delete("/api/books/1"))
            .andExpect(status().isNoContent)
    }
}
