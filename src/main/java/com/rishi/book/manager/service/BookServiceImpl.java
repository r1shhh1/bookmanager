package com.rishi.book.manager.service;

import com.rishi.book.manager.exception.BookNotFoundException;
import com.rishi.book.manager.model.Book;
import com.rishi.book.manager.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book findBookByName(String name) {
        return bookRepository.findByTitle(name);
    }

    @Override
    public Book editBook(int id, Book book) {
        Book savedBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));

        if (book.getTitle() != null && !book.getTitle().isEmpty()) {
            savedBook.setTitle(book.getTitle());
        }

        if (book.getAuthor() != null && !book.getAuthor().isEmpty()) {
            savedBook.setAuthor(book.getAuthor());
        }

        if (book.getGenre() != null && !book.getGenre().isEmpty()) {
            savedBook.setGenre(book.getGenre());
        }

        return bookRepository.save(savedBook);
    }

    @Override
    public void deleteBook(int id) {
        Book savedBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));

        bookRepository.delete(savedBook);
    }

    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }
}
