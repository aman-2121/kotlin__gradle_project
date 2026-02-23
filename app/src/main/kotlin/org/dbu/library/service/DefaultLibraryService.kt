package org.dbu.library.service

import org.dbu.library.model.Book
import org.dbu.library.repository.LibraryRepository

class DefaultLibraryService(
    private val repository: LibraryRepository
) : LibraryService {

    companion object {
        private const val BORROW_LIMIT = 3
    }

    override fun addBook(book: Book): Boolean {
        return repository.addBook(book)
    }

    override fun borrowBook(patronId: String, isbn: String): BorrowResult {
        // Check if book exists
        val book = repository.findBook(isbn)
        if (book == null) {
            return BorrowResult.BOOK_NOT_FOUND
        }

        // Check if patron exists
        val patron = repository.findPatron(patronId)
        if (patron == null) {
            return BorrowResult.PATRON_NOT_FOUND
        }

        // Check if book is available
        if (!book.isAvailable) {
            return BorrowResult.NOT_AVAILABLE
        }

        // Check if patron has reached borrow limit
        if (patron.borrowedBooks.size >= BORROW_LIMIT) {
            return BorrowResult.LIMIT_REACHED
        }

        // Update book to not available
        val updatedBook = book.copy(isAvailable = false)
        repository.updateBook(updatedBook)

        // Update patron's borrowed books
        val updatedPatron = patron.copy(
            borrowedBooks = patron.borrowedBooks + isbn
        )
        repository.updatePatron(updatedPatron)

        return BorrowResult.SUCCESS
    }

    override fun returnBook(patronId: String, isbn: String): Boolean {
        // Check if book exists
        val book = repository.findBook(isbn)
        if (book == null) {
            return false
        }

        // Check if patron exists
        val patron = repository.findPatron(patronId)
        if (patron == null) {
            return false
        }

        // Check if patron has borrowed this book
        if (!patron.borrowedBooks.contains(isbn)) {
            return false
        }

        // Update book to available
        val updatedBook = book.copy(isAvailable = true)
        repository.updateBook(updatedBook)

        // Update patron's borrowed books
        val updatedPatron = patron.copy(
            borrowedBooks = patron.borrowedBooks - isbn
        )
        repository.updatePatron(updatedPatron)

        return true
    }

    override fun search(query: String): List<Book> {
        val lowerQuery = query.lowercase()
        return repository.getAllBooks().filter { book ->
            book.title.lowercase().contains(lowerQuery) ||
            book.author.lowercase().contains(lowerQuery)
        }
    }
}
