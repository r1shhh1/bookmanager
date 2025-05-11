    package com.rishi.book.manager.controller;

    import com.rishi.book.manager.model.Book;
    import com.rishi.book.manager.service.BookService;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/books")
    public class BookController {

        @Autowired
        private BookService bookService;

        @PostMapping
        public ResponseEntity<Book> addBook(@Valid @RequestBody Book book){
            Book newBook = bookService.createBook(book);
            return new ResponseEntity<>(newBook, HttpStatus.CREATED);
        }

        @GetMapping("/title/{title}")
        public ResponseEntity<Book> findBookByTitle(@PathVariable String title){
            Book book = bookService.findBookByName(title);
            return new ResponseEntity<>(book, HttpStatus.OK);
        }

        @GetMapping
        public ResponseEntity<List<Book>> getAllBooks(){
            List<Book> bookList = bookService.getBooks();
            return new ResponseEntity<>(bookList, HttpStatus.OK);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Book> updateBook(@PathVariable int id,@Valid @RequestBody Book book){
            Book updatedBook = bookService.editBook(id, book);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteBook(@PathVariable int id){
            bookService.deleteBook(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
