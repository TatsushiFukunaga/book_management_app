package com.quoCard.bookManagementApp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.quoCard.bookManagementApp.model.Author
import com.quoCard.bookManagementApp.service.AuthorService
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
import java.time.LocalDate

@WebMvcTest(AuthorController::class)
internal class AuthorControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authorService: AuthorService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return all authors`() {
        val authors = listOf(
            Author(id = 1, name = "John Doe", birthDate = LocalDate.of(1980, 1, 1)),
            Author(id = 2, name = "Jane Smith", birthDate = LocalDate.of(1990, 5, 15))
        )

        Mockito.`when`(authorService.getAllAuthors()).thenReturn(authors)

        mockMvc.perform(get("/api/authors"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].name").value("John Doe"))
            .andExpect(jsonPath("$[1].birthDate").value("1990-05-15"))
    }

    @Test
    fun `should return an author by ID`() {
        val author = Author(id = 1, name = "John Doe", birthDate = LocalDate.of(1980, 1, 1))

        Mockito.`when`(authorService.getAuthorById(1)).thenReturn(author)

        mockMvc.perform(get("/api/authors/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.birthDate").value("1980-01-01"))
    }

    @Test
    fun `should create a new author`() {
        val author = Author(name = "New Author", birthDate = LocalDate.of(2000, 1, 1))

        Mockito.`when`(authorService.createAuthor(author)).thenReturn(author)

        mockMvc.perform(
            post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author))
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("New Author"))
    }

    @Test
    fun `should update an existing author`() {
        val updatedAuthor = Author(id = 1, name = "Updated Author", birthDate = LocalDate.of(1985, 12, 25))

        Mockito.`when`(authorService.updateAuthor(1, updatedAuthor))
            .thenReturn(updatedAuthor)

        mockMvc.perform(
            put("/api/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAuthor))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Updated Author"))
            .andExpect(jsonPath("$.birthDate").value("1985-12-25"))
    }

    @Test
    fun `should delete an author`() {
        Mockito.doNothing().`when`(authorService).deleteAuthor(1)

        mockMvc.perform(delete("/api/authors/1"))
            .andExpect(status().isNoContent)
    }
}
