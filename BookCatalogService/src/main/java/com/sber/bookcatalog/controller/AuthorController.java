package com.sber.bookcatalog.controller;

import com.sber.bookcatalog.exception.ServiceException;
import com.sber.bookcatalog.model.Author;
import com.sber.bookcatalog.model.Book;
import com.sber.bookcatalog.service.BookCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthorController {

    private final BookCatalogService bookCatalogService;

    @Autowired
    public AuthorController(BookCatalogService bookCatalogService) {
        this.bookCatalogService = bookCatalogService;
    }

    @GetMapping(value = "/authors/all")
    public ResponseEntity<List<Author>> readAllAuthors(@RequestHeader("Content-Language") String language)
            throws ServiceException {
        List<Author> authors = null;
        if (language.contains("ru")) {
            authors = bookCatalogService.readAllAuthors();
        }

        return authors != null && !authors.isEmpty()
                ? new ResponseEntity<>(authors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/authors/list")
    public ResponseEntity<List<String>> readListAuthors(@RequestHeader("Content-Language") String language)
            throws ServiceException {
        List<String> authors = null;
        if (language.contains("ru")) {
            authors = bookCatalogService.readListAuthors();
        }

        return authors != null && !authors.isEmpty()
                ? new ResponseEntity<>(authors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/authors/{id}")
    public ResponseEntity<Author> readAuthorById(@PathVariable(name = "id") long id)
            throws ServiceException {
        final Author author = bookCatalogService.readAuthorById(id);

        return author != null
                ? new ResponseEntity<>(author, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/authors")
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) throws ServiceException {
        final Author returnAuthor = bookCatalogService.createAuthor(author);
        return returnAuthor != null
                ? new ResponseEntity<>(returnAuthor, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping(value = "/authors/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable(name = "id") long id,
                                          @RequestBody Author author) throws ServiceException {
        final boolean updated = bookCatalogService.updateAuthor(id, author);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/authors/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable(name = "id") long id) throws ServiceException {
        final boolean deleted = bookCatalogService.deleteAuthor(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(value = "/authors")
    public ResponseEntity<?> readBooksByAuthorAndTitle(@RequestParam String author,
                                                       @RequestParam String title)
            throws ServiceException {
        Book book = bookCatalogService.readBookByAuthorAndTitle(author, title);
        return book != null
                ? new ResponseEntity<>(book, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
