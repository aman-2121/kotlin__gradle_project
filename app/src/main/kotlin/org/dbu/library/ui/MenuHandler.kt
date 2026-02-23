package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService
import org.dbu.library.util.display

fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {

    return when (choice) {

        "1" -> {
            addBook(service)
            true
        }

        "2" -> {
            registerPatron(repository)
            true
        }

        "3" -> {
            borrowBook(service)
            true
        }

        "4" -> {
            returnBook(service)
            true
        }

        "5" -> {
            search(service)
            true
        }

        "6" -> {
            listAllBooks(repository)
            true
        }

        "0" -> false

        else -> {
            println("Invalid option")
            true
        }
    }
}

fun addBook(service: LibraryService) {
    println("Enter ISBN:")
    val isbn = readln().trim()
    println("Enter title:")
    val title = readln().trim()
    println("Enter author:")
    val author = readln().trim()
    println("Enter year:")
    val year = readln().trim().toIntOrNull() ?: 0

    val book = Book(isbn, title, author, year)
    val success = service.addBook(book)
    if (success) {
        println("Book added successfully!")
    } else {
        println("Book with this ISBN already exists.")
    }
}

fun registerPatron(repository: LibraryRepository) {
    println("Enter patron ID:")
    val id = readln().trim()
    println("Enter name:")
    val name = readln().trim()

    val patron = Patron(id, name)
    val success = repository.addPatron(patron)
    if (success) {
        println("Patron registered successfully!")
    } else {
        println("Patron with this ID already exists.")
    }
}

fun borrowBook(service: LibraryService) {
    println("Enter patron ID:")
    val patronId = readln().trim()
    println("Enter ISBN:")
    val isbn = readln().trim()

    val result = service.borrowBook(patronId, isbn)
    when (result) {
        BorrowResult.SUCCESS -> println("Book borrowed successfully!")
        BorrowResult.BOOK_NOT_FOUND -> println("Book not found.")
        BorrowResult.PATRON_NOT_FOUND -> println("Patron not found.")
        BorrowResult.NOT_AVAILABLE -> println("Book is not available.")
        BorrowResult.LIMIT_REACHED -> println("Patron has reached the borrow limit (3 books).")
    }
}

fun returnBook(service: LibraryService) {
    println("Enter patron ID:")
    val patronId = readln().trim()
    println("Enter ISBN:")
    val isbn = readln().trim()

    val success = service.returnBook(patronId, isbn)
    if (success) {
        println("Book returned successfully!")
    } else {
        println("Failed to return book. Check patron ID and ISBN.")
    }
}

fun search(service: LibraryService) {
    println("Enter search query (title or author):")
    val query = readln().trim()

    val results = service.search(query)
    if (results.isEmpty()) {
        println("No books found.")
    } else {
        println("Search results:")
        results.forEach { book ->
            val status = if (book.isAvailable) "Available" else "Borrowed"
            println("- ${book.display()} [$status]")
        }
    }
}

fun listAllBooks(repository: LibraryRepository) {
    val books = repository.getAllBooks()
    if (books.isEmpty()) {
        println("No books in the library.")
    } else {
        println("All books:")
        books.forEach { book ->
            val status = if (book.isAvailable) "Available" else "Borrowed"
            println("- ${book.display()} [$status]")
        }
    }
}
