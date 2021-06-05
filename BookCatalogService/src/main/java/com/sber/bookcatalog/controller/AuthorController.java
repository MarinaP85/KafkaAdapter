package com.sber.bookcatalog.controller;

import com.sber.bookcatalog.exception.ServiceException;
import com.sber.bookcatalog.model.AuthorDto;
import com.sber.bookcatalog.model.BookDto;
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
    public ResponseEntity<List<String>> readAllAuthors(@RequestHeader("genre") String genre)
            throws ServiceException {
        final List<String> authors = bookCatalogService.readAllAuthors();

        return authors != null && !authors.isEmpty() && genre.equals("fantasy")
                ? new ResponseEntity<>(authors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/authors/{id}")
    public ResponseEntity<AuthorDto> readAuthorById(@PathVariable(name = "id") int id)
            throws ServiceException {
        final AuthorDto author = bookCatalogService.readAuthorById(id);

        return author != null
                ? new ResponseEntity<>(author, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/authors")
    public ResponseEntity<?> createAuthor(@RequestBody AuthorDto author) throws ServiceException {
        final boolean create = bookCatalogService.createAuthor(author);
        return create
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping(value = "/authors")
    public ResponseEntity<?> updateAuthor(@RequestBody AuthorDto author) throws ServiceException {
        final boolean updated = bookCatalogService.updateAuthor(author);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/authors/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable(name = "id") int id) throws ServiceException {
        final boolean deleted = bookCatalogService.deleteAuthor(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(value = "/authors")
    public ResponseEntity<?> readBooksByAuthorAndTitle(@RequestParam String author,
                                                       @RequestParam String title)
            throws ServiceException {
        BookDto book = bookCatalogService.readBookByAuthorAndTitle(author, title);
        return book != null
                ? new ResponseEntity<>(book, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
