package com.quoCard.bookManagementApp.service

import com.quoCard.bookManagementApp.model.Author
import com.quoCard.bookManagementApp.model.Book
import com.quoCard.bookManagementApp.repository.AuthorRepository
import com.quoCard.bookManagementApp.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class AuthorService(
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository
) {
    fun getAllAuthors(): List<Author> {
        return authorRepository.findAll()
    }

    fun getAuthorById(id: Long): Author {
        return authorRepository.findById(id)
            ?: throw IllegalArgumentException("Author with ID $id not found")
    }

    fun createAuthor(author: Author): Author {
        return authorRepository.save(author)
    }

    fun updateAuthor(id: Long, updatedAuthor: Author): Author {
        val existingAuthor = getAuthorById(id)
        val authorToSave = existingAuthor.copy(
            name = updatedAuthor.name,
            birthDate = updatedAuthor.birthDate,
            books = updatedAuthor.books
        )
        return authorRepository.save(authorToSave)
    }

    fun deleteAuthor(id: Long) {
        authorRepository.deleteById(id)
    }

    fun getBooksByAuthorId(authorId: Long): List<Book> {
        return bookRepository.findByAuthor(authorId)
    }
}