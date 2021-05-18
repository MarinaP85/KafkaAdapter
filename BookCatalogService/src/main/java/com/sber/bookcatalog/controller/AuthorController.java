package com.sber.bookcatalog.controller;

import com.sber.bookcatalog.model.AuthorDto;
import com.sber.bookcatalog.model.BookDto;
import com.sber.bookcatalog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthorController {

    private final CatalogService catalogService;

    @Autowired
    public AuthorController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping(value = "/authors/all")
    public ResponseEntity<List<String>> readAllAuthors() {
        final List<String> authors = catalogService.readAllAuthors();

        return authors != null && !authors.isEmpty()
                ? new ResponseEntity<>(authors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/authors/{id}")
    public ResponseEntity<AuthorDto> readAuthorById(@PathVariable(name = "id") int id) {
        final AuthorDto author = catalogService.readAuthorById(id);

        return author != null
                ? new ResponseEntity<>(author, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/authors")
    public ResponseEntity<?> createAuthor(@RequestBody AuthorDto author) {
        final boolean create = catalogService.createAuthor(author);
        return create
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping(value = "/authors")
    public ResponseEntity<?> updateAuthor(@RequestBody AuthorDto author) {
        final boolean updated = catalogService.updateAuthor(author);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/authors/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable(name = "id") int id) {
        final boolean deleted = catalogService.deleteAuthor(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping(value = "/authors")
    public ResponseEntity<?> readBooksByAuthorAndTitle(@RequestParam String author,
                                                       @RequestParam String title) {
        BookDto book = catalogService.readBookByAuthorAndTitle(author, title);
        return book != null
                ? new ResponseEntity<>(book, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
