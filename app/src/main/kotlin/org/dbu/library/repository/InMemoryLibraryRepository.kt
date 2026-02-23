package org.dbu.library.repository

import org.dbu.library.model.Book
import org.dbu.library.model.Patron

class InMemoryLibraryRepository : LibraryRepository {

    private val books = mutableMapOf<String, Book>()
    private val patrons = mutableMapOf<String, Patron>()

    override fun addBook(book: Book): Boolean {
        return if (books.containsKey(book.isbn)) {
            false
        } else {
            books[book.isbn] = book
            true
        }
    }

    override fun findBook(isbn: String): Book? {
        return books[isbn]
    }

    override fun updateBook(book: Book) {
        books[book.isbn] = book
    }

    override fun addPatron(patron: Patron): Boolean {
        return if (patrons.containsKey(patron.id)) {
            false
        } else {
            patrons[patron.id] = patron
            true
        }
    }

    override fun findPatron(id: String): Patron? {
        return patrons[id]
    }

    override fun updatePatron(patron: Patron) {
        patrons[patron.id] = patron
    }

    override fun getAllBooks(): List<Book> {
        return books.values.toList()
    }
}
