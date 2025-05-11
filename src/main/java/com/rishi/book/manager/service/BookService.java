package com.rishi.book.manager.service;

import com.rishi.book.manager.model.Book;

import java.util.List;
import java.util.UUID;

public interface BookService {

    Book createBook(Book book);
    Book findBookByName(String name);
    Book editBook(int id, Book book);
    void deleteBook(int id);

    List<Book> getBooks();

}
